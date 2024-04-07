package com.fsdm.hopital.dto;

import com.fsdm.hopital.entities.CareActivity;
import com.fsdm.hopital.entities.Patient;
import com.fsdm.hopital.entities.User;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareGiverDTO extends User {
    List<PatientDTO> associatedPatients;
    List<CareActivity> activities;
}
