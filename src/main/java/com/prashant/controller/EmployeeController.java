package com.prashant.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prashant.entity.Employee;
import com.prashant.repository.EmployeeRepository;

@RestController
@RequestMapping("/api")
public class EmployeeController {
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@GetMapping("/all")
	public ResponseEntity<List<Employee>>findAll(){
		List<Employee>lists = employeeRepository.findAll();
		return new ResponseEntity<List<Employee>>(lists,HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/save")
	public void save(@RequestBody Employee employee) {
		employeeRepository.save(employee);
	}
	
	@GetMapping("/{name}")
	public ResponseEntity<Employee>fnidByName(@PathVariable String name){
		Optional<Employee>employee = employeeRepository.findByFirstName(name);
		return new ResponseEntity<Employee>(employee.get(),HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public void Update(@PathVariable Long id,@RequestBody Employee empl){
		Employee emp = employeeRepository.findById(id).get();
		emp.setFirstName(empl.getFirstName());
		emp.setLastName(empl.getLastName());
		employeeRepository.save(emp);
	}

}
