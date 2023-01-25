package org.standard.dreamcalendar.domain.user.dto.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class LoginByAccessTokenResponse {

    private String message;

    public LoginByAccessTokenResponse(HttpStatus status) {

        switch (status) {

            case BAD_REQUEST:
                this.message = "잘못된 토큰입니다.";
                break;

            case UNAUTHORIZED:
                this.message = "만료된 토큰입니다. 토큰을 갱신하세요.";
                break;

            case NOT_FOUND:
                this.message = "이메일 또는 비밀번호가 틀렸습니다.";
                break;

            case CONFLICT:
                this.message = "이미 등록된 이메일입니다.";
                break;

            case ACCEPTED:
                this.message = "success";
                break;

        }

    }

}
