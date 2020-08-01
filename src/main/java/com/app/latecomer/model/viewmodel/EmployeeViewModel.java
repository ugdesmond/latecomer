package com.app.latecomer.model.viewmodel;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class EmployeeViewModel {
    @NotEmpty
    @NotNull
    private String name;
    @NotEmpty
    @NotNull
    private String email;
    @NotEmpty
    @NotNull
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
