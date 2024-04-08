package com.fsdm.hopital.controllers;

import com.fsdm.hopital.dto.ActionEntity;
import com.fsdm.hopital.dto.AppointmentDTO;
import com.fsdm.hopital.entities.Appointment;
import com.fsdm.hopital.repositories.AppointmentRepository;
import com.fsdm.hopital.services.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/appointment")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final AppointmentRepository appointmentRepository;
    @PostMapping("/create")
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentDTO appointment) {
        Appointment savedAppointment = appointmentService.createAppointment(appointment);
        return ResponseEntity.ok(savedAppointment);
    }
    @GetMapping("/{id}")
    public Appointment getAppointment(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id);
    }
    @GetMapping
    public List<Appointment> getAppointmentsOfUser(@RequestParam("userId") Long id) {
        return appointmentService.getAllUserAppointments(id);
    }
    @GetMapping("/user-schedule/{user_id}")
    public List<Date> userSchedule(@PathVariable Long user_id){
        return appointmentService.getSchedule(user_id);
    }
    @DeleteMapping("/{id}")
    public ActionEntity deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return new ActionEntity("DELETE_APPOINTMENT","Appointment deleted successfully", true);
    }
    @PutMapping("/update/{id}")
    public ActionEntity updateAppointment(@PathVariable Long id, @RequestBody AppointmentDTO appointment) {
        appointmentService.updateAppointment(appointment);
        return new ActionEntity("UPDATE_APPOINTMENT","Appointment updated successfully", true);
    }
    @DeleteMapping("/all/{userId}")
    public ActionEntity deleteAllAppointmentsOfUser(@PathVariable Long userId) {
        appointmentService.deleteAllUserAppointments(userId);
        return new ActionEntity("DELETE_ALL_APPOINTMENTS","All appointments deleted successfully", true);
    }
}
