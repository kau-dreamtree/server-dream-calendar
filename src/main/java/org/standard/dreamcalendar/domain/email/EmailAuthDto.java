package org.standard.dreamcalendar.domain.email;

import lombok.Data;

@Data
public class EmailAuthDto {
    private String email;
    private String code;
}
