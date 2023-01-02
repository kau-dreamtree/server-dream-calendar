package org.standard.dreamcalendar.config;

import lombok.Data;

@Data
public class TokenValidationResult {

    private final TokenValidationType type;
    private final Integer userId;

}
