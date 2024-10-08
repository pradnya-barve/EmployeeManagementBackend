package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectCode;
    
    private String projectName;

    private String startDate; // Consider using LocalDate

    private String endDate; // Consider using LocalDate

    private String clientName;

    private String reportingManager; // Employee code/mail
}
