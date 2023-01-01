package org.standard.dreamcalendar.domain.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.models.DtoConverter;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    DtoConverter converter;

    @Transactional(rollbackFor = Exception.class)
    public void create(ScheduleDto scheduleDto) {
        scheduleRepository.save(converter.toScheduleEntity(scheduleDto));
    }

    @Transactional(readOnly = true)
    public ScheduleDto findById(Integer id) {
        Schedule schedule = scheduleRepository.findById(id).get();
        ScheduleDto scheduleDto = converter.toScheduleDto(schedule);
        return scheduleDto;
    }

    @Transactional(readOnly = true)
    public List<ScheduleDto> findAll() {
        List<Schedule> scheduleList = scheduleRepository.findAll();
        List<ScheduleDto> scheduleDtoList = scheduleList.stream().map(converter::toScheduleDto).collect(Collectors.toList());
        return scheduleDtoList;
    }

    public void update() {

    }

    public void delete() {

    }

}
