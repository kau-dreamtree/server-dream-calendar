package org.standard.dreamcalendar.domain.schedule;

import org.standard.dreamcalendar.models.BaseModel;
import org.standard.dreamcalendar.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "schedules")
public class Schedule extends BaseModel {

    @ManyToOne
    private User user;

    @Column(length = 50, nullable = false)
    private String title;

    private int tag;

    @Column(nullable = false)
    private boolean isAllDay;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

}
