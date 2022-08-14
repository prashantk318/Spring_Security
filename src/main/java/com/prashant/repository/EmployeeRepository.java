package com.prashant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prashant.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
Optional<Employee> findByFirstName(String firstName);
}
