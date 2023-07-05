package org.standard.dreamcalendar.domain.admin.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.standard.dreamcalendar.domain.schedule.Schedule;
import org.standard.dreamcalendar.domain.schedule.ScheduleRepository;
import org.standard.dreamcalendar.domain.schedule.dto.ScheduleDto;
import org.standard.dreamcalendar.domain.user.User;
import org.standard.dreamcalendar.domain.user.UserRepository;
import org.standard.dreamcalendar.util.DtoConverter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminScheduleService {

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final DtoConverter converter;

    public List<ScheduleDto> readAll() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return (schedules.isEmpty()) ?
                Collections.emptyList() :
                schedules.stream()
                        .map(converter::toScheduleDto)
                        .collect(Collectors.toList());
    }

    public List<ScheduleDto> readAllByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        List<Schedule> schedules = (user != null) ? user.getSchedules() : Collections.emptyList();
        return (schedules.isEmpty()) ?
                Collections.emptyList() :
                schedules.stream()
                        .map(converter::toScheduleDto)
                        .collect(Collectors.toList());
    }
}
