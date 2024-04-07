package com.fsdm.hopital.controllers;

import com.fsdm.hopital.entities.Patient;
import com.fsdm.hopital.services.PatientsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final PatientsService patientsService;
    @PostMapping("/createPatient")
    public Patient createPatient(@RequestBody Patient patient){
        return patientsService.createPatient(patient);
    }
}
