package org.standard.dreamcalendar.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.standard.dreamcalendar.config.auth.dto.OAuthAttributes;
import org.standard.dreamcalendar.domain.email.EmailAuth;
import org.standard.dreamcalendar.domain.email.EmailAuthDto;
import org.standard.dreamcalendar.domain.schedule.ScheduleRepository;
import org.standard.dreamcalendar.domain.schedule.Schedule;
import org.standard.dreamcalendar.domain.schedule.dto.ScheduleDto;
import org.standard.dreamcalendar.domain.user.User;
import org.standard.dreamcalendar.domain.user.dto.UserDto;

@Slf4j
@RequiredArgsConstructor
@Component
public class DtoConverter {

    private final ScheduleRepository scheduleRepository;

    public User toUserEntity(UserDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .name(dto.getName())
                .picture(dto.getPicture())
                .role(dto.getRole())
                .accessToken(dto.getAccessToken())
                .refreshToken(dto.getRefreshToken())
                .build();
    }

    public User toUserEntity(OAuthAttributes attributes) {
        return User.builder()
                .email(attributes.getEmail())
                .name(attributes.getName())
                .picture(attributes.getPicture())
                .build();
    }

    public UserDto toUserDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .name(entity.getName())
                .picture(entity.getPicture())
                .role(entity.getRole())
                .accessToken(entity.getAccessToken())
                .refreshToken(entity.getRefreshToken())
                .build();
    }

    public EmailAuth toEmailAuthEntity(EmailAuthDto dto) {
        return EmailAuth.builder()
                .email(dto.getEmail())
                .code(dto.getCode())
                .build();
    }

    public Schedule toScheduleEntity(ScheduleDto dto) {

        if (dto.getId() == null || !scheduleRepository.existsById(dto.getId())) {
            return Schedule.builder()
                    .title(dto.getTitle())
                    .isAllDay(dto.isAllDay())
                    .startAt(dto.getStartAt())
                    .endAt(dto.getEndAt())
                    .tag(dto.getTag())
                    .build();
        }

        scheduleRepository.updateByAllParams(
                dto.getId(), dto.getTitle(), dto.getTag(), dto.isAllDay(), dto.getStartAt(), dto.getEndAt()
        );

        return scheduleRepository.findById(dto.getId()).orElse(null);

    }

    public ScheduleDto toScheduleDto(Schedule entity) {
        return ScheduleDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .tag(entity.getTag())
                .isAllDay(entity.isAllDay())
                .startAt(entity.getStartAt())
                .endAt(entity.getEndAt())
                .build();
    }

}
