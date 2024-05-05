package com.fsdm.hopital.controllers;

import com.fsdm.hopital.dto.ActionEntity;
import com.fsdm.hopital.dto.CareActivityDTO;
import com.fsdm.hopital.entities.CareActivity;
import com.fsdm.hopital.entities.CareActivity;
import com.fsdm.hopital.repositories.CareActivityRepository;
import com.fsdm.hopital.repositories.CareActivityRepository;
import com.fsdm.hopital.services.CareActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/care-activity")
@RequiredArgsConstructor
public class CareActivityController {
        private final CareActivityService careActivityService;
        private final CareActivityRepository careActivityRepository;
        @PostMapping("/create")
        public ResponseEntity<CareActivity> createCareActivity(@RequestBody CareActivityDTO appointment) {
            CareActivity savedCareActivity = careActivityService.createCareActivity(appointment);
            return ResponseEntity.ok(savedCareActivity);
        }
        @GetMapping("/{id}")
        public CareActivity getCareActivity(@PathVariable Long id) {
            return careActivityService.getCareActivityById(id);
        }
        record CareActivityResponse(List<CareActivity> appointments, boolean hasNext){}
        @GetMapping
        public CareActivityResponse getCareActivityOfUser(@RequestParam("userId") Long id,
                                                          @RequestParam(name="offset") int offset,
                                                          @RequestParam(name="limit") int limit) {
            List<CareActivity> appointments = careActivityService.getAllUserCareActivitys(id, offset,limit);
            return new CareActivityResponse(appointments, appointments.size() == limit);
        }
        @GetMapping("/user-schedule/{user_id}")
        public List<Date> userSchedule(@PathVariable Long user_id){
            return careActivityService.getSchedule(user_id);
        }
        @DeleteMapping("/{id}")
        public ActionEntity deleteCareActivity(@PathVariable Long id) {
            careActivityService.deleteCareActivity(id);
            return new ActionEntity("DELETE_CARE_ACTIVITY","CareActivity deleted successfully", true);
        }
        @PutMapping("/update")
        public ActionEntity updateCareActivity(@RequestBody CareActivityDTO appointment) {
            careActivityService.updateCareActivity(appointment);
            return new ActionEntity("UPDATE_CARE_ACTIVITY","CareActivity updated successfully", true);
        }
        @PutMapping("/accept/{id}")
        public CareActivity acceptCareActivity(@PathVariable Long id) {
            return  careActivityService.acceptCareActivity(id);
        }
        @PutMapping("/reject/{id}")
        public CareActivity rejectCareActivity(@PathVariable Long id) {
            return  careActivityService.rejectCareActivity(id);
        }
        @PutMapping("/cancel/{id}")
        public CareActivity cancelCareActivity(@PathVariable Long id) {
            return careActivityService.cancelCareActivity(id);
        }
        @PutMapping("/complete/{id}")
        public CareActivity completeCareActivity(@PathVariable Long id) {
            return careActivityService.completeCareActivity(id);
        }
        @PutMapping("/uncomplete/{id}")
        public CareActivity unCompleteCareActivity(@PathVariable Long id) {
            return careActivityService.unCompleteCareActivity(id);
        }

        @DeleteMapping("/all/{userId}")
        public void deleteAllCareActivitysOfUser(@PathVariable Long userId) {
            careActivityService.deleteAllUserCareActivitys(userId);
        }
    
}
