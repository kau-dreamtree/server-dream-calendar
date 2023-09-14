package org.standard.dreamcalendar.domain.user.dto;

import lombok.Data;
import org.standard.dreamcalendar.domain.user.enums.TokenValidationStatus;

@Data
public class TokenValidationResult {
    private final TokenValidationStatus status;
    private final Long userId;
}
