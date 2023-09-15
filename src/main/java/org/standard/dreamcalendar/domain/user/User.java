package org.standard.dreamcalendar.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.standard.dreamcalendar.domain.schedule.Schedule;
import org.standard.dreamcalendar.domain.user.enums.Role;
import org.standard.dreamcalendar.global.model.BaseModel;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "users")
public class User extends BaseModel {

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(nullable = false)
    private String name;

    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Schedule> schedules;

    public void addSchedule(Schedule schedule) {
        schedule.setUser(this);
        schedules.add(schedule);
    }

    public void updatePassword(String password) {
        this.password = password;
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
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

}