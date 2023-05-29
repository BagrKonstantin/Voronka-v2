package com.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationMessage {
    Boolean isValid;
    String message;
}
