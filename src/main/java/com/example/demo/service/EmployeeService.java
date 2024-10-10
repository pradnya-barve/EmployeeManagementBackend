package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import java.security.SecureRandom;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JavaMailSender mailSender;
    
    public Employee createEmployee(Employee employee) {
        // Validate the employee fields
        validateEmployee(employee);

        // Save employee in the Employee table
        Employee savedEmployee = employeeRepository.save(employee);

        // Generate a random password for the new user
        String randomPassword = generateRandomPassword();

        // Create and save the user for the employee
        createUserForEmployee(employee, randomPassword);

        // Send email with login credentials (add this here to keep the password flow consistent)
        sendEmailWithCredentials(employee.getCompanyMail(), randomPassword);

        // Return saved employee
        return savedEmployee;
    }

    private void createUserForEmployee(Employee employee, String password) {
        User user = new User();
        user.setEmail(employee.getCompanyMail());
        String encodedPassword = passwordEncoder.encode(password); // Encode the password
        user.setPassword(encodedPassword); // Save the encoded password
        user.setRole(Role.EMPLOYEE); // Set role to EMPLOYEE
        userService.save(user);
        
     // Log the encoded password for debugging
        System.out.println("Encoded Password for user " + employee.getCompanyMail() + ": " + encodedPassword);
    }

    // Generate a random password
    private String generateRandomPassword() {
        int length = 10;
        String passwordChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(passwordChars.length());
            password.append(passwordChars.charAt(index));
        }

        return password.toString();
    }
    
    private void sendEmailWithCredentials(String toEmail, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Employee Login Credentials");
        message.setText("Welcome to the company! Your login credentials are:\n\n" +
                        
                        "Password: " + password + "\n\n" +
                        "Please change your password after logging in.");
        mailSender.send(message);
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