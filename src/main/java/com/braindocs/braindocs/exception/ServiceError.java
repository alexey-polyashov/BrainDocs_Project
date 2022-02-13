package com.braindocs.braindocs.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class ServiceError {

    private String message;
    private List<String> fieldsWithError;
    private Date timestamp;

    public ServiceError(String message) {
        this.message = message;
        this.timestamp = new Date();
    }
}
