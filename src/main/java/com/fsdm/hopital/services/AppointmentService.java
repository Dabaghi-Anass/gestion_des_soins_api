package com.fsdm.hopital.services;

import com.fsdm.hopital.dto.AppointmentDTO;
import com.fsdm.hopital.dto.EntityToDto;
import com.fsdm.hopital.entities.Appointment;
import com.fsdm.hopital.exceptions.AppException;
import com.fsdm.hopital.exceptions.ProcessingException;
import com.fsdm.hopital.repositories.AppointmentRepository;
import com.fsdm.hopital.types.Status;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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
        var hours = (int) Math.floor(appointment.getDuration());
        var minutes = (int)(appointment.getDuration() - hours) * 60;
        LocalDateTime newAppointmentEnd = newAppointmentStart.plusHours(hours).plusMinutes(minutes);
        for (Appointment existingAppointment : appointments) {
            var ehours = (int) Math.floor(existingAppointment.getDuration());
            var eminutes = (int)(existingAppointment.getDuration() - hours) * 60;
            LocalDateTime existingAppointmentStart = existingAppointment.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime existingAppointmentEnd = existingAppointmentStart.plusHours(ehours).plusMinutes(eminutes);
            if (newAppointmentStart.isBefore(existingAppointmentEnd) && newAppointmentEnd.isAfter(existingAppointmentStart)) {
                if(existingAppointment.isAccepted()) return true;
            }
        }
        return false;
    }
    public static List<Appointment> containsOverlap(List<Appointment> appointments, Appointment appointment) {
        List<Appointment> overlaps = new ArrayList<>();
        LocalDateTime newAppointmentStart = appointment.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        var hours = (int) Math.floor(appointment.getDuration());
        var minutes = (int)((appointment.getDuration() - hours) * 60);
        LocalDateTime newAppointmentEnd = newAppointmentStart.plusHours(hours).plusMinutes(minutes);
        for (Appointment existingAppointment : appointments) {
            if(existingAppointment.getId() == appointment.getId()) continue;
            var ehours = (int) Math.floor(existingAppointment.getDuration());
            var eminutes = (int)((existingAppointment.getDuration() - hours) * 60);
            LocalDateTime existingAppointmentStart = existingAppointment.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime existingAppointmentEnd = existingAppointmentStart.plusHours(ehours).plusMinutes(eminutes);
            if (newAppointmentStart.isBefore(existingAppointmentEnd) && newAppointmentEnd.isAfter(existingAppointmentStart)) {
                if(existingAppointment.isAccepted()) overlaps.add(appointment);
                else overlaps.add(existingAppointment);
            }
        }
        return overlaps;
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
    public Appointment updateAppointment(Long id,AppointmentDTO appointment){
        if (appointment == null) throw new AppException(ProcessingException.APPOINTMENT_NOT_FOUND);
        if(appointmentTaken(appointment)) throw new AppException(ProcessingException.USER_NOT_AVAILABLE_AT_THIS_TIME);
        Appointment appointment1 =
                appointmentRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ProcessingException.APPOINTMENT_NOT_FOUND));
        if(appointment.getDate() != null) appointment1.setDate(appointment.getDate());
        if(appointment.getStatus() != null) appointment1.setStatus(appointment.getStatus());
        if(appointment.getType() != null) appointment1.setType(appointment.getType());
        if(appointment.getReason() != null) appointment1.setReason(appointment.getReason());
        if(appointment.getDuration() != 0) appointment1.setDuration(appointment.getDuration());
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
    class AppointmentDateComparator implements Comparator<Appointment> {
        @Override
        public int compare(Appointment appointment1, Appointment appointment2) {
            return appointment2.getDate().compareTo(appointment1.getDate());
        }
    }
    public List<Appointment> getAllUserAppointments(Long user_id, int offset, int limit){
        AtomicBoolean mutatedArray = new AtomicBoolean(false);
        List<Appointment> appointments;
        if(limit == 0 && offset == 0) {
            appointments =  appointmentRepository.findAllByUserId(user_id);
        }else {
            Pageable page = PageRequest.of(offset,limit);
            appointments =  appointmentRepository.findAllByUserId(user_id, page);
            appointments.sort(new AppointmentDateComparator());
        }
        appointments.forEach(appointment -> {
            if(appointment.getDate().before(new Date()) && !appointment.isAccepted()){
                appointment.setStatus(Status.DENIED);
                mutatedArray.set(true);
            }else if(appointment.getDate().before(new Date()) && appointment.isAccepted()){
                appointment.setStatus(Status.DONE);
                mutatedArray.set(true);
            }
        });
        if(mutatedArray.get()) return appointmentRepository.saveAllAndFlush(appointments);
        return appointments;
    }

    public void deleteAllUserAppointments(Long user_id) {
        appointmentRepository.deleteAllByUserId(user_id);
    }

    public Appointment acceptAppointment(Long id) {
        Appointment appointment = getAppointmentById(id);
        appointment.setAccepted(true);
        appointment.setStatus(Status.SCHEDULED);
        Appointment savedAppointment = appointmentRepository.saveAndFlush(appointment);
        List<Appointment> userAppointments = getAllUserAppointments(savedAppointment.getAssignedTo().getId(), 0,0); //doctor or nurse
        List<Appointment> patientAppointments = getAllUserAppointments(savedAppointment.getPatient().getId(), 0,0); //patient
        if(userAppointments != null){
            List<Appointment> overlaps = containsOverlap(userAppointments, savedAppointment);
            System.out.println(overlaps);
            if(overlaps.size() > 0){
                overlaps.forEach(appointment1 -> {
                    if(appointment1.isAccepted()) {
                        appointment.setAccepted(false);
                        appointment.setStatus(Status.DENIED);
                    }else{
                        appointment1.setStatus(Status.DENIED);
                        appointment1.setAccepted(false);
                    }
                });
                appointmentRepository.saveAllAndFlush(overlaps);
            }
        }
        if(patientAppointments != null){
            List<Appointment> overlaps = containsOverlap(patientAppointments, savedAppointment);
            System.out.println(overlaps);
            if(overlaps.size() > 0){
                overlaps.forEach(appointment1 -> {
                    if(appointment1.isAccepted()) {
                        appointment.setAccepted(false);
                        appointment.setStatus(Status.DENIED);
                    }else{
                        appointment1.setStatus(Status.DENIED);
                        appointment1.setAccepted(false);
                    }
                });
                appointmentRepository.saveAllAndFlush(overlaps);
            }
        }
        return savedAppointment;
    }
    public Appointment rejectAppointment(Long id) {
        Appointment appointment = getAppointmentById(id);
        appointment.setAccepted(false);
        appointment.setStatus(Status.DENIED);
        return  appointmentRepository.save(appointment);
    }
    public Appointment cancelAppointment(Long id) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(Status.CANCELED);
       return appointmentRepository.save(appointment);
    }

    public Appointment completeAppointment(Long id) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(Status.DONE);
        return appointmentRepository.save(appointment);
    }

    public Appointment unCompleteAppointment(Long id) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(Status.SCHEDULED);
        return appointmentRepository.save(appointment);
    }
}
