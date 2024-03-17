package com.fsdm.hopital.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActionEntity {
    private String actionName;
    private String actionDescription;
    private boolean isDone;
}
