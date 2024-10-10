package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmployeeService employeeService;
    
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User registerEmployee(User user, Employee employee) {
        // Save the employee first
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // Associate the employee with the user and encode the password
        user.setEmployee(savedEmployee);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user
        return userRepository.save(user);
    }

    public User registerUser(User user) {
        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
  
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
   


    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            System.out.println("User found: " + user.getEmail());
        } else {
            System.out.println("User not found for email: " + email);
        }
        return user;
    }
    
 // New method to find user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        if (user != null) {
            user.setEmail(userDetails.getEmail());
            user.setRole(userDetails.getRole());
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    public boolean checkPassword(String email, String providedPassword) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            System.out.println("User not found for email: " + email);
            return false; // User not found
        }

        // Log the stored password and the provided password
        System.out.println("Stored Password (encoded): " + user.getPassword());
        System.out.println("Provided Password: " + providedPassword);

        // Check if the provided password matches the stored password
        boolean matches = passwordEncoder.matches(providedPassword, user.getPassword());
        System.out.println("Password matches: " + matches);
        
        return matches;
    }

}