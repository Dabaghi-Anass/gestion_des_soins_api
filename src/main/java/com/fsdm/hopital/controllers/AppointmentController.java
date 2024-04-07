package com.fsdm.hopital.controllers;

import com.fsdm.hopital.dto.AppointmentDTO;
import com.fsdm.hopital.entities.Appointment;
import com.fsdm.hopital.services.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointment")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    @PostMapping("/create")
    public String createAppointment(@RequestBody AppointmentDTO appointment) {
        System.out.println("Appointment: " + appointment.getDate());
        Appointment savedAppointment = appointmentService.createAppointment(appointment);
//        return ResponseEntity.ok(savedAppointment);
        return "Appointment created successfully";
    }
    @GetMapping("/{user_id}")
    public List<AppointmentDTO> getAppointments(@PathVariable Long user_id) {
        return appointmentService.getAppointments(user_id);
    }
}
