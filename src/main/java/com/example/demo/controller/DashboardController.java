package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeService;

@Controller
public class DashboardController {
	@Autowired
    private EmployeeService employeeService;

    @GetMapping("/admin-dashboard")
    public String adminDashboard(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        if (employees == null) {
            // Handle the case where employees are not found
            model.addAttribute("error", "No employees found.");
        } else {
            model.addAttribute("employees", employees);
        }
        return "admin-dashboard"; // Renders dashboard.html
    }

    @GetMapping("/employee-dashboard")
    public String employeeDashboard() {
        return "employee-dashboard"; // Renders the employee-dashboard.html view
    }

    @GetMapping("/dashboard")
    public String defaultDashboard() {
        return "dashboard"; // Renders the default dashboard.html view for other roles
    }
    
    @PostMapping("/add-employee")
    public String addEmployee(Employee employee) {
        employeeService.createEmployee(employee);
        return "redirect:/admin/dashboard"; // Redirect back to the dashboard
    }
}
