package com.fsdm.hopital.services;

import com.fsdm.hopital.dto.AppointmentDTO;
import com.fsdm.hopital.dto.EntityToDto;
import com.fsdm.hopital.entities.Appointment;
import com.fsdm.hopital.exceptions.AppException;
import com.fsdm.hopital.exceptions.ProcessingException;
import com.fsdm.hopital.repositories.AppointmentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentService {
    @PersistenceContext
    private final EntityManager entityManager;
    private final AppointmentRepository appointmentRepository;
    private final UserService userService;
    private final PatientsService patientsService;
    public static boolean containsOverlap(List<Appointment> appointments, AppointmentDTO appointment) {
        if (appointment.getDate() == null) return true;
        LocalDateTime newAppointmentStart = appointment.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime newAppointmentEnd = newAppointmentStart.plusHours(appointment.getDuration());
        for (Appointment existingAppointment : appointments) {
            LocalDateTime existingAppointmentStart = existingAppointment.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime existingAppointmentEnd = existingAppointmentStart.plusHours(existingAppointment.getDuration());
            if (newAppointmentStart.isBefore(existingAppointmentEnd) && newAppointmentEnd.isAfter(existingAppointmentStart)) {
                return true;
            }
        }
        return false;
    }
    boolean appointmentTaken(AppointmentDTO appointment){
        Long userId = appointment.getAssignedToId();
        Long  patientId = appointment.getPatientId();
        List<Appointment> userAppointments = getAllUserAppointments(userId, 0,0); //doctor or nurse
        List<Appointment> patientAppointments = getAllUserAppointments(patientId, 0,0); //patient
        if(userAppointments != null)
            if(containsOverlap(userAppointments, appointment)) return true;
        if(patientAppointments != null)
            if(containsOverlap(patientAppointments, appointment)) return true;
        return false;
    }
    @SneakyThrows
    public Appointment createAppointment(AppointmentDTO appointment) {
        if(appointmentTaken(appointment)) throw new AppException(ProcessingException.USER_NOT_AVAILABLE_AT_THIS_TIME);
       Appointment appointment1 = EntityToDto.appointmentDTOToAppointment(appointment,patientsService, userService);
       return appointmentRepository.save(appointment1);
    }
    @SneakyThrows
    public Appointment updateAppointment(AppointmentDTO appointment){
        if(appointmentTaken(appointment)) throw new AppException(ProcessingException.USER_NOT_AVAILABLE_AT_THIS_TIME);
        if (appointment == null) throw new AppException(ProcessingException.APPOINTMENT_NOT_FOUND);
        Appointment appointment1 =
                appointmentRepository
                .findById(appointment.getId())
                .orElseThrow(() -> new AppException(ProcessingException.APPOINTMENT_NOT_FOUND));
        if(appointment.getDate() != null) appointment1.setDate(appointment.getDate());
        if(appointment.getStatus() != null) appointment1.setStatus(appointment.getStatus());
        if(appointment.getType() != null) appointment1.setType(appointment.getType());
        if(appointment.getReason() != null) appointment1.setReason(appointment.getReason());
        return appointmentRepository.save(appointment1);
    }
    public List<Date> getSchedule(Long id){
        return getAllUserAppointments(id, 0,0).stream().map(Appointment::getDate).toList();
    }
    public void deleteAppointment(Long id){
        appointmentRepository.deleteById(id);
    }
    @SneakyThrows
    public Appointment getAppointmentById(Long id){
        return appointmentRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ProcessingException.APPOINTMENT_NOT_FOUND));
    }
    public List<Appointment> getAllUserAppointments(Long user_id, int offset, int limit){
        if(limit == 0 && offset == 0) {
            return appointmentRepository.findAllByUserId(user_id);
        }
        Pageable page = PageRequest.of(offset,limit);
        return appointmentRepository.findAllByUserId(user_id, page);
    }

    public void deleteAllUserAppointments(Long user_id) {
        appointmentRepository.deleteAllByUserId(user_id);
    }
}
