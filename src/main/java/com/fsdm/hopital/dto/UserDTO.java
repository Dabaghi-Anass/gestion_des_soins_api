package com.fsdm.hopital.dto;

import com.fsdm.hopital.entities.Appointment;
import com.fsdm.hopital.entities.BaseEntity;
import com.fsdm.hopital.entities.Profile;
import com.fsdm.hopital.types.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends BaseEntity {
    public String username;
    public String firstName;
    public String lastName;
    public Role role;
    public Boolean isVerified;
    public Profile profile;

}
