package com.example.demo.controller;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login"; // This refers to login.html in src/main/resources/templates
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String providedPassword,
                        Model model) {
        // Check if the password is valid
        boolean isPasswordValid = userService.checkPassword(email, providedPassword);
        
        if (isPasswordValid) {
            // Password is correct, proceed with login (e.g., set user in session, redirect to dashboard)
            return "redirect:/employee-dashboard"; // Change to your dashboard path
        } else {
            // Password is incorrect, show an error message
            model.addAttribute("errorMessage", "Invalid email or password");
            return "login"; // Return to the login page
        }
    }
}
