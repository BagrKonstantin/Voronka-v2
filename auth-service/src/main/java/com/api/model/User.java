package com.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(schema = "PUBLIC", name="USERS")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Integer userId;

    @NotBlank
    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EMAIL")
    @NotBlank
    private String email;

    @NotBlank
    @Column(name = "PASSWORD_HASH")
    @JsonIgnore
    private String password;

    @Column(name = "ROLE")
    private String role;
}
