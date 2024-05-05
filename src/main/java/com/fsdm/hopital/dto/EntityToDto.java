package com.fsdm.hopital.dto;

import com.fsdm.hopital.entities.Appointment;
import com.fsdm.hopital.entities.CareGiver;
import com.fsdm.hopital.entities.Patient;
import com.fsdm.hopital.entities.User;
import com.fsdm.hopital.services.PatientsService;
import com.fsdm.hopital.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EntityToDto {

    public static PatientDTO patientToPatientDTO(Patient patient) {
        if(patient == null) return null;
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(patient.getId());
        patientDTO.setUsername(patient.getUsername());
        patientDTO.setFirstName(patient.getFirstName());
        patientDTO.setLastName(patient.getLastName());
        patientDTO.setRole(patient.getRole());
        patientDTO.setIsVerified(patient.getIsVerified());
        patientDTO.setProfile(patient.getProfile());
        patientDTO.setMedicalInformation(patient.getMedicalInformation());
        patientDTO.setCareGiverId(patient.getCareGiver().getId());
        return patientDTO;
    }
    public static AppointmentDTO appointmentToAppointmentDTO(Appointment appointment) {
        if(appointment == null) return null;
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(appointment.getId());
        appointmentDTO.setDate(appointment.getDate());
        appointmentDTO.setStatus(appointment.getStatus());
        appointmentDTO.setType(appointment.getType());
        appointmentDTO.setReason(appointment.getReason());
        appointmentDTO.setPatientId(appointment.getPatient().getId());
        appointmentDTO.setAssignedToId(appointment.getAssignedTo().getId());
        appointmentDTO.setCreationDate(appointment.getCreationDate());
        return appointmentDTO;
    }
    public static Appointment appointmentDTOToAppointment(AppointmentDTO appointmentDTO,PatientsService patientsService, UserService userService){
        if(appointmentDTO == null) return null;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentDTO.getId());
        appointment.setDate(appointmentDTO.getDate());
        appointment.setStatus(appointmentDTO.getStatus());
        appointment.setType(appointmentDTO.getType());
        appointment.setReason(appointmentDTO.getReason());
        Patient patient = patientsService.getPatientById(appointmentDTO.getPatientId());
        User user = userService.getUserById(appointmentDTO.getAssignedToId());
        appointment.setPatient(patient);
        appointment.setAssignedTo(user);
        appointment.setCreationDate(appointmentDTO.getCreationDate());
        appointment.setDuration(appointmentDTO.getDuration());
        return appointment;
    }
    public static UserDTO userToUserDTO(User user) {
        if(user == null) return null;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setRole(user.getRole());
        userDTO.setIsVerified(user.getIsVerified());
        userDTO.setProfile(user.getProfile());
        userDTO.setCreationDate(user.getCreationDate());
        userDTO.setLastModifiedDate(user.getLastModifiedDate());
        return userDTO;
    }
    public static CareGiverDTO careGiverToCareGiverDTO(CareGiver careGiver) {
        if(careGiver == null) return null;
        CareGiverDTO careGiverDTO = new CareGiverDTO();
        careGiverDTO.setId(careGiver.getId());
        careGiverDTO.setUsername(careGiver.getUsername());
        careGiverDTO.setFirstName(careGiver.getFirstName());
        careGiverDTO.setLastName(careGiver.getLastName());
        careGiverDTO.setRole(careGiver.getRole());
        careGiverDTO.setIsVerified(careGiver.getIsVerified());
        careGiverDTO.setProfile(careGiver.getProfile());
        careGiverDTO.setActivities(careGiver.getActivities());
        return careGiverDTO;
    }
}
