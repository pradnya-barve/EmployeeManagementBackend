package com.example.demo.repository;

import com.example.demo.entity.Employee;
import com.example.demo.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	Optional<Employee> findByEmploymentCode(String employmentCode); // Custom method to find employee by employment code
}