package com.fsdm.hopital.services;
import com.fsdm.hopital.dto.MedicalInformation;
import com.fsdm.hopital.dto.TreatmentDTO;
import com.fsdm.hopital.dto.TreatmentRequestDTO;
import com.fsdm.hopital.entities.Patient;
import com.fsdm.hopital.entities.Treatment;
import com.fsdm.hopital.entities.TreatmentRequest;
import com.fsdm.hopital.entities.User;
import com.fsdm.hopital.exceptions.AppException;
import com.fsdm.hopital.exceptions.ProcessingException;
import com.fsdm.hopital.repositories.PatientRepository;
import com.fsdm.hopital.repositories.TreatmentRepository;
import com.fsdm.hopital.repositories.TreatmentRequestRepository;
import com.fsdm.hopital.types.Role;
import com.fsdm.hopital.types.Status;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TreatmentsService {
    private final TreatmentRequestRepository treatmentRequestRepository;
    private final TreatmentRepository treatmentRepository;
    private final UserService userService;
    private final PatientRepository patientRepository;

    public List<TreatmentRequest> getTreatmentRequestsOfUser(Long user_id) {
        return  treatmentRequestRepository.findAllBySentToId(user_id);
    }
    public List<TreatmentRequest> getTreatmentRequestsOfPatient(Long user_id) {
        return  treatmentRequestRepository.findAllByUser(user_id);
    }
    public TreatmentRequest requestTreatment(TreatmentRequestDTO treatmentRequestDTO) {
        Long sentToId = treatmentRequestDTO.getSentToId();
        Long sentById = treatmentRequestDTO.getSentById();
        User sentTo = userService.getUserById(sentToId);
        Patient sentBy = userService.getPatientById(sentById);
        TreatmentRequest treatmentRequest = TreatmentRequest.builder()
                .sentBy(sentBy)
                .sentTo(sentTo)
                .description(treatmentRequestDTO.getDescription())
                .responded(false)
                .status(Status.PENDING)
                .title(treatmentRequestDTO.getTitle())
                .build();
        return treatmentRequestRepository.save(treatmentRequest);
    }
    @SneakyThrows
    public Treatment addTreatment(TreatmentDTO treatmentDTO) {
        User sender = userService.getUserById(treatmentDTO.getSentById());
        Patient receiver = userService.getPatientById(treatmentDTO.getSentToId());
        TreatmentRequest request = treatmentRequestRepository.findById(treatmentDTO.getRequestId())
                .orElseThrow(() -> new AppException(ProcessingException.INVALID_OPERATON));
        if(receiver.getMedicalInformation() == null){
           receiver.setMedicalInformation(new MedicalInformation());
        }
        request.setResponded(true);
        request.setStatus(Status.CONFIRMED);
        Treatment treatment = Treatment.builder()
                .response(treatmentDTO.getResponse())
                .title(treatmentDTO.getTitle())
                .review(treatmentDTO.getReview())
                .status(Status.CONFIRMED)
                .sentBy(sender)
                .sentTo(receiver)
                .request(request)
                .build();
        return treatmentRepository.save(treatment);
    }

    public List<Treatment> getAllTreatments(Long userId) {
        return treatmentRepository.findAllByUserId(userId);
    }
    @SneakyThrows
    public TreatmentRequest denyTreatmentRequest(Long id){
        TreatmentRequest request = treatmentRequestRepository.findById(id)
                .orElseThrow(() -> new AppException(ProcessingException.INVALID_OPERATON));
        request.setStatus(Status.DENIED);
        return treatmentRequestRepository.save(request);
    }
    @SneakyThrows
    public TreatmentRequest acceptTreatmentRequest(Long id){
        TreatmentRequest request = treatmentRequestRepository.findById(id)
                .orElseThrow(() -> new AppException(ProcessingException.INVALID_OPERATON));
        request.setStatus(Status.PENDING);
        return treatmentRequestRepository.save(request);
    }

    public List<Treatment> getTreatments(Long userId,int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        return treatmentRepository.findAllByUserId(userId, pageable);
    }

    @SneakyThrows
    public Treatment getTreatmentById(Long id) {
        return treatmentRepository.findById(id).orElseThrow(() -> new AppException(ProcessingException.INVALID_OPERATON));
    }
     @SneakyThrows
    public Treatment addTreatmentNotes(Long id, String notes) {
         Treatment treatment =  treatmentRepository.findById(id).orElseThrow(() -> new AppException(ProcessingException.INVALID_OPERATON));
         treatment.setReview(notes);
         return treatmentRepository.save(treatment);
    }

    public Treatment getTreatmentByRequestId(Long id) {
        return treatmentRepository.findByRequestId(id);
    }
    @SneakyThrows
    public Treatment updateTreatment(TreatmentDTO treatmentDTO) {
        Treatment treatment = treatmentRepository.findById(treatmentDTO.getId())
                .orElseThrow(() -> new AppException(ProcessingException.INVALID_OPERATON));
        if(treatmentDTO.getReview() != null)
            treatment.setReview(treatmentDTO.getReview());
        if(treatmentDTO.getResponse() != null)
            treatment.setResponse(treatmentDTO.getResponse());
        if(treatmentDTO.getStatus() != null)
            treatment.setStatus(treatmentDTO.getStatus());
        if(treatmentDTO.getTitle() != null)
            treatment.setTitle(treatmentDTO.getTitle());
        return treatmentRepository.save(treatment);
    }

    public void deleteRequest(Long id) {
        Treatment treatment = getTreatmentByRequestId(id);
        if(treatment != null){
            treatmentRepository.deleteById(treatment.getId());
        }
        treatmentRequestRepository.deleteById(id);
    }
}
