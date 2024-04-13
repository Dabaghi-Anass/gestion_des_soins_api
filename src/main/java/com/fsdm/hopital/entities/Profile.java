package com.fsdm.hopital.entities;

import com.fsdm.hopital.types.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "profiles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Profile extends BaseEntity{
    private String address;
    @Column(length = 1000)
    private String imageUrl;
    private Gender gender;
    @Temporal(TemporalType.DATE)
    private Date birthDate;
    private String phoneNumber;
}
