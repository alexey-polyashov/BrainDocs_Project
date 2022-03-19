package com.braindocs.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ServiceError extends RuntimeException {

    public ServiceError(String message) {
        super(message);
    }

}
