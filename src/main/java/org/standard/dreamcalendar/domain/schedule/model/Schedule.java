package org.standard.dreamcalendar.domain.schedule.model;

import org.standard.dreamcalendar.model.BaseModel;
import org.standard.dreamcalendar.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "SCHEDULES")
public class Schedule extends BaseModel {

    @ManyToOne
    // TODO : 변경 불가능하도록 설정
    private User user;

    @Column(length = 50, nullable = false)
    private String title;

    private int tag;

    @Column(nullable = false)
    private boolean isAllDay;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

}
