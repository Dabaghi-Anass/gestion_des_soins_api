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
    public List<Treatment> getAllTreatments(@PathVariable Long user_id){
        return treatmentsService.getAllTreatments(user_id);
    }

}
