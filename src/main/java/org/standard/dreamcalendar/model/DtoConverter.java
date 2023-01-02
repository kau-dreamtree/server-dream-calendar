package org.standard.dreamcalendar.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.standard.dreamcalendar.domain.schedule.Schedule;
import org.standard.dreamcalendar.domain.schedule.ScheduleDto;
import org.standard.dreamcalendar.domain.user.User;
import org.standard.dreamcalendar.domain.user.UserDto;
import org.standard.dreamcalendar.domain.user.UserRepository;

@Component
public class DtoConverter {

    @Autowired
    UserRepository userRepository;

    public User toUserEntity(UserDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .username(dto.getUsername())
                .build();
    }

    public UserDto toUserDto(User user) {
        return (user == null) ? null : UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .username(user.getUsername())
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
