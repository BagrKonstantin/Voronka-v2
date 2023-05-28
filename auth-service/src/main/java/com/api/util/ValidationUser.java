package com.api.util;

import com.api.model.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ValidationUser {
    ValidationMessage validationMessage;
    User user;
}
