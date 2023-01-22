package org.standard.dreamcalendar.domain.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.standard.dreamcalendar.domain.schedule.model.ScheduleDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody ScheduleDto scheduleDto) {
        scheduleService.create(scheduleDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<ScheduleDto>> findAll() {
        List<ScheduleDto> scheduleDtoList = scheduleService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(scheduleDtoList);
    }

}
