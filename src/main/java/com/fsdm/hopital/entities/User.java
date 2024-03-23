package com.fsdm.hopital.entities;

import com.fsdm.hopital.types.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.springframework.lang.NonNullFields;

import java.io.Serializable;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable{
    @Id
    @SequenceGenerator(name = "users_seq" , sequenceName = "users_seq" , allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "users_seq")
    private Long uid;
    @Email
    @NotBlank
    private String username;
    private String firstName;
    private String lastName;
    @NotBlank
    @Size(min = 8, max = 255)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean isVerified;
    @OneToOne(cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id" , foreignKey = @ForeignKey(name = "FK_USER_PROFILE"))
    private Profile profile;
}
