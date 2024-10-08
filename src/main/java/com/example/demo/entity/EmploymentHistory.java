package com.example.demo.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentHistory {
    private String companyName;

    private String joiningDate;

    private String endDate;
}
