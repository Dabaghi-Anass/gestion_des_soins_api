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
    @GetMapping("/patient-requests/{user_id}")
    public List<TreatmentRequest> getPatientTreatmentRequests(@PathVariable Long user_id) {
        return treatmentsService.getTreatmentRequestsOfPatient(user_id);
    }
    @PostMapping("/request-treatment")
    public TreatmentRequest requestTreatment(@RequestBody TreatmentRequestDTO treatmentRequestDTO) {
        return treatmentsService.requestTreatment(treatmentRequestDTO);
    }
    public record TreatmentReview(String review){};
    @PutMapping("/add-review/{id}")
    public Treatment addReviewTreatment(@PathVariable Long id, @RequestBody TreatmentReview notes) {
        return treatmentsService.addTreatmentNotes(id,notes.review);
    }
    @PostMapping("/add-treatment")
    public Treatment addTreatment(@RequestBody TreatmentDTO treatmentDTO){
        return treatmentsService.addTreatment(treatmentDTO);
    }
    @DeleteMapping("/request/{id}")
    public void deleteRequest(@PathVariable Long id){
        treatmentsService.deleteRequest(id);
    }
    @PutMapping("/update-treatment")
    public Treatment updateTreatment(@RequestBody TreatmentDTO treatmentDTO){
        return treatmentsService.updateTreatment(treatmentDTO);
    }
    @GetMapping("/treatment-by-request/{id}")
    public Treatment addTreatment(@PathVariable Long id){
        return treatmentsService.getTreatmentByRequestId(id);
    }
    @GetMapping("/{user_id}")
    public List<Treatment> getTreatments(@PathVariable Long user_id, @RequestParam Integer limit, @RequestParam Integer offset){
        if(limit == null || offset == null)
            return treatmentsService.getAllTreatments(user_id);
        return treatmentsService.getTreatments(user_id,offset, limit);
    }
    @GetMapping("/treatment/{id}")
    public Treatment getTreatmentById(@PathVariable Long id){
        return treatmentsService.getTreatmentById(id);
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
