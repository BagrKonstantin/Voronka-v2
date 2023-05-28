package com.api.DTO;

import com.api.model.User;
import lombok.Data;

@Data
public class ValidationUser {
    ValidationMessage validationMessage;
    User user;
}
