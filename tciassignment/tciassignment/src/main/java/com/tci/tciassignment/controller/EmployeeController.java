package com.tci.tciassignment.controller;

import com.tci.tciassignment.exception.EmployeeException;
import com.tci.tciassignment.model.Employee;
import com.tci.tciassignment.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tci")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee-bonus")
    public ResponseEntity<String> addEmployeeHandler(@RequestBody HashMap<String, List<Employee>> employeeMap) {
        try {
            List<Employee> employees = employeeMap.get("employees");
            if (employees != null) {
                employeeService.addEmployee(employees);
                return new ResponseEntity<>("Employee added successfully!", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Invalid request payload!", HttpStatus.BAD_REQUEST);
            }
        } catch (EmployeeException e) {
            return new ResponseEntity<>("Failed to add employee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/employee-bonus")
    public ResponseEntity<Map<String, Object>> getEligibleEmployeesHandler(@RequestParam("date") String dateString) {
        try {
            Map<String, Object> response = employeeService.getEligibleEmployees(dateString);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorMessage", "An unexpected error occurred");
            errorResponse.put("data", Collections.emptyList());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/greet")
    public String welcome() {
        return "Hello World! by gradle";
    }
}
