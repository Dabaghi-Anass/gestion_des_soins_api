package com.fsdm.hopital.entities;
import com.fsdm.hopital.dto.MedicalInformation;
import jakarta.persistence.*;
        import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "patients")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient extends User{
    @Embedded
    private MedicalInformation medicalInformation;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private CareGiver careGiver;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Companion companion;
}
