package org.standard.dreamcalendar.domain.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.standard.dreamcalendar.domain.schedule.model.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

}
