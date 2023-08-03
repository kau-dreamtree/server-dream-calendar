package org.standard.dreamcalendar.domain.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.standard.dreamcalendar.domain.auth.AccessToken;
import org.standard.dreamcalendar.domain.schedule.dto.ScheduleDto;
import org.standard.dreamcalendar.domain.user.dto.TokenValidationResult;

import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/schedule")
    public ResponseEntity<ScheduleDto> create(
            @AccessToken TokenValidationResult result,
            @RequestBody ScheduleDto scheduleDto
    ) {
        ScheduleDto dto = scheduleService.create(result, scheduleDto);
        return ResponseEntity.created(URI.create("schedule/" + dto.getId())).body(dto);
    }

    @GetMapping("/schedule/{id}")
    public ResponseEntity<ScheduleDto> read(
            @PathVariable Long id, @AccessToken TokenValidationResult result
    ) {
        ScheduleDto scheduleDto = scheduleService.read(result, id);
        return (scheduleDto != null) ?
                ResponseEntity.status(HttpStatus.OK).body(scheduleDto) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/schedules")
    public ResponseEntity<List> readAll(@AccessToken TokenValidationResult result) {
        List<ScheduleDto> scheduleDtoList = scheduleService.readAll(result);
        return (scheduleDtoList != null) ?
                ResponseEntity.status(HttpStatus.OK).body(scheduleDtoList) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/schedule/{id}")
    public ResponseEntity<HttpStatus> update(
            @PathVariable Long id,
            @AccessToken TokenValidationResult result,
            @RequestBody ScheduleDto scheduleDto
    ) {
        return (scheduleService.update(result, id, scheduleDto)) ?
                ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/schedule/{id}")
    public ResponseEntity<HttpStatus> delete(
            @PathVariable Long id,
            @AccessToken TokenValidationResult result
    ) {
        return (scheduleService.delete(result, id)) ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
