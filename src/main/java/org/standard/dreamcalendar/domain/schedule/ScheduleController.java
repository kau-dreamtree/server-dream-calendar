package org.standard.dreamcalendar.domain.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

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
