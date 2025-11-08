package com.example.rds_terraform_project.service;

import com.example.rds_terraform_project.entity.Employee;
import com.example.rds_terraform_project.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public Optional<Employee> getEmployeeById(Long empId) {
        return repository.findById(empId);
    }
}
