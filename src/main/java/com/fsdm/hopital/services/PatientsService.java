package com.fsdm.hopital.services;

import com.fsdm.hopital.dto.MedicalInformation;
import com.fsdm.hopital.dto.PatientDTO;
import com.fsdm.hopital.entities.CareGiver;
import com.fsdm.hopital.entities.Companion;
import com.fsdm.hopital.entities.Patient;
import com.fsdm.hopital.exceptions.AppException;
import com.fsdm.hopital.exceptions.ProcessingException;
import com.fsdm.hopital.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientsService {
    private final PatientRepository patientRepository;
    private final UserService userService;
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

    public Patient updatePatient(PatientDTO patientDTO) {
        try{
            Optional<Patient> patientOptional = patientRepository.findById(patientDTO.getId());
            if(patientOptional.isEmpty()) throw new AppException(ProcessingException.USER_NOT_FOUND);
            Patient patient = patientOptional.get();
            if(patient.getMedicalInformation() == null){
                patient.setMedicalInformation(new MedicalInformation());
            }
            if(patientDTO.getMedicalInformation() != null){
                MedicalInformation infos = updateMedicalInformation(patientDTO, patient);
                patient.setMedicalInformation(infos);
            }
            if(patientDTO.getCompanionId() != null){
                Companion companion = userService.getCompanionById(patientDTO.getCompanionId());
                patient.setCompanion(companion);
            }
            return patientRepository.save(patientOptional.get());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static MedicalInformation updateMedicalInformation(PatientDTO patientDTO, Patient patient) {
        MedicalInformation infos = patient.getMedicalInformation();
        if(patientDTO.getMedicalInformation() == null){
            infos = new MedicalInformation();
        }
        if(patientDTO.getMedicalInformation().getAllergies() != null){
            infos.setAllergies(patientDTO.getMedicalInformation().getAllergies());
        }
        if(patientDTO.getMedicalInformation().getBloodType() != null){
            infos.setBloodType(patientDTO.getMedicalInformation().getBloodType());
        }
        return infos;
    }
    public List<Patient> getAllPatients(){
        return patientRepository.findAll();
    }
}
