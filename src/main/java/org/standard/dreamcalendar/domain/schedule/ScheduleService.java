package org.standard.dreamcalendar.domain.schedule;

import lombok.RequiredArgsConstructor;
import org.apache.el.util.Validation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.domain.schedule.model.Schedule;
import org.standard.dreamcalendar.domain.schedule.model.ScheduleDto;
import org.standard.dreamcalendar.model.DtoConverter;

import javax.xml.validation.Validator;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final DtoConverter converter;
    @Transactional(rollbackFor = Exception.class)
    public boolean create(ScheduleDto scheduleDto) {
        try {
            // TODO : scheduleDto 유효성검사
            scheduleDto.setId(scheduleDto.getId());
            scheduleDto.setUserId(scheduleDto.getUserId());
            scheduleDto.setTitle(scheduleDto.getTitle());
            scheduleDto.setTag(scheduleDto.getTag());
            scheduleDto.setAllDay(scheduleDto.isAllDay());
            scheduleDto.setStartAt(scheduleDto.getStartAt());
            scheduleDto.setEndAt(scheduleDto.getEndAt());
            scheduleRepository.save(converter.toScheduleEntity(scheduleDto));
            return true;
        } catch (Exception exception) {
            // TODO : 로그 찍기
            return false;
        }
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
        List<ScheduleDto> scheduleDtoList = scheduleList.stream()
                .map(converter::toScheduleDto)
                .collect(Collectors.toList());
        return scheduleDtoList;
    }
    public boolean update(ScheduleDto scheduleDto) {
        try {
            // TODO : for문 등 반복문으로 변경
            Schedule schedule = scheduleRepository.getReferenceById(scheduleDto.getId());
//            ScheduleDto newScheduleDto = new ScheduleDto();
//            newScheduleDto.setId(schedule.getId());
//            newScheduleDto.setUserId(schedule.getUser().getId());
//            newScheduleDto.setTitle(schedule.getTitle());
//            newScheduleDto.setTag(schedule.getTag());
//            newScheduleDto.setAllDay(schedule.isAllDay());
//            newScheduleDto.setStartAt(schedule.getStartAt());
//            newScheduleDto.setEndAt(schedule.getEndAt());
//            scheduleRepository.delete(schedule);
//            return create(newScheduleDto);
            return true;
        } catch (Exception exception) {
            // TODO : 'DB에 등록된 스케줄이 없음' 응답으로 변경
            return false;
        }
    }

    public void delete() {

    }

}
