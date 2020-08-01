package com.app.latecomer.controllers;


import com.app.latecomer.businesslogic.EmployeeLogic;
import com.app.latecomer.model.Employee;
import com.app.latecomer.model.MessageResponse;
import com.app.latecomer.model.viewmodel.EmployeeViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    EmployeeLogic employeeLogic;

    public EmployeeController(EmployeeLogic employeeLogic) {
        this.employeeLogic = employeeLogic;
    }
    /**
     * This controller creates employee
     *
     * @param employee  Request  body {@link Employee} class
     * @return A {@link ResponseEntity<>} containing the {@link Employee} .
     */
    @Transactional
    @RequestMapping(value = "/employees", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse<EmployeeViewModel>> createEmployee(@Valid @RequestBody EmployeeViewModel employee) throws Exception {
        MessageResponse<EmployeeViewModel> messageResponse;
        try {
            List<Employee> employeeList = employeeLogic.getByColumnName("email",employee.getEmail());
            if(!employeeList.isEmpty()){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Employee already exist!");
            }
            Employee employeeDetail = new Employee();
            employeeDetail.setAddress(employee.getAddress());
            employeeDetail.setEmail(employee.getEmail());
            employeeDetail.setName(employee.getName());
            employeeLogic.create(employeeDetail);
            messageResponse = new MessageResponse<>();
            messageResponse.setMessage("Employee created successfully");
            messageResponse.setData(employee);
            messageResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }

    }

    /**
     * This controller get employee by i.d
     *
     * @param id Request  body {@link Integer} class
     * @return A {@link ResponseEntity<>} containing the {@link Employee} .
     */
    @RequestMapping(value = "/employees/{id}", method = RequestMethod.GET)
    public ResponseEntity<MessageResponse<Employee>> getEmployees(@PathVariable Integer id) throws Exception {
        MessageResponse<Employee> messageResponse;
        try {
            Employee employee = employeeLogic.findOne(id);
            if(employee ==null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee does not exist!");
            }
            messageResponse = new MessageResponse<>();
            messageResponse.setMessage("Employee retrieved successfully");
            messageResponse.setData(employee);
            messageResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }

    }
}