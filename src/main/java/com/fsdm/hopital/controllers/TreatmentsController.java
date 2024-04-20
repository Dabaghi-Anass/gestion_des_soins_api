package com.fsdm.hopital.controllers;

import com.fsdm.hopital.dto.TreatmentDTO;
import com.fsdm.hopital.dto.TreatmentRequestDTO;
import com.fsdm.hopital.entities.Treatment;
import com.fsdm.hopital.entities.TreatmentRequest;
import com.fsdm.hopital.services.TreatmentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatments")
@RequiredArgsConstructor
public class TreatmentsController {
    private final TreatmentsService treatmentsService;
    @GetMapping("/requests/{user_id}")
    public List<TreatmentRequest> getTreatmentRequests(@PathVariable Long user_id) {
        return treatmentsService.getTreatmentRequestsOfUser(user_id);
    }
    @PostMapping("/request-treatment")
    public TreatmentRequest requestTreatment(@RequestBody TreatmentRequestDTO treatmentRequestDTO) {
        return treatmentsService.requestTreatment(treatmentRequestDTO);
    }
    @PostMapping("/add-treatment")
    public Treatment addTreatment(@RequestBody TreatmentDTO treatmentDTO){
        return treatmentsService.addTreatment(treatmentDTO);
    }
    @GetMapping("/{user_id}")
    public List<Treatment> getTreatments(@PathVariable Long user_id, @RequestParam Integer limit, @RequestParam Integer offset){
        if(limit == null || offset == null)
            return treatmentsService.getAllTreatments(user_id);
        return treatmentsService.getTreatments(user_id,offset, limit);
    }
    @GetMapping("/all/{user_id}")
    public List<Treatment> getTreatments(@PathVariable Long user_id){
       return treatmentsService.getAllTreatments(user_id);
    }
    @PutMapping("/deny/{id}")
    public TreatmentRequest denyTreatmentRequest(@PathVariable Long id){
        return treatmentsService.denyTreatmentRequest(id);
    }
    @PutMapping("/accept/{id}")
    public TreatmentRequest acceptTreatmentRequest(@PathVariable Long id){
        return treatmentsService.acceptTreatmentRequest(id);
    }
}
