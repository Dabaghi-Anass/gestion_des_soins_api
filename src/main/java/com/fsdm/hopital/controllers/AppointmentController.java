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
    record AppointmentsResponse(List<Appointment> appointments, boolean hasNext){}
    @GetMapping
    public AppointmentsResponse getAppointmentsOfUser(@RequestParam("userId") Long id,
                                                      @RequestParam(name="offset") int offset,
                                                      @RequestParam(name="limit") int limit) {
        List<Appointment> appointments = appointmentService.getAllUserAppointments(id, offset,limit);
        return new AppointmentsResponse(appointments, appointments.size() == limit);
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
    @PutMapping("/update")
    public ActionEntity updateAppointment(@RequestBody AppointmentDTO appointment) {
        appointmentService.updateAppointment(appointment);
        return new ActionEntity("UPDATE_APPOINTMENT","Appointment updated successfully", true);
    }
    @PutMapping("/accept/{id}")
    public Appointment acceptAppointment(@PathVariable Long id) {
       return  appointmentService.acceptAppointment(id);
    }
    @PutMapping("/reject/{id}")
    public Appointment rejectAppointment(@PathVariable Long id) {
        return  appointmentService.rejectAppointment(id);
    }
    @PutMapping("/cancel/{id}")
    public Appointment cancelAppointment(@PathVariable Long id) {
        return appointmentService.cancelAppointment(id);
    }
    @PutMapping("/complete/{id}")
    public Appointment completeAppointment(@PathVariable Long id) {
        return appointmentService.completeAppointment(id);
    }
     @PutMapping("/uncomplete/{id}")
        public Appointment unCompleteAppointment(@PathVariable Long id) {
            return appointmentService.unCompleteAppointment(id);
        }

    @DeleteMapping("/all/{userId}")
    public void deleteAllAppointmentsOfUser(@PathVariable Long userId) {
        appointmentService.deleteAllUserAppointments(userId);
    }
}
