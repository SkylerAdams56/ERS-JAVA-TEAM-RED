package com.group.project.red.team.employee;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("api/employees")
public class EmployeeController {

	@Autowired
	private EmployeeRepository employeeRepo;
	
	@GetMapping("{email}/{password}")
	public ResponseEntity<Employee> getEmployeeByEmailAndPassword(@PathVariable String email, @PathVariable String password) {
		Optional<Employee> employeeByEmailAndPassword = employeeRepo.findByEmailAndPassword(email, password);
		if(employeeByEmailAndPassword.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Employee>(employeeByEmailAndPassword.get(), HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<Iterable<Employee>> getEmployees() {
		Iterable<Employee> employees = employeeRepo.findAll();
		return new ResponseEntity<Iterable<Employee>>(employees, HttpStatus.OK);
	}
	
	@GetMapping("id")
	public ResponseEntity<Employee> getEmployee(@PathVariable int id) {
		Optional<Employee> employee = employeeRepo.findById(id);
		if(employee.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Employee>(employee.get(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Employee> postEmployee(@RequestBody Employee employee) {
		Employee newEmployee = employeeRepo.save(employee);
		return new ResponseEntity<Employee>(newEmployee, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("id")
	public ResponseEntity putEmployee(@PathVariable int id, @RequestBody Employee employee) {
		if(employee.getId() != id) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		employeeRepo.save(employee);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("id")
	public ResponseEntity deleteEmployee(@PathVariable int id) {
		Optional<Employee> employeeToDelete = employeeRepo.findById(id);
		if(employeeToDelete.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		employeeRepo.delete(employeeToDelete.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
}
