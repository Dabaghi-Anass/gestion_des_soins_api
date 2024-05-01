package com.fsdm.hopital.entities;

import com.fsdm.hopital.types.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "UserBuilder")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseEntity{
    @Email
    @Column(unique = true)
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean isVerified;
    @OneToOne(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id" , foreignKey = @ForeignKey(name = "FK_USER_PROFILE"))
    private Profile profile;
}
