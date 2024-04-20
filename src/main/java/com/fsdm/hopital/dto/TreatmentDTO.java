package com.fsdm.hopital.dto;

import com.fsdm.hopital.types.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentDTO {
    private String response;
    private String review;
    private Status status;
    private String title;
    private Long sentToId;
    private Long sentById;
    private Long requestId;
}
