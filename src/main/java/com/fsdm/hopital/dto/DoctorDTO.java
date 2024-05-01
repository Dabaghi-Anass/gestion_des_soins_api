package com.fsdm.hopital.dto;

import com.fsdm.hopital.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDTO extends UserDTO {
    List<String> specialities;
}
