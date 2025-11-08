package com.example.rds_terraform_project.repository;

import com.example.rds_terraform_project.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}

