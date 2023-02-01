package org.standard.dreamcalendar.domain.schedule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDto {
    // TODO : 유효성 검사 추가하기

    @NotNull
    private Integer id;

    // TODO : user 정보 받아오는 방법?
    @JsonProperty("user_id")
    private Long userId;

    @NotNull
    private String title;

    private int tag;

    @NotNull
    @JsonProperty("is_all_day")
    private boolean isAllDay;

    // TODO : start_at, end_at 이미 지난 시간 유효성 검사
    @JsonProperty("start_at")
    private LocalDateTime startAt;

    @JsonProperty("end_at")
    private LocalDateTime endAt;
}
