package com.fsdm.hopital.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "care_givers")
@AllArgsConstructor
@NoArgsConstructor
public class CareGiver extends User {
    @OneToMany(mappedBy = "careGiver")
    List<Patient> associatedPatients;
    @OneToMany(mappedBy = "assignedTo")
    List<CareActivity> activities;
}
