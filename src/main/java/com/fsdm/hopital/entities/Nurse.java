package com.fsdm.hopital.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "nurses")
@AllArgsConstructor
@NoArgsConstructor
public class Nurse extends User{
    @Column(length = 3000)
    private String qualities;
}
