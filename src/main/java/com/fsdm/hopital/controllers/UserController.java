package com.fsdm.hopital.controllers;

import com.fsdm.hopital.dto.DoctorDTO;
import com.fsdm.hopital.dto.PatientDTO;
import com.fsdm.hopital.entities.*;
import com.fsdm.hopital.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final PatientsService patientsService;
    private final DoctorsService doctorsService;
    private final UserService userService;
    private final NurseService nurseService;
    private final CareGiverService careGiverService;
    @PostMapping("/createPatient")
    public Patient createPatient(@RequestBody Patient patient){
        return patientsService.createPatient(patient);
    }
    @PostMapping("/updateDoctor")
    public Doctor createDoctor(@RequestBody DoctorDTO doctor){
        return doctorsService.updateDoctor(doctor);
    }
    @PostMapping("/createNurse")
    public Nurse createNurse(@RequestBody Nurse nurse){
        return nurseService.createNurse(nurse);
    }
    @PostMapping("/createCareGiver")
    public CareGiver createCareGiver(@RequestBody CareGiver careGiver){
        return careGiverService.createCareGiver(careGiver);
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
