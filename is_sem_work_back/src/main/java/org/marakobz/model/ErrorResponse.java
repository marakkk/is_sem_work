package org.marakobz.model;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    private String field;
    private String errorCode;

}

