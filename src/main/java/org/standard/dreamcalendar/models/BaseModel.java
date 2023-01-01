package org.standard.dreamcalendar.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseModel implements Comparable<BaseModel> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Column
    @JsonProperty("modified_at")
    private LocalDateTime modifiedAt;

    @Column
    @JsonProperty("deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    public void prePersist() {
        createdAt = modifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        modifiedAt = LocalDateTime.now();
    }

    @Override
    public int compareTo(BaseModel o) {
        return this.getId().compareTo(o.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        return this.getId().equals(((BaseModel) obj).getId());
    }

}
