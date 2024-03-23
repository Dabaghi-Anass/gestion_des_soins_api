package com.fsdm.hopital.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
public class AppException extends Exception{
    private ProcessingException errorType;
    public AppException(String message) {
        super(message);
    }
    public AppException(ProcessingException type) {
        super(type.name().replace("_" , " ").toLowerCase());
        this.errorType = type;
    }
}
