package org.standard.dreamcalendar.domain.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.domain.schedule.dto.ScheduleDto;
import org.standard.dreamcalendar.domain.user.User;
import org.standard.dreamcalendar.domain.user.UserRepository;
import org.standard.dreamcalendar.domain.user.dto.TokenValidationResult;
import org.standard.dreamcalendar.util.DtoConverter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.standard.dreamcalendar.domain.user.type.TokenValidationStatus.VALID;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final DtoConverter converter;
    private final UserRepository userRepository;

    @Transactional
    public ScheduleDto create(TokenValidationResult result, ScheduleDto scheduleDto) {

        if (result.getStatus() != VALID) {
            return null;
        }

        User user = userRepository.findById(result.getUserId()).orElse(null);

        Schedule schedule = converter.toScheduleEntity(scheduleDto);
        user.addSchedule(schedule);
        scheduleRepository.save(schedule);

        return converter.toScheduleDto(schedule);
    }

    public ScheduleDto read(TokenValidationResult result, Long id) {

        if ((result.getStatus() != VALID) || (!scheduleRepository.existsById(id))) {
            return null;
        }

        User user = userRepository.findById(result.getUserId()).orElse(null);

        Schedule schedule = scheduleRepository.findById(id).orElse(null);
        User scheduleOwner = schedule != null ? schedule.getUser() : null;

        return user != null && user.equals(scheduleOwner) ? converter.toScheduleDto(schedule) : null;

    }

    public List<ScheduleDto> readAll(TokenValidationResult result) {

        if (result.getStatus() != VALID) {
            return null;
        }

        User user = userRepository.findById(result.getUserId()).orElse(null);
        List<Schedule> schedules = user.getSchedules();

        return (schedules.isEmpty()) ?
                Collections.emptyList() :
                schedules.stream()
                        .map(converter::toScheduleDto)
                        .collect(Collectors.toList());

    }

    @Transactional
    public Boolean update(TokenValidationResult result, Long id, ScheduleDto scheduleDto) {

        if ((result.getStatus() != VALID) || (!scheduleRepository.existsById(id))) {
            return false;
        }

        scheduleRepository.updateByAllParams(
                id, scheduleDto.getTitle(), scheduleDto.getTag(),
                scheduleDto.isAllDay(), scheduleDto.getStartAt(), scheduleDto.getEndAt()
        );
        return true;

    }

    @Transactional
    public Boolean delete(TokenValidationResult result, Long id) {

        if ((result.getStatus() != VALID) || (!scheduleRepository.existsById(id))) {
            return false;
        }

        scheduleRepository.deleteById(id);
        return true;
    }

}
