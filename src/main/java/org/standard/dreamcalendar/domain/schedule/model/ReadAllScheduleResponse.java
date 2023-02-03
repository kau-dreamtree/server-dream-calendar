package org.standard.dreamcalendar.domain.schedule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ReadAllScheduleResponse {

    private String message;

    @JsonProperty("schedule_list")
    private List<ScheduleDto> scheduleDtoList;

    public ReadAllScheduleResponse(String message) {
        this.message = message;
    }

    public ReadAllScheduleResponse(String message, List<ScheduleDto> scheduleDtoList) {
        this.message = message;
        this.scheduleDtoList = scheduleDtoList;
    }
}
