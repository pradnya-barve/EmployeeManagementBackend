package com.example.demo.controller;

import com.example.demo.entity.Employee;
import com.example.demo.entity.User;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeViewController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public String listEmployees(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "employee/list"; // Adjust the path as needed
    }

    @GetMapping("/add")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee/add";
    }
    
    @PostMapping("/add")
    public String addEmployee(@ModelAttribute Employee employee, BindingResult result) {
        // Perform validations here
    	if (result.hasErrors()) {
            return "employee/add"; // Return to the form if there are validation errors
        }
        employeeService.createEmployee(employee);
        return "redirect:/employees";
    }
    
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute Employee employee) {
        // Ensure dateOfJoining is set
        if (employee.getDateOfJoining() == null) {
            // handle the error, e.g., return to the form with an error message
            return "redirect:/employees"; // or whatever your form path is
        }
        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }

    
    @GetMapping("/edit/{id}")
    public String showEditEmployeeForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "employee/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateEmployee(@PathVariable Long id, @ModelAttribute Employee employee) {
        // Perform updates
        employeeService.updateEmployee(id, employee);
        return "redirect:/employees";
    }

    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees";
    }
}
