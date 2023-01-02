package org.standard.dreamcalendar.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.standard.dreamcalendar.model.BaseModel;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "users")
public class User extends BaseModel {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column
    private String accessToken;

    @Column
    private String refreshToken;

    public void saveAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void saveRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + getId() + '\'' +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

}
