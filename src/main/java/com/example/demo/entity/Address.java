package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String pinCode; // Ensure this is validated
}
