package com.app.latecomer.model.viewmodel;

import javax.validation.constraints.NotNull;

public class AttendanceViewModel {

    @NotNull
    private Integer employee;

    public Integer getEmployee() {
        return employee;
    }

    public void setEmployee(Integer employee) {
        this.employee = employee;
    }
}
