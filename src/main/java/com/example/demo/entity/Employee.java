package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String employmentCode; // Unique employment code

    @Column(nullable = false)
    private String name; // Employee name

    @Column(unique = true, nullable = false)
    private String companyEmail; // Company email

    private String managerName; // Name of the employee's manager

    private String currentProject; // Current project name

    // Add any other necessary fields
}
