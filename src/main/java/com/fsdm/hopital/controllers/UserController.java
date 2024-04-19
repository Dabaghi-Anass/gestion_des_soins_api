package com.fsdm.hopital.controllers;

import com.fsdm.hopital.dto.PatientDTO;
import com.fsdm.hopital.entities.Patient;
import com.fsdm.hopital.entities.User;
import com.fsdm.hopital.services.PatientsService;
import com.fsdm.hopital.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final PatientsService patientsService;
    private final UserService userService;
    @PostMapping("/createPatient")
    public Patient createPatient(@RequestBody Patient patient){
        return patientsService.createPatient(patient);
    }
    @PutMapping("/patient")
    public Patient updatePatient(@RequestBody PatientDTO patientDTO){
       return  patientsService.updatePatient(patientDTO);
    }
    @GetMapping("/{id}")
    public User retreiveUser(@PathVariable Long id){
        return userService.getUserById(id);
    }
}
