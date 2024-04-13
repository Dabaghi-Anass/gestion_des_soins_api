package com.fsdm.hopital.dto;

import com.fsdm.hopital.entities.BaseEntity;
import com.fsdm.hopital.types.AppointmentStatus;
import com.fsdm.hopital.types.AppointmentType;
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
    private AppointmentStatus status;
    private AppointmentType type;
    private String description;
    private Long patientId;
    private Long assignedToId;
    private int duration;
}
