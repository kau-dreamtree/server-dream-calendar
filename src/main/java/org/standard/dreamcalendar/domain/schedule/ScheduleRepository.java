package org.standard.dreamcalendar.domain.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Schedule s set s.title = ?2, s.tag = ?3, s.isAllDay = ?4, s.startAt = ?5, s.endAt = ?6 " +
            "where s.id = ?1")
    void updateByAllParams(
            @NonNull Long id, @NonNull String title, @NonNull int tag,
            @NonNull boolean isAllDay, @NonNull LocalDateTime startAt, @NonNull LocalDateTime endAt
    );

}