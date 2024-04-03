package com.fsdm.hopital.types;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalInformation {
    // Vital signs
    private float temperature;
    private int bloodPressure;
    private int heartRate;
    private int respiratoryRate;
    private String bloodType;

    private String allergies;
    private String medicalHistory; //treatment history
    private String emergencyContact;
    private String insuranceNumber;
    private String insuranceCompany;

}
