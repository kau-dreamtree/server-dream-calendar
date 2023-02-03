package org.standard.dreamcalendar.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.standard.dreamcalendar.config.auth.dto.OAuthAttributes;
import org.standard.dreamcalendar.domain.schedule.model.Schedule;
import org.standard.dreamcalendar.domain.schedule.model.ScheduleDto;
import org.standard.dreamcalendar.domain.user.User;
import org.standard.dreamcalendar.domain.user.dto.UserDto;

@RequiredArgsConstructor
@Component
public class DtoConverter {

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

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .picture(user.getPicture())
                .role(user.getRole())
                .accessToken(user.getAccessToken())
                .refreshToken(user.getRefreshToken())
                .build();
    }

    public Schedule toScheduleEntity(ScheduleDto dto) {
        return Schedule.builder()
                .uuid(dto.getUuid())
                .title(dto.getTitle())
                .isAllDay(dto.isAllDay())
                .startAt(dto.getStartAt())
                .endAt(dto.getEndAt())
                .tag(dto.getTag())
                .build();
    }

    public ScheduleDto toScheduleDto(Schedule schedule) {
        return ScheduleDto.builder()
                .id(schedule.getId())
                .uuid(schedule.getUuid())
                .title(schedule.getTitle())
                .tag(schedule.getTag())
                .isAllDay(schedule.isAllDay())
                .startAt(schedule.getStartAt())
                .endAt(schedule.getEndAt())
                .build();
    }

}
