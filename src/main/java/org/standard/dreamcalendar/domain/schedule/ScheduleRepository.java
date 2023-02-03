package org.standard.dreamcalendar.domain.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.domain.schedule.model.Schedule;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Schedule s set s.uuid = ?2, s.title = ?3, s.isAllDay = ?4, s.startAt = ?5, s.endAt = ?6, s.tag = ?7 " +
            "where s.id = ?1")
    void updateByAllParams(
            @NonNull Long id, @NonNull String uuid, @NonNull String title, @NonNull boolean isAllDay,
            LocalDateTime startAt, LocalDateTime endAt, @NonNull int tag
    );

}