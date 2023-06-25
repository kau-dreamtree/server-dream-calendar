package org.standard.dreamcalendar.domain.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.standard.dreamcalendar.domain.schedule.dto.ScheduleDto;
import org.standard.dreamcalendar.domain.schedule.model.Schedule;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/schedule")
    public ResponseEntity<ScheduleDto> create(
            @RequestHeader("Authorization")  String accessToken,
            @RequestBody ScheduleDto scheduleDto
    ) {
        ScheduleDto result = scheduleService.create(accessToken, scheduleDto);
        return (result != null) ?
                ResponseEntity.status(HttpStatus.CREATED).body(result) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/schedule/{id}")
    public ResponseEntity<ScheduleDto> read(
            @PathVariable Long id ,@RequestHeader("Authorization") String accessToken
    ) {
        ScheduleDto scheduleDto = scheduleService.read(accessToken, id);
        return (scheduleDto != null) ?
                ResponseEntity.status(HttpStatus.OK).body(scheduleDto) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/schedules")
    public ResponseEntity<List> readAll(@RequestHeader("Authorization") String accessToken) {
        List<ScheduleDto> scheduleDtoList = scheduleService.readAll(accessToken);
        return (scheduleDtoList != null) ?
                ResponseEntity.status(HttpStatus.OK).body(scheduleDtoList) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/schedule/{id}")
    public ResponseEntity<HttpStatus> update(
            @PathVariable Long id,
            @RequestHeader("Authorization") String accessToken,
            @RequestBody ScheduleDto scheduleDto
    ) {
        return (scheduleService.update(accessToken, id, scheduleDto)) ?
                ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/schedule/{id}")
    public ResponseEntity<HttpStatus> delete(
            @PathVariable Long id,
            @RequestHeader("Authorization") String accessToken
    ) {
        return (scheduleService.delete(accessToken, id)) ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
