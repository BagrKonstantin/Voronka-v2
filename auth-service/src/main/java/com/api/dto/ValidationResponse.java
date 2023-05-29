package com.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationResponse {
    Boolean isValid;
    String message;
}
