package com.api.dto;

import com.api.model.User;
import lombok.Data;

@Data
public class ValidationUser {
    ValidationResponse validationResponse;
    User user;
}
