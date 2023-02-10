package org.standard.dreamcalendar.domain.schedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.standard.dreamcalendar.domain.schedule.dto.ScheduleDto;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReadScheduleResponse {

    private HttpStatus status;
    private ScheduleDto scheduleDto;

}
