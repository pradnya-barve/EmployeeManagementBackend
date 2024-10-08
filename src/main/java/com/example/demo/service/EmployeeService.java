package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee createEmployee(Employee employee) {
        // Validation logic for mandatory fields
        validateEmployee(employee);
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = getEmployeeById(id);
        if (employee != null) {
            // Update fields as necessary, respecting the non-updatable fields
            employee.setFullName(employeeDetails.getFullName());
            employee.setCompanyMail(employeeDetails.getCompanyMail());
            employee.setReportingManager(employeeDetails.getReportingManager());
            employee.setDateOfBirth(employeeDetails.getDateOfBirth()); // If allowed
            employee.setGender(employeeDetails.getGender()); // If allowed
            employee.setMobile(employeeDetails.getMobile());
            employee.setPersonalMail(employeeDetails.getPersonalMail());
            employee.setEmergencyContactName(employeeDetails.getEmergencyContactName());
            employee.setEmergencyContactMobile(employeeDetails.getEmergencyContactMobile());
            // Add updates for other fields...
            return employeeRepository.save(employee);
        }
        return null;
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    private void validateEmployee(Employee employee) {
        // Add validation logic for employee fields
        // E.g., check employment code, emails, phone numbers, etc.
    }
}
