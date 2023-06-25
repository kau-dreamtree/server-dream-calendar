package org.standard.dreamcalendar.domain.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.service.JwtProvider;
import org.standard.dreamcalendar.domain.schedule.dto.ScheduleDto;
import org.standard.dreamcalendar.domain.schedule.model.Schedule;
import org.standard.dreamcalendar.domain.user.User;
import org.standard.dreamcalendar.domain.user.UserRepository;
import org.standard.dreamcalendar.domain.user.type.TokenType;
import org.standard.dreamcalendar.service.DtoConverter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.standard.dreamcalendar.domain.user.type.TokenValidationStatus.VALID;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final JwtProvider tokenProvider;
    private final DtoConverter converter;
    private final UserRepository userRepository;

    @Transactional
    public ScheduleDto create(String accessToken, ScheduleDto scheduleDto) {

        if (
                (tokenProvider.validateToken(accessToken, TokenType.AccessToken) != VALID) ||
                (!userRepository.existsByAccessToken(accessToken))
        ) {
            // TODO: 토큰 갱신 및 유저 확인 루틴, AOP 적용
            return null;
        }

        User user = userRepository.findByAccessToken(accessToken).orElse(null);

        Schedule schedule = converter.toScheduleEntity(scheduleDto);
        user.addSchedule(schedule);
        scheduleRepository.save(schedule);

        return converter.toScheduleDto(schedule);
    }

    /**
     * accessToken으로 schedule id의 소유자 user를 검증 후 schedule을 반환합니다.
     * @param accessToken
     * @param id
     * @return found schedule entity
     */
    public ScheduleDto read(String accessToken, Long id) {

        if (
                (tokenProvider.validateToken(accessToken, TokenType.AccessToken) != VALID) ||
                (!userRepository.existsByAccessToken(accessToken))
        ) {
            // TODO: 토큰 갱신 및 유저 확인 루틴, AOP 적용
            return null;
        }

        Schedule schedule = scheduleRepository.findById(id).orElse(null);
        User scheduleOwner = schedule != null ? schedule.getUser() : null;
        User user = userRepository.findByAccessToken(accessToken).orElse(null);

        return user != null && user.equals(scheduleOwner) ? converter.toScheduleDto(schedule) : null;

    }

    public List<ScheduleDto> readAll(String accessToken) {

        if (
                (tokenProvider.validateToken(accessToken, TokenType.AccessToken) != VALID) ||
                (!userRepository.existsByAccessToken(accessToken))
        ) {
            // TODO: 토큰 갱신 및 유저 확인 루틴, AOP 적용
            return null;
        }

        User user = userRepository.findByAccessToken(accessToken).orElse(null);
        List<Schedule> schedules = user.getSchedules();

        return (schedules.isEmpty()) ?
                Collections.emptyList() :
                schedules.stream()
                        .map(converter::toScheduleDto)
                        .collect(Collectors.toList());

    }

    @Transactional
    public Boolean update(String accessToken, ScheduleDto scheduleDto) {

        if (
                (tokenProvider.validateToken(accessToken, TokenType.AccessToken) != VALID) ||
                (!userRepository.existsByAccessToken(accessToken))
        ) {
            // TODO: 토큰 갱신 및 유저 확인 루틴, AOP 적용
            return false;
        }

        scheduleRepository.updateByAllParams(
                scheduleDto.getId(), scheduleDto.getTitle(), scheduleDto.getTag(),
                scheduleDto.isAllDay(), scheduleDto.getStartAt(), scheduleDto.getEndAt()
        );

        return true;

    }

    @Transactional
    public Boolean delete(String accessToken, Long id) {

        if (
                (tokenProvider.validateToken(accessToken, TokenType.AccessToken) != VALID) ||
                (!userRepository.existsByAccessToken(accessToken))
        ) {
            // TODO: 토큰 갱신 및 유저 확인 루틴, AOP 적용
            return false;
        }

        scheduleRepository.deleteById(id);
        return true;
    }

}
