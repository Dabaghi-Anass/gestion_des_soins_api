package com.fsdm.hopital.entities;

import com.fsdm.hopital.types.ActivityType;
import com.fsdm.hopital.types.AppointmentType;
import com.fsdm.hopital.types.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "care_activities")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareActivity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private ActivityType type;
    @Column(length = 3000)
    private String description;
    private double duration;
    private boolean isAccepted;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    CareGiver caregiver;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Patient patient;
}
