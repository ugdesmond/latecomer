package com.app.latecomer.model.viewmodel;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class SettingsViewModel {

    @NotNull
    private Date meetingDateTime;

    public Date getMeetingDateTime() {
        return meetingDateTime;
    }

    public void setMeetingDateTime(Date meetingDateTime) {
        this.meetingDateTime = meetingDateTime;
    }


}
