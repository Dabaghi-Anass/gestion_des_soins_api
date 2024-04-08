package com.fsdm.hopital.dto;

import com.fsdm.hopital.entities.BaseEntity;
import com.fsdm.hopital.entities.Patient;
import com.fsdm.hopital.entities.User;
import com.fsdm.hopital.types.AppointementStatus;
import com.fsdm.hopital.types.AppointmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO extends BaseEntity {
    private Long id;
    private Date date;
    private AppointementStatus status;
    private AppointmentType type;
    private String description;
    private Long patientId;
    private Long assignedToId;
    private int duration;
}
