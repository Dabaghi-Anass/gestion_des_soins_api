package com.fsdm.hopital.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "specialities")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Speciality extends BaseEntity {
    private String category;
}
