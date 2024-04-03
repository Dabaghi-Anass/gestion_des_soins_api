package com.fsdm.hopital.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "nurses")
@AllArgsConstructor
@NoArgsConstructor
public class Nurse extends User{
    private String speciality;
}
