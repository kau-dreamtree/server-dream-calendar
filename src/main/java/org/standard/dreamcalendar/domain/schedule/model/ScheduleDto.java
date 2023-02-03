package org.standard.dreamcalendar.domain.schedule.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ScheduleDto {

    // TODO : 유효성 검사 추가

    private Long id;
    private String uuid;
    private String title;
    private int tag;

    @JsonProperty("is_all_day")
    private boolean isAllDay;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @JsonProperty("start_at")
    private LocalDateTime startAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @JsonProperty("end_at")
    private LocalDateTime endAt;

}
