package org.statndard.dreamcalendar.service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.standard.dreamcalendar.DreamCalendarApplication;
import org.standard.dreamcalendar.config.auth.dto.OAuthAttributes;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.type.Role;
import org.standard.dreamcalendar.domain.user.User;
import org.standard.dreamcalendar.service.DtoConverter;

@SpringBootTest(classes = DreamCalendarApplication.class)
@RunWith(SpringRunner.class)
public class DtoConverterTest {

    @Autowired
    DtoConverter converter;

    @Test
    public void 변환_테스트() {

        UserDto dto = UserDto.builder()
                .id(1L)
                .email("john@test.com")
                .name("John")
                .role(Role.USER)
                .build();

        User entity = converter.toUserEntity(dto);

        UserDto dto1 = converter.toUserDto(entity);

        OAuthAttributes attributes = OAuthAttributes.builder()
                .email("john@test.com")
                .name("John")
                .build();

        User entity1 = converter.toUserEntity(attributes);

        Assertions.assertEquals(dto.getEmail(), dto1.getEmail());
        Assertions.assertEquals(entity.getEmail(), entity1.getEmail());

    }
}
