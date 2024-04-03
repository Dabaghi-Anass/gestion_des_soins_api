package com.fsdm.hopital.entities;
import com.fsdm.hopital.types.MedicalInformation;
import jakarta.persistence.*;
        import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "patients")
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "uid")
public class Patient extends User{
    @Embedded
    private MedicalInformation medicalInformation;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private CareGiver careGiver;
}
