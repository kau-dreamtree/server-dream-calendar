package org.standard.dreamcalendar.domain.user;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.standard.dreamcalendar.domain.schedule.model.Schedule;
import org.standard.dreamcalendar.domain.user.type.Role;
import org.standard.dreamcalendar.model.BaseModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "USERS")
public class User extends BaseModel {

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(nullable = false)
    private String name;

    private String mailKey;

    private Boolean mailAuth;

    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(unique = true)
    private String accessToken;

    @Column(unique = true)
    private String refreshToken;

    @OneToMany(mappedBy = "user")
    private List<Schedule> schedules;

    public void addSchedule(Schedule schedule) {
        schedule.setUser(this);
        schedules.add(schedule);
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public User updateOnSocialLogIn(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + this.getId() + '\'' +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", role='" + role + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }

}