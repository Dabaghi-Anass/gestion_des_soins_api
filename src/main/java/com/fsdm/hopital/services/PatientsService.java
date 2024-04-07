package com.fsdm.hopital.services;

import com.fsdm.hopital.entities.Patient;
import com.fsdm.hopital.exceptions.AppException;
import com.fsdm.hopital.exceptions.ProcessingException;
import com.fsdm.hopital.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientsService {
    private final PatientRepository patientRepository;
    @SneakyThrows
    public Patient createPatient(Patient patient){
        if(patient == null) throw new AppException(ProcessingException.INVALID_USER_DETAILS);
        return patientRepository.save(patient);
    }
    @SneakyThrows
    public Patient getPatientById(Long patientId) {
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if(patientOptional.isEmpty()) throw new AppException(ProcessingException.USER_NOT_FOUND);
        return patientOptional.get();
    }
}
