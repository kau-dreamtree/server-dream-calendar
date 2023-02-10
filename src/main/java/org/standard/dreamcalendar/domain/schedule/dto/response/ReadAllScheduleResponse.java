package org.standard.dreamcalendar.domain.schedule.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.standard.dreamcalendar.domain.schedule.dto.ScheduleDto;

import java.util.List;

@Data
public class ReadAllScheduleResponse {

    private String message;

    @JsonProperty("schedule_list")
    private List<ScheduleDto> scheduleDtoList;

    public ReadAllScheduleResponse(String message, List<ScheduleDto> scheduleDtoList) {
        this.message = message;
        this.scheduleDtoList = scheduleDtoList;
    }
}
