package com.fsdm.hopital.dto;

import com.fsdm.hopital.entities.BaseEntity;
import com.fsdm.hopital.entities.CareGiver;
import com.fsdm.hopital.entities.Patient;
import com.fsdm.hopital.types.ActivityType;
import com.fsdm.hopital.types.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareActivityDTO extends BaseEntity {
    private Long id;
    private Date date;
    private Status status;
    private ActivityType type;
    private String description;
    private double duration;
    private boolean isAccepted;
    Long caregiverId;
    Long patientId;
}
