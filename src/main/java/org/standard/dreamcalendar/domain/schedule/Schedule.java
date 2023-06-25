package org.standard.dreamcalendar.domain.schedule;

import org.standard.dreamcalendar.model.BaseModel;
import org.standard.dreamcalendar.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Schedule extends BaseModel {

    @ManyToOne
    private User user;

    @Column(length = 50, nullable = false)
    private String title;

    private int tag;

    @Column(nullable = false)
    private boolean isAllDay;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

}
