package com.api.dto;

import com.api.util.EStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    @JsonIgnore
    private EStatus status;
    private String message;

    public ResponseEntity<Response> wrap() {
        if (status == EStatus.OK) {
            return ResponseEntity.ok(this);
        }
        return ResponseEntity.badRequest().body(this);
    }
}
