package com.fsdm.hopital.entities;

import com.fsdm.hopital.types.Status;
import com.fsdm.hopital.types.AppointmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Date;

@Entity
@Table(name = "appointments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private AppointmentType type;
    @Column(columnDefinition = "LONGTEXT")
    private String reason;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Patient patient;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User assignedTo;
    private double duration;
    private boolean isAccepted;
}