package org.standard.dreamcalendar.domain.email;

import lombok.*;
import org.standard.dreamcalendar.global.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "EMAIL_AUTH")
@Entity
public class EmailAuth extends BaseModel {

    @Column(nullable = false, unique = true)
    private String email;
    private String code;

}
