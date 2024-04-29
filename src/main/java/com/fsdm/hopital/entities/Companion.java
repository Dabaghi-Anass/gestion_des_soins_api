package com.fsdm.hopital.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "companions")
@AllArgsConstructor
@NoArgsConstructor
public class Companion extends User{
    private String relation;
}
