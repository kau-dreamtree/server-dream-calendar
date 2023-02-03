package org.standard.dreamcalendar.domain.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.config.JwtTokenProvider;
import org.standard.dreamcalendar.domain.schedule.model.Schedule;
import org.standard.dreamcalendar.domain.schedule.model.ScheduleDto;
import org.standard.dreamcalendar.domain.user.User;
import org.standard.dreamcalendar.domain.user.UserRepository;
import org.standard.dreamcalendar.domain.user.type.TokenType;
import org.standard.dreamcalendar.domain.user.type.TokenValidationStatus;
import org.standard.dreamcalendar.model.DtoConverter;

import java.util.List;
import java.util.stream.Collectors;

import static org.standard.dreamcalendar.domain.user.type.TokenValidationStatus.VALID;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final JwtTokenProvider tokenProvider;
    private final DtoConverter converter;
    private final UserRepository userRepository;

    @Transactional
    public ScheduleDto create(String accessToken, ScheduleDto scheduleDto) {

        TokenValidationStatus status = tokenProvider.validateToken(accessToken, TokenType.AccessToken);
        if (status != VALID) {
            // TODO: 토큰 갱신 루틴, AOP 적용
            return null;
        }

        User user = userRepository.findByAccessToken(accessToken).orElse(null);
        if (user == null) {
            return null;
        }

        Schedule schedule = converter.toScheduleEntity(scheduleDto);
        user.addSchedule(schedule);
        scheduleRepository.save(schedule);

        return converter.toScheduleDto(schedule);
    }

    @Transactional
    public ScheduleDto find(String accessToken, Long id) {

        TokenValidationStatus status = tokenProvider.validateToken(accessToken, TokenType.AccessToken);
        if (status != VALID) {
            // TODO: 토큰 갱신 루틴, AOP 적용
            return null;
        }

        Schedule schedule = scheduleRepository.findById(id).orElse(null);
        if (schedule == null) {
            return null;
        }

        return converter.toScheduleDto(schedule);

    }

    @Transactional
    public List<ScheduleDto> findAll(String accessToken) {

        TokenValidationStatus status = tokenProvider.validateToken(accessToken, TokenType.AccessToken);
        if (status != VALID) {
            // TODO: 토큰 갱신 루틴, AOP 적용
            return null;
        }

        User user = userRepository.findByAccessToken(accessToken).orElse(null);
        if (user == null) {
            return null;
        }

        List<Schedule> scheduleList = user.getSchedules();

        return scheduleList.stream()
                .map(converter::toScheduleDto)
                .collect(Collectors.toList());

    }

    @Transactional
    public List<ScheduleDto> findAllAdmin() {
        return scheduleRepository.findAll().stream()
                .map(converter::toScheduleDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ScheduleDto update(String accessToken, ScheduleDto scheduleDto) {

        TokenValidationStatus status = tokenProvider.validateToken(accessToken, TokenType.AccessToken);
        if (status != VALID) {
            // TODO: 토큰 갱신 루틴, AOP 적용
            return null;
        }

        User user = userRepository.findByAccessToken(accessToken).orElse(null);
        if (user == null) {
            return null;
        }

        Schedule schedule = scheduleRepository.findByUuid(scheduleDto.getUuid()).orElse(null);
        if (schedule == null) {
            schedule = converter.toScheduleEntity(scheduleDto);
            return converter.toScheduleDto(schedule);
        }

        return converter.toScheduleDto(schedule);

    }

    @Transactional
    public void delete(String accessToken, Long id) {

        TokenValidationStatus status = tokenProvider.validateToken(accessToken, TokenType.AccessToken);
        if (status != VALID) {
            // TODO: 토큰 갱신 루틴, AOP 적용
        }

        scheduleRepository.deleteById(id);
    }

}
