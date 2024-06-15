package com.tci.tciassignment.service;

import com.tci.tciassignment.exception.EmployeeException;
import com.tci.tciassignment.model.Employee;

import java.util.List;
import java.util.Map;

public interface EmployeeService {
    void addEmployee(List<Employee> employeeList) throws EmployeeException;
    Map<String, Object> getEligibleEmployees(String date);

}
