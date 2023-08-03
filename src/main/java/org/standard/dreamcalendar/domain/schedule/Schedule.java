package org.standard.dreamcalendar.domain.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.standard.dreamcalendar.domain.user.User;
import org.standard.dreamcalendar.global.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
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

    public void setUser(User user) {
        this.user = user;
    }
}
