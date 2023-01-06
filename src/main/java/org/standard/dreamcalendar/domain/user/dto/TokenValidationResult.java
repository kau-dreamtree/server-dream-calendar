package org.standard.dreamcalendar.domain.user.dto;

import lombok.Data;
import org.standard.dreamcalendar.config.type.TokenValidationType;

@Data
public class TokenValidationResult {
    private final TokenValidationType type;
    private final String email;
}
