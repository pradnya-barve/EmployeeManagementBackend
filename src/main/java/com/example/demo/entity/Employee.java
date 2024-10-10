package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Personal Details
    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String dateOfBirth; // Consider using LocalDate

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private int age;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "addressLine1", column = @Column(name = "current_address_line1")),
        @AttributeOverride(name = "addressLine2", column = @Column(name = "current_address_line2")),
        @AttributeOverride(name = "city", column = @Column(name = "current_address_city")),
        @AttributeOverride(name = "pinCode", column = @Column(name = "current_address_pincode"))
    })
    private Address currentAddress;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "addressLine1", column = @Column(name = "permanent_address_line1")),
        @AttributeOverride(name = "addressLine2", column = @Column(name = "permanent_address_line2")),
        @AttributeOverride(name = "city", column = @Column(name = "permanent_address_city")),
        @AttributeOverride(name = "pinCode", column = @Column(name = "permanent_address_pincode"))
    })
    private Address permanentAddress;

    @Column(nullable = false)
    private String mobile;

    @Column(nullable = false)
    private String personalMail;

    @Column(nullable = false)
    private String emergencyContactName;

    @Column(nullable = false)
    private String emergencyContactMobile;

    // Professional Details
    @Column(unique = true, nullable = false)
    private String employmentCode;

    @Column(nullable = false)
    private String companyMail;

    private String officePhone;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "addressLine1", column = @Column(name = "office_address_line1")),
        @AttributeOverride(name = "addressLine2", column = @Column(name = "office_address_line2")),
        @AttributeOverride(name = "city", column = @Column(name = "office_address_city")),
        @AttributeOverride(name = "pinCode", column = @Column(name = "office_address_pincode"))
    })
    private Address officeAddress;

    @Column(nullable = false)
    private String reportingManager;

    @Column(nullable = false)
    private String hrName;

    @ElementCollection
    private List<EmploymentHistory> employmentHistory; // Ensure fields do not conflict

    @Column(nullable = false)
    private LocalDate dateOfJoining;

    // Project Details
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private List<Project> projects;

    // Finance
    private String panCard;

    private String aadharCard;

    @Embedded
    private BankDetails bankDetails;

    private double ctcBreakup;

    // Additional fields and methods as needed
    
    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

}