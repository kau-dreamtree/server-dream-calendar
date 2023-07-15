package org.standard.dreamcalendar.domain.admin.schedule;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.standard.dreamcalendar.domain.admin.user.AdminUserService;
import org.standard.dreamcalendar.domain.schedule.dto.ScheduleDto;
import org.standard.dreamcalendar.domain.user.User;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AdminScheduleController {

    private final AdminScheduleService scheduleService;

    @Value("${spring.security.user.password}")
    private String adminAuth;

    @GetMapping("/admin/schedules")
    public ResponseEntity<ReadAllScheduleResponse> readAllSchedule(
            @RequestHeader("Authorization") String authorization,
            @RequestParam(value = "email", defaultValue = "") String email
    ) {
        if (!authorization.equals(adminAuth)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<ScheduleDto> scheduleDtoList = (email.isEmpty()) ? scheduleService.readAll() : scheduleService.readAllByEmail(email);

        String message = (scheduleDtoList.isEmpty()) ? "등록된 일정이 없습니다." : "success";

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ReadAllScheduleResponse(message, scheduleDtoList));
    }

}
