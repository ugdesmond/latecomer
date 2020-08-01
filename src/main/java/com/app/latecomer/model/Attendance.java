package com.app.latecomer.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "attendance")
public class Attendance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @ManyToOne(targetEntity = Employee.class)
    private Employee employee;
    private Timestamp arrivalDateTime;
    @ManyToOne(targetEntity = Settings.class)
    private Settings settings;
    private Double oweAmount;

    public Double getOweAmount() {
        return oweAmount;
    }

    public Timestamp getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(Timestamp arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public void setOweAmount(Double oweAmount) {
        this.oweAmount = oweAmount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }


    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
