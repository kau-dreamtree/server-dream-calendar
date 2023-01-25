package org.standard.dreamcalendar.domain.schedule;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/schedule"})
public class ScheduleController {

    @GetMapping({"/test"})
    public ResponseEntity<HttpStatus> test() {
        return ResponseEntity.ok().build();

    }
}
