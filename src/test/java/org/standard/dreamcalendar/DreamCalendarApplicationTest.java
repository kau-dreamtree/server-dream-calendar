package org.standard.dreamcalendar;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.standard.dreamcalendar.domain.schedule.dto.ScheduleDto;
import org.standard.dreamcalendar.domain.user.dto.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class DreamCalendarApplicationTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    String email = "test@test.com";
    String password = "test1234";
    String name = "John";

    String title = "test title";
    int tag = 1;
    boolean isAllDay = false;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDateTime startAt = LocalDateTime.parse("2023-06-26 18:40", formatter);
    LocalDateTime endAt = LocalDateTime.parse("2023-06-26 19:40", formatter);

    long scheduleId;

    String accessToken;
    String refreshToken;

    @Order(1)
    @Test
    public void 사용자_생성() throws Exception {

        String requestBody = objectMapper.writeValueAsString(
                UserDto.builder().email(email).password(password).name(name).build()
        );

        mvc.perform(
                post("/user")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Order(2)
    @Test
    public void 사용자_로그인() throws Exception {

        String requestBody = objectMapper.writeValueAsString(
            UserDto.builder().email(email).password(password).build()
        );

        MvcResult result = mvc.perform(
                post("/auth")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        Map<String, String> responseMap = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);

        accessToken = responseMap.get("access_token");
        refreshToken = responseMap.get("refresh_token");
    }

    @Order(3)
    @Test
    public void 토큰_인증() throws Exception {
        mvc.perform(
                get("/auth")
                        .header("Authorization", "Bearer " + accessToken)
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Order(4)
    @Test
    public void 토큰_갱신() throws Exception {
        MvcResult result = mvc.perform(
                get("/auth-refresh")
                        .header("Authorization", "Bearer " + refreshToken)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        Map<String, String> responseMap = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);

        accessToken = responseMap.get("access_token");
        refreshToken = responseMap.get("refresh_token");
    }

    @Order(5)
    @Test
    public void 일정_생성() throws Exception {
        String requestBody = objectMapper.writeValueAsString(
                ScheduleDto.builder()
                        .title(title)
                        .tag(tag)
                        .isAllDay(isAllDay)
                        .startAt(startAt)
                        .endAt(endAt)
                        .build()
        );

        MvcResult result = mvc.perform(
                post("/schedule")
                        .header("Authorization", "Bearer " + accessToken)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.[?(@.id == '%s')]", 1).exists())
                .andDo(print())
                .andReturn();
    }

}