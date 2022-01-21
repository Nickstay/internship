package org.mts.internship.dto;

import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true)
public class ErrorResponse {
    private String code;
    private int status;
    private String message;
}
