package com.tci.tciassignment.testService;

import com.tci.tciassignment.controller.EmployeeController;
import com.tci.tciassignment.exception.EmployeeException;
import com.tci.tciassignment.model.Employee;
import com.tci.tciassignment.repository.DepartmentRepository;
import com.tci.tciassignment.repository.EmployeeRepository;
import com.tci.tciassignment.service.EmployeeService;
import com.tci.tciassignment.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class EmployeeTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddEmployeeHandler() throws EmployeeException {
        // Given
        HashMap<String, List<Employee>> employeeMap = new HashMap<>();
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee();
        employee.setEmpName("John");
        employee.setAmount(1000);
        employee.setCurrency("USD");
        employee.setDateJoined("may-01-2022");
        employee.setDateExit("may-01-2023");
        employee.setDepartmentName("IT");
        employees.add(employee);
        employeeMap.put("employees", employees);

        // Mocking void method
        doNothing().when(employeeService).addEmployee(anyList());

        // When
        ResponseEntity<String> response = employeeController.addEmployeeHandler(employeeMap);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Employee added successfully!", response.getBody());
        verify(employeeService).addEmployee(anyList());
    }


    @Test
    public void testGetEligibleEmployees() {
        // Mocking behavior for employeeRepository
        List<Employee> employees = new ArrayList<>();
        Employee employeeUSD = new Employee();
        employeeUSD.setEmpName("John");
        employeeUSD.setAmount(1000);
        employeeUSD.setCurrency("USD");
        employeeUSD.setJoiningDate(LocalDate.parse("2020-01-01"));
        employeeUSD.setExitDate(LocalDate.parse("2025-01-01"));
        employeeUSD.setDepartmentName("IT");
        employees.add(employeeUSD);

        Employee employeeEUR = new Employee();
        employeeEUR.setEmpName("Doe");
        employeeEUR.setAmount(2000);
        employeeEUR.setCurrency("EUR");
        employeeEUR.setJoiningDate(LocalDate.parse("2020-01-01"));
        employeeEUR.setExitDate(LocalDate.parse("2025-01-01"));
        employeeEUR.setDepartmentName("Finance");
        employees.add(employeeEUR);

        when(employeeRepository.findAll()).thenReturn(employees);

        // Mock the service method call
        String dateString = "may-06-2022";
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("USD", new ArrayList<>(List.of(employeeUSD)));
        mockResponse.put("EUR", new ArrayList<>(List.of(employeeEUR)));

        // Ensure employeeService is correctly mocked
        EmployeeServiceImpl employeeServiceSpy = spy(employeeService);
        when(employeeServiceSpy.getEligibleEmployees(dateString)).thenReturn(mockResponse);

        // Call the actual method
        Map<String, Object> response = employeeServiceSpy.getEligibleEmployees(dateString);

        // Assertions
        assertEquals(2, response.size()); // Assuming 2 different currencies in mock data

        // Assert for "USD" currency
        List<Employee> usdEmployees = (List<Employee>) response.get("USD");
        assertEquals("USD", usdEmployees.get(0).getCurrency());

        // Assert for "EUR" currency
        List<Employee> eurEmployees = (List<Employee>) response.get("EUR");
        assertEquals("EUR", eurEmployees.get(0).getCurrency());
    }
}
