package com.fsdm.hopital.entities;

import com.fsdm.hopital.types.AppointementStatus;
import com.fsdm.hopital.types.AppointmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private AppointementStatus status;
    @Enumerated(EnumType.STRING)
    private AppointmentType type;
    private String description;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Patient patient;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User assignedTo;
}
