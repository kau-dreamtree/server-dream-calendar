package org.standard.dreamcalendar.domain.schedule.dto.response;

import lombok.Data;
import org.standard.dreamcalendar.domain.schedule.dto.ScheduleDto;

@Data
public class CreateScheduleResponse {

    private String message;
    private ScheduleDto scheduleDto;

}
