package com.group.project.red.team.expense;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.group.project.red.team.employee.Employee;

public interface ExpenseRepository extends CrudRepository<Expense,Integer>{
	Iterable<Expense> findByStatus(String status);

	Iterable<Expense> findAll();

	Optional<Expense> findById(int id);

	Iterable<Expense> findAllByStatus(String status_Paid);
	
	Iterable<Expense> findByEmployeeId(int employeeId);
	
}
