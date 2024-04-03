package com.fsdm.hopital.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "doctors")
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "uid")
public class Doctor extends User{
    @ManyToMany(mappedBy = "doctors")
    private List<Speciality> specialities;
}
