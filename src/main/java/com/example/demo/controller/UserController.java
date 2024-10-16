package com.example.demo.controller;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        System.out.println("Incoming registration request: " + user);  // Add logging here
        User createdUser = userService.registerUser(user);
        return ResponseEntity.ok(createdUser);
    }
    
    @PostMapping
    public ResponseEntity<User> addEmployee(@RequestBody Employee employee, @RequestParam String email, @RequestParam String password) {
        // Create a new user with role 'EMPLOYEE'
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(Role.EMPLOYEE);


        // Register employee and user
        User createdUser = userService.registerEmployee(user, employee);

        return ResponseEntity.ok(createdUser);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
    	System.out.println("Updating user with ID: " + id + " with details: " + userDetails);
        User updatedUser = userService.updateUser(id, userDetails);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}