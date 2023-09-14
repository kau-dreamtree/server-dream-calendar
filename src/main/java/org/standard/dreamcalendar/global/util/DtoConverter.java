package org.standard.dreamcalendar.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.standard.dreamcalendar.domain.oauth2.dto.OAuthAttributes;
import org.standard.dreamcalendar.domain.schedule.Schedule;
import org.standard.dreamcalendar.domain.schedule.ScheduleRepository;
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
                .build();
    }

    public Schedule toScheduleEntity(ScheduleDto dto) {
        if (dto.getId() == null || !scheduleRepository.existsById(dto.getId())) {
            return generateScheduleEntity(dto);
        }
        return scheduleRepository.updateByAllParams(
                dto.getId(),
                dto.getTitle(),
                dto.getTag(),
                dto.isAllDay(),
                dto.getStartAt(),
                dto.getEndAt()
        ).orElse(null);
    }

    private Schedule generateScheduleEntity(ScheduleDto dto) {
        return Schedule.builder()
                .title(dto.getTitle())
                .isAllDay(dto.isAllDay())
                .startAt(dto.getStartAt())
                .endAt(dto.getEndAt())
                .tag(dto.getTag())
                .build();
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
