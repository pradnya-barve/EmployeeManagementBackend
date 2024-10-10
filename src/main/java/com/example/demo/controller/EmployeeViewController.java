package com.example.demo.controller;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.UserService;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeViewController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String listEmployees(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "employee/list"; // Adjust the path as needed
    }

    @GetMapping("/employee/details/{id}")
    public String showEmployeeDetails(@PathVariable("id") Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id); // Fetch the employee
        model.addAttribute("employee", employee); // Add employee to model
        return "employeeDashboard"; // Thymeleaf template name
    }
    
    @GetMapping("/add")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee/add";
    }
    
    

    @PostMapping("/add")
    public String addEmployee(@ModelAttribute Employee employee, BindingResult result, Model model) {
        // Perform validations
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "employee/add"; // Return to the form if there are validation errors
        }

        // Save the employee in the employees table (which will also handle user creation)
        employeeService.createEmployee(employee);

        // Add a success message
        model.addAttribute("successMessage", "Employee created successfully, and login credentials have been sent to their email.");
        return "redirect:/admin-dashboard"; // Redirect to the employee list after successful addition
    }

    // The saveEmployee method can also be refactored similarly
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute Employee employee, BindingResult result, Model model) {
        // Log the employee object to verify it's being received correctly
        System.out.println("Received Employee: " + employee);
        
        if (employee.getDateOfJoining() == null) {
            employee.setDateOfJoining(LocalDate.now()); // Set to current date or another default
        }

        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "employee/add"; // Return to the form if there are validation errors
        }
        
        employeeService.saveEmployee(employee);

        // Since the user creation logic is already in the createEmployee method, this can be omitted
        // If the logic for user creation needs to be maintained for editing, consider adding a similar method

        // Add a success message
        model.addAttribute("successMessage", "Employee updated successfully.");
        
        return "redirect:/admin-dashboard";
    }

    
    @GetMapping("/edit/{id}")
    public String showEditEmployeeForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "employee/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateEmployee(@PathVariable Long id, @ModelAttribute Employee employee, BindingResult result) {
        if (result.hasErrors()) {
            return "employee/edit"; // Return to the form if there are validation errors
        }
        employeeService.updateEmployee(id, employee);
        return "redirect:/admin"; // Redirect after successful update
    }

    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/admin-dashboard"; // Redirect to the employee list after deletion
    }
    
    @GetMapping("/admin-dashboard")
    public String adminDashboard(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "admin-dashboard"; // Returns admin-dashboard.html
    }
    
    @GetMapping("/download-payslip")
    public void downloadPaySlip(HttpServletResponse response) throws DocumentException, IOException {
        // Set the content type and attachment header
        response.setContentType("application/pdf");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=payslip.pdf");

        // Create a PDF document
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // Add a title to the payslip
        document.add(new Paragraph("Employee Payslip", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));
        document.add(new Paragraph(" "));
        
        // Add employee details
        document.add(new Paragraph("Employee Name: John Doe", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(new Paragraph("Employee ID: 12345", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(new Paragraph("Department: Finance", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(new Paragraph("Pay Period: " + LocalDate.now().minusMonths(6) + " to " + LocalDate.now(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(new Paragraph(" "));
        
        // Add salary breakdown
        document.add(new Paragraph("Salary Breakdown:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        document.add(new Paragraph("Basic Salary: $3000", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(new Paragraph("HRA: $500", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(new Paragraph("Other Allowances: $200", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(new Paragraph("Deductions: -$300", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(new Paragraph(" "));
        
        // Add total salary calculation
        document.add(new Paragraph("Total Salary: $3400", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        document.add(new Paragraph(" "));

        // Add bank details
        document.add(new Paragraph("Bank Details:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        document.add(new Paragraph("Bank: Bank of America", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(new Paragraph("Account Number: 123456789", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(new Paragraph("IFSC Code: BOA123456", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        
        document.add(new Paragraph(" "));

        // Add footer
        document.add(new Paragraph("Thank you for your hard work!", FontFactory.getFont(FontFactory.TIMES_ITALIC, 12)));
        
        // Close the document
        document.close();
    }


    public String generateRandomPassword() {
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

    public void sendEmailWithCredentials(String toEmail, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Employee Login Credentials");
        message.setText("Welcome to the company! Your login credentials are:\n\n" +
                        "Email: " + toEmail + "\n" +
                        "Password: " + password + "\n\n" +
                        "Please change your password after logging in.");

        mailSender.send(message);
    }
}
