package com.fsdm.hopital.dto;

import com.fsdm.hopital.entities.CareGiver;
import com.fsdm.hopital.entities.Companion;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO extends UserDTO {
    private MedicalInformation medicalInformation;
    private Long companionId;
}
