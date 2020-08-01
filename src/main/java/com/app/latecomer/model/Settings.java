package com.app.latecomer.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "settings")
public class Settings implements  Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Boolean isActivated;
    private Date meetingDateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getActivated() {
        return isActivated;
    }

    public void setActivated(Boolean activated) {
        isActivated = activated;
    }

    public Date getMeetingDateTime() {
        return meetingDateTime;
    }

    public void setMeetingDateTime(Date meetingDateTime) {
        this.meetingDateTime = meetingDateTime;
    }
}
