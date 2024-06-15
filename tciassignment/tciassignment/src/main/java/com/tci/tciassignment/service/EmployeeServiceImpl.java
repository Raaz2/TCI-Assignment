package com.tci.tciassignment.service;

import com.tci.tciassignment.exception.EmployeeException;
import com.tci.tciassignment.model.Department;
import com.tci.tciassignment.model.Employee;
import com.tci.tciassignment.repository.DepartmentRepository;
import com.tci.tciassignment.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void addEmployee(List<Employee> employeeList) throws EmployeeException {
        for (Employee emp : employeeList) {
            emp.setJoiningDate(convertDateFormat(emp.getDateJoined()));
            emp.setExitDate(convertDateFormat(emp.getDateExit()));

            if (emp.getDepartment() == null && emp.getDepartmentName() != null) {
                Department department = departmentRepository.findByDepartment(emp.getDepartmentName());
                if (department == null) {
                    department = new Department();
                    department.setDepartment(emp.getDepartmentName());
                    department = departmentRepository.save(department);
                }
                emp.setDepartment(department);
            }
        }
        employeeRepository.saveAll(employeeList);
    }

    @Override
    public Map<String, Object> getEligibleEmployees(String dateString) {
        LocalDate date = convertDateFormat(dateString);
        List<Employee> allEmployees = employeeRepository.findAll();

        List<Employee> eligibleEmployees = allEmployees.stream()
                .filter(employee -> !employee.getJoiningDate().isAfter(date) &&
                        (employee.getExitDate() == null || !employee.getExitDate().isBefore(date)))
                .sorted(Comparator.comparing(Employee::getEmpName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());

        Map<String, List<Employee>> employeesByCurrency = groupByCurrency(eligibleEmployees);

        return formatResponse(employeesByCurrency);
    }

    private Map<String, List<Employee>> groupByCurrency(List<Employee> employees) {
        Map<String, List<Employee>> employeesByCurrency = new HashMap<>();
        for (Employee emp : employees) {
            String currency = emp.getCurrency();
            employeesByCurrency.computeIfAbsent(currency, k -> new ArrayList<>()).add(emp);
        }
        return employeesByCurrency;
    }

    private Map<String, Object> formatResponse(Map<String, List<Employee>> employeesByCurrency) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> dataList = new ArrayList<>();
        response.put("errorMessage", "");
        for (Map.Entry<String, List<Employee>> entry : employeesByCurrency.entrySet()) {
            String currency = entry.getKey();
            Map<String, Object> currencyData = getStringObjectMap(entry, currency);
            dataList.add(currencyData);
        }


        response.put("data", dataList);
        return response;
    }

    private static Map<String, Object> getStringObjectMap(Map.Entry<String, List<Employee>> entry, String currency) {
        List<Employee> employees = entry.getValue();

        List<Map<String, Object>> employeeDataList = new ArrayList<>();
        for (Employee emp : employees) {
            Map<String, Object> empData = new HashMap<>();
            empData.put("empName", emp.getEmpName());
            empData.put("amount", emp.getAmount());
            employeeDataList.add(empData);
        }

        Map<String, Object> currencyData = new HashMap<>();
        currencyData.put("currency", currency);
        currencyData.put("employees", employeeDataList);
        return currencyData;
    }

    public static LocalDate convertDateFormat(String dateString) {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("MMM-dd-yyyy");
        DateTimeFormatter inputFormatter = builder.toFormatter(Locale.ENGLISH);
        return LocalDate.parse(dateString, inputFormatter);
    }
}
