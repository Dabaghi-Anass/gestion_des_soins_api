package com.fsdm.hopital.services;
import com.fsdm.hopital.dto.TreatmentRequestDTO;
import com.fsdm.hopital.entities.Patient;
import com.fsdm.hopital.entities.TreatmentRequest;
import com.fsdm.hopital.entities.User;
import com.fsdm.hopital.exceptions.AppException;
import com.fsdm.hopital.repositories.PatientRepository;
import com.fsdm.hopital.repositories.TreatmentRequestRepository;
import com.fsdm.hopital.types.Status;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TreatmentsService {
    private final TreatmentRequestRepository treatmentRequestRepository;
    private final UserService userService;

    public List<TreatmentRequest> getTreatmentRequestsOfUser(Long user_id) {
        return  treatmentRequestRepository.findAllBySentToId(user_id);
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
}
