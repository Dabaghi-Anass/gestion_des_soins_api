package com.fsdm.hopital.entities;

import com.fsdm.hopital.types.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "users_profiles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Profile {
    @Id
    @Column(name = "profile_id")
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String imageUrl;
    private Gender gender;
    private Date birthDate;
}
