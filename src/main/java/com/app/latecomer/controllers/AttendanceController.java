package com.app.latecomer.controllers;

import com.app.latecomer.businesslogic.AttendanceLogic;
import com.app.latecomer.businesslogic.EmployeeLogic;
import com.app.latecomer.businesslogic.SettingsLogic;
import com.app.latecomer.model.Attendance;
import com.app.latecomer.model.Employee;
import com.app.latecomer.model.MessageResponse;
import com.app.latecomer.model.Settings;
import com.app.latecomer.model.viewmodel.AttendanceViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AttendanceController {
    AttendanceLogic attendanceLogic;
    SettingsLogic settingsLogic;
    EmployeeLogic employeeLogic;
    private static Integer pageNumVal = 1;
    private static Integer pageSizeVal = 10;

    public AttendanceController(AttendanceLogic attendanceLogic, SettingsLogic settingsLogic, EmployeeLogic employeeLogic) {
        this.attendanceLogic = attendanceLogic;
        this.settingsLogic = settingsLogic;
        this.employeeLogic = employeeLogic;
    }
    /**
     * This controller creates employee attendance
     *
     * @param attendanceViewModel Request  body {@link AttendanceViewModel} class
     * @return A {@link ResponseEntity<>} containing the {@link Attendance} .
     */
    @Transactional
    @RequestMapping(value = "/attendance", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse<Attendance>> createAttendance(@Valid @RequestBody AttendanceViewModel attendanceViewModel) throws Exception {
        MessageResponse<Attendance> messageResponse;
        try {
            Employee employee = employeeLogic.findOne(attendanceViewModel.getEmployee());
            if (employee == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee detail  not found!");

            }
            Attendance attendance = new Attendance();
            attendance.setEmployee(employee);
            attendance.setArrivalDateTime(new Timestamp(new Date().getTime()));
            List<Settings> settings = settingsLogic.getByColumnName("isActivated", true);
            if (settings.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one meeting datetime  settings should be activate!");
            }
            Settings settingsObject = settings.get(0);
            List<Attendance> attendances = attendanceLogic.getByColumnName("employee", employee);
            if (!attendances.isEmpty()) {
                if (attendances.get(0).getSettings().getId().equals(settingsObject.getId()))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate employee attendance exist!");

            }
            attendance.setSettings(settingsObject);
            attendance.setOweAmount(attendanceLogic.calculateAmountOwing(settingsObject, attendance.getArrivalDateTime()));
            attendanceLogic.create(attendance);

            attendance.setEmployee(employee);
            messageResponse = new MessageResponse<>();
            messageResponse.setMessage("Employee attendance created successfully");
            messageResponse.setData(attendance);
            messageResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    /**
     * This controller retrieves attendances in descending order of the amount owe
     *
     * @param pageNum Request  body {@link Integer} class
     * @param pageSize Request  body {@link Integer} class
     * @return A {@link ResponseEntity<>} containing the {@link Attendance} .
     */
    @RequestMapping(value = "/attendance", method = RequestMethod.GET)
    public ResponseEntity<MessageResponse<List<Attendance>>> getAttendance(@RequestParam Integer pageNum, @RequestParam Integer pageSize) throws Exception {
        MessageResponse<List<Attendance>> messageResponse;
        try {
            if (pageNum != null)
                pageNum = pageNum > 0 ? pageNum-1 : pageNum;
            List<Attendance> attendances = attendanceLogic.findPaginated(pageNum == null ? pageNumVal : pageNum, pageSize == null ? pageSizeVal : pageSize);
            messageResponse = new MessageResponse<>();
            messageResponse.setMessage("Attendance  retrieved successfully");
            messageResponse.setData(attendances);
            messageResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }

    }

    /**
     * This controller retrieves employee attendances using email,name and address
     *
     * @param searchVal Request  body {@link String} class
     * @return A {@link ResponseEntity<>} containing the {@link Attendance} .
     */
    @RequestMapping(value = "/attendances/search", method = RequestMethod.GET)
    public ResponseEntity<MessageResponse<List<Attendance>>> searchAttendance(@RequestParam String searchVal) throws Exception {
        MessageResponse<List<Attendance>> messageResponse;
        try {
            List<Attendance> attendances=attendanceLogic.searchAttendance(searchVal);
            messageResponse = new MessageResponse<>();
            messageResponse.setMessage("Attendance  retrieved successfully");
            messageResponse.setData(attendances);
            messageResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }

    }
}