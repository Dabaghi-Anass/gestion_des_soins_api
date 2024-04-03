package com.fsdm.hopital.entities;

import com.fsdm.hopital.types.ActivityType;
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
public class CareActivity {
    @Id
    private Long id;
    private String title;
    private String description;
    private Date date;
    private String status;
    @Enumerated(EnumType.STRING)
    private ActivityType type;
    private String duration;
    @ManyToOne
    User assignedTo;
    @OneToOne
    Patient patient;
}
