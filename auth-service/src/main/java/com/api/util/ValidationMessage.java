package com.api.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationMessage {
    Boolean isValid;
    String message;
}
