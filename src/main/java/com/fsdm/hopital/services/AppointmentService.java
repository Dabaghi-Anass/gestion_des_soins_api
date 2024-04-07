package com.fsdm.hopital.services;

import com.fsdm.hopital.dto.AppointmentDTO;
import com.fsdm.hopital.dto.EntityToDto;
import com.fsdm.hopital.entities.Appointment;
import com.fsdm.hopital.entities.Patient;
import com.fsdm.hopital.entities.User;
import com.fsdm.hopital.exceptions.AppException;
import com.fsdm.hopital.exceptions.ProcessingException;
import com.fsdm.hopital.repositories.AppointmentRepository;
import com.fsdm.hopital.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

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
    @SneakyThrows
    public Appointment createAppointment(AppointmentDTO appointment) {
        Long userId = appointment.getAssignedToId();
        Long  patientId = appointment.getPatientId();
        User user = userService.getUserById(userId); //doctor or nurse
        User patient = userService.getUserById(patientId);
        List<Appointment> userAppointments = user.getSchedule();
        List<Appointment> patientAppointments = patient.getSchedule();
        if(userAppointments != null){
            for(Appointment appointment1 : userAppointments){
                if(appointment1.getDate().equals(appointment.getDate())){
                    throw new AppException(ProcessingException.USER_NOT_AVAILABLE_AT_THIS_TIME);
                }
            }
        }
       if(patientAppointments != null) {
           for(Appointment appointment1 : patientAppointments){
               if(appointment1.getDate().equals(appointment.getDate())){
                   throw new AppException(ProcessingException.USER_NOT_AVAILABLE_AT_THIS_TIME);
               }
           }
       }
       Appointment appointment1 = EntityToDto.appointmentDTOToAppointment(appointment,patientsService, userService);
       return appointmentRepository.save(appointment1);
    }
    @SneakyThrows
    public List<AppointmentDTO> getAppointments(Long user_id) {
        User user = userService.getUserById(user_id);
        List<Appointment> appointments = user.getSchedule();
        List<AppointmentDTO> appointmentDTOS =
                appointments
                        .stream()
                        .map(EntityToDto::appointmentToAppointmentDTO)
                        .toList();
        return appointmentDTOS;
    }
}
