package com.fsdm.hopital.dto;

import lombok.Data;

@Data
public class TreatmentRequestDTO {
    private Long sentToId;
    private Long sentById;
    private String description;
    private String treatmentType;
    private String status;
    private String title;
    private boolean responded;
}
