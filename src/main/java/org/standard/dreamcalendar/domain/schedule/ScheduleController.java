package org.standard.dreamcalendar.domain.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.standard.dreamcalendar.domain.schedule.model.ScheduleDto;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleDto> create(
            @RequestHeader("Authorization")  String accessToken,
            @RequestBody ScheduleDto scheduleDto
    ) {
        ScheduleDto response = scheduleService.create(accessToken, scheduleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleDto>> read(@RequestHeader("Authorization") String accessToken) {
        List<ScheduleDto> scheduleDtoList = scheduleService.findAll(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleDtoList);
    }

    @PutMapping
    public ResponseEntity<ScheduleDto> update(
            @RequestHeader("Authorization")  String accessToken,
            @RequestBody ScheduleDto scheduleDto
    ) {
        ScheduleDto response = scheduleService.update(accessToken, scheduleDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> delete(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody ScheduleDto scheduleDto
    ) {
        scheduleService.delete(accessToken, scheduleDto.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
