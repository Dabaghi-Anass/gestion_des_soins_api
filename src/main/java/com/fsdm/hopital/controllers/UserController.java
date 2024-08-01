package com.fsdm.hopital.controllers;

import com.fsdm.hopital.dto.DoctorDTO;
import com.fsdm.hopital.dto.NurseDTO;
import com.fsdm.hopital.dto.PatientDTO;
import com.fsdm.hopital.entities.*;
import com.fsdm.hopital.repositories.SpecialityRepository;
import com.fsdm.hopital.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final PatientsService patientsService;
    private final DoctorsService doctorsService;
    private final UserService userService;
    private final NurseService nurseService;
    private final CareGiverService careGiverService;
    private final SpecialityRepository specialityRepository;
    @PostMapping("/createPatient")
    public Patient createPatient(@RequestBody Patient patient){
        return patientsService.createPatient(patient);
    }
    @PostMapping("/updateDoctor")
    public Doctor createDoctor(@RequestBody DoctorDTO doctor){
        return doctorsService.updateDoctor(doctor);
    }
    @PostMapping("/updateNurse")
    public Nurse updateNurse(@RequestBody NurseDTO nurseDTO){
        return nurseService.updateNurse(nurseDTO);
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
    @GetMapping("/get-by-profile/{id}")
    public User retreiveUserByProfile(@PathVariable Long id){
        return userService.getUserByProfileId();
    }
    @GetMapping("/specialities")
    public List<String> getAllSpecialities(){
        return specialityRepository.findAllCategories();
    }
    @GetMapping("/employees")
    public List<User> getAllDoctors(){
        return userService.getEmployees();
    }
    @GetMapping("/all-patients")
    public List<Patient> getPatients(){
        return patientsService.getAllPatients();
    }
    @GetMapping("/all-unassociated-patients")
    public List<Patient> getUnAssociatedPatients(){
        return patientsService.getAllUnAssociatedPatients();
    }
    @PutMapping("/companion")
    public Companion updateCompanion(Companion companion){
        return userService.updateCompanion(companion);
    }
    @GetMapping("/most-rated-doctors")
    public List<Doctor> getMostRatedDoctors(){
        return doctorsService.getMostRatedDoctors();
    }
}
