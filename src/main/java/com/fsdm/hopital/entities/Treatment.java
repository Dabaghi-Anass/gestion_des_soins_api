package com.fsdm.hopital.entities;

import com.fsdm.hopital.types.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="treatments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Treatment extends BaseEntity{
    @Column(columnDefinition = "LONGTEXT")
    private String response;
    @Column(length = 1000)
    private String review;
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private Status status;
    private String title;
    @ManyToOne
    private Patient sentTo;
    @ManyToOne
    private User sentBy;
    @OneToOne(cascade = CascadeType.ALL)
    private TreatmentRequest request;
}



