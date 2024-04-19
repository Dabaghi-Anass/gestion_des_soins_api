package com.fsdm.hopital.dto;

import com.fsdm.hopital.entities.Treatment;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalInformation implements Serializable {
    private float temperature;
    private String bloodType;
    private String allergies;
    @ManyToMany
    private List<Treatment> medicalHistory;
}
