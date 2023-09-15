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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 테스트 순서 <br>
 * 1. 회원가입 <br>
 * 2. 이메일, 비밀번호 로그인 <br>
 * 3. 토큰 인증 <br>
 * 4. 토큰 갱신 <br>
 * 5. 일정 생성 <br>
 * 6. 일정 조회 <br>
 * 7. 일정 수정 <br>
 * 8. 일정 삭제 <br>
 */

@SuppressWarnings("ALL")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class DreamCalendarApplicationTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    UserDto userDto = UserDto.builder()
            .email("john@test.com")
            .password("test1234")
            .name("John")
            .build();

    ScheduleDto scheduleDto = ScheduleDto.builder()
            .title("test title")
            .tag(1)
            .isAllDay(false)
            .startAt(LocalDateTime.now())
            .endAt(LocalDateTime.now())
            .build();

    String accessToken;
    String refreshToken;

    Long scheduleId;

    @Order(1)
    @Test
    void 회원가입() throws Exception {

        String requestBody = objectMapper.writeValueAsString(userDto);

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
    void 이메일_로그인() throws Exception {

        String requestBody = objectMapper.writeValueAsString(userDto);

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

        System.out.println(accessToken);
        System.out.println(refreshToken);
    }

    @Order(3)
    @Test
    void 토큰_인증() throws Exception {
        mvc.perform(
                get("/auth")
                        .header("Authorization", "Bearer " + accessToken)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Order(4)
    @Test
    void 토큰_갱신() throws Exception {

        Thread.sleep(1000);

        MvcResult result = mvc.perform(
                get("/auth-refresh")
                        .header("Authorization", "Bearer " + refreshToken)
                )
                .andExpect(status().isOk())
                .andReturn();

        Map<String, String> responseMap = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);

        assertNotEquals(accessToken, responseMap.get("access_token"));
        assertNotEquals(refreshToken, responseMap.get("refresh_token"));

        accessToken = responseMap.get("access_token");
        refreshToken = responseMap.get("refresh_token");
    }

    @Order(5)
    @Test
    void 일정_생성() throws Exception {

        String requestBody = objectMapper.writeValueAsString(scheduleDto);

        MvcResult result = mvc.perform(
                post("/schedule")
                        .header("Authorization", "Bearer " + accessToken)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.[?(@.id == '%s')]", 1).exists())
                .andReturn();

        Map<String, Integer> responseMap = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);

        scheduleId = Long.valueOf(responseMap.get("id")) ;

        assertEquals(1L, scheduleId);
    }

    @Order(6)
    @Test
    void 일정_조회() throws Exception {

        MvcResult result = mvc.perform(
                        get("/schedule/" + scheduleId.toString())
                                .header("Authorization", "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        Map<String, Object> responseMap = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);

        assertEquals("test title", (String) responseMap.get("title"));
        assertEquals(1, (Integer) responseMap.get("tag"));
        assertEquals(false, (Boolean) responseMap.get("is_all_day"));
    }

    @Order(7)
    @Test
    void 일정_수정() {

    }

    @Order(8)
    @Test
    void 일정_삭제() {

    }
}