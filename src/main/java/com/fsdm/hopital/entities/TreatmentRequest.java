package com.fsdm.hopital.entities;

import com.fsdm.hopital.types.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "treatment_requests")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.JOINED)
public class TreatmentRequest extends BaseEntity {
    @Column(length = 2000)
    private String description;
    private String title;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne
    private User sentTo;
    @ManyToOne
    private Patient sentBy;
    private boolean responded;
}
