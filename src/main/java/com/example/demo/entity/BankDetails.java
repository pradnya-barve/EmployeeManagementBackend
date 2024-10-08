package com.example.demo.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankDetails {
    private String bankName;

    private String branch;

    private String ifscCode;
}
