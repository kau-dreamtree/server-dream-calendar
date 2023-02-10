package org.standard.dreamcalendar.domain.user.dto.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class LogInByAccessTokenResponse {

    private String message;
    private HttpStatus status;

    public LogInByAccessTokenResponse(HttpStatus status) {

        this.status = status;

        switch (status) {

            case BAD_REQUEST:
                this.message = "DB에 존재하지 않는 토큰입니다.";

                break;

            case UNAUTHORIZED:
                this.message = "만료된 토큰입니다. 토큰을 갱신하세요.";
                break;

            case OK:
                this.message = "success";
                break;

        }

    }

}
