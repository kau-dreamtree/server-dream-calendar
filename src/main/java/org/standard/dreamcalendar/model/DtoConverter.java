package org.standard.dreamcalendar.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.standard.dreamcalendar.config.auth.dto.OAuthAttributes;
import org.standard.dreamcalendar.domain.schedule.model.Schedule;
import org.standard.dreamcalendar.domain.schedule.model.ScheduleDto;
import org.standard.dreamcalendar.domain.user.model.User;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.UserRepository;

@Component
public class DtoConverter {

    @Autowired
    UserRepository userRepository;

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
        return (user == null) ? null : UserDto.builder()
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
                .user(userRepository.findById(dto.getUserId()).orElse(null))
                .title(dto.getTitle())
                .isAllDay(dto.isAllDay())
                .startAt(dto.getStartAt())
                .endAt(dto.getEndAt())
                .tag(dto.getTag())
                .build();
    }

    public ScheduleDto toScheduleDto(Schedule schedule) {
        return (schedule == null) ? null : ScheduleDto.builder()
                .userId(schedule.getUser().getId())
                .title(schedule.getTitle())
                .isAllDay(schedule.isAllDay())
                .startAt(schedule.getStartAt())
                .endAt(schedule.getEndAt())
                .tag(schedule.getTag())
                .build();
    }

}
