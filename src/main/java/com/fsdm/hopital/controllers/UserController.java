package com.fsdm.hopital.controllers;

import com.fsdm.hopital.dto.PatientDTO;
import com.fsdm.hopital.entities.Patient;
import com.fsdm.hopital.services.PatientsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final PatientsService patientsService;
    @PostMapping("/createPatient")
    public Patient createPatient(@RequestBody Patient patient){
        return patientsService.createPatient(patient);
    }
    @PutMapping("/patient")
    public Patient updatePatient(@RequestBody PatientDTO patientDTO){
       return  patientsService.updatePatient(patientDTO);
    }
}
