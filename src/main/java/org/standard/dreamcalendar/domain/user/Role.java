package org.standard.dreamcalendar.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    GUEST, USER, ADMIN
}
