package org.standard.dreamcalendar.domain.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.standard.dreamcalendar.domain.schedule.model.ScheduleDto;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/scheduleTest")
    public ResponseEntity<HttpStatus> scheduleTest() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/createSchedule")
    public ResponseEntity<HttpStatus> createSchedule(@RequestBody ScheduleDto schedule) {
        if (scheduleService.create(schedule)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @PostMapping("/updateSchedule")
    public ResponseEntity<HttpStatus> updateSchedule(@RequestBody ScheduleDto schedule) {
        if (scheduleService.update(schedule)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.unprocessableEntity().build();
    }

//    @PostMapping("/findSchedule")
//    public ResponseEntity<HttpStatus> createSchedule(@RequestBody ScheduleDto schedule) {
//        if (scheduleService.create(schedule)) {
//            return ResponseEntity.status(HttpStatus.CREATED).build();
//        }
//        return ResponseEntity.unprocessableEntity().build();
//    }
}
