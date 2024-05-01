package com.fsdm.hopital.dto;

import com.fsdm.hopital.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NurseDTO extends BaseEntity {
    List<String> qualities;
}
