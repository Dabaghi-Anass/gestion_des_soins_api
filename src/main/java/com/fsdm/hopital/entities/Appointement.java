package com.fsdm.hopital.entities;

import com.fsdm.hopital.types.AppointementStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String date;
    private String time;
    private AppointementStatus status;
    private String type;
    private String description;
    private String patient;
    private String doctor;
    private String nurse;
    private String companion;
}
