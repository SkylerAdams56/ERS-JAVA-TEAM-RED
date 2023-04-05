package com.group.project.red.team.expense;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.group.project.red.team.employee.*;


@CrossOrigin
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

	private final String Status_New = "NEW";
	private final String Status_Review = "REVIEW";
	private final String Status_Approved = "APPROVED";
	private final String Status_Rejected = "REJECTED";
	private final String Status_Paid = "PAID";
	private final String Status_Due = "DUE";
	
	@Autowired
	private ExpenseRepository expRepo;
	private EmployeeRepository empRepo;
	
	//UpdateEmployeeExpensesDueAndPaid(employeeId)
	private boolean UpdateEmployeeExpensesDueAndPaid(int employeeId) {
		Optional<Expense> putEmployeeExpensesDueAndPaid = expRepo.findById(employeeId);
		if(putEmployeeExpensesDueAndPaid.isEmpty()) {
			return false;
		}
		Optional<Employee> employee = empRepo.findById(employeeId);
		Iterable<Expense> expenseByEmpId = expRepo.findByEmployeeId(employeeId);
		double total;
		for(Expense ex: expenseByEmpId) {
			if(ex.getStatus() != Status_Paid) {
				employee.get().setExpensesDue(ex.getTotal() + employee.get().getExpensesDue());
			}
			if(ex.getStatus() == Status_Paid) {
				employee.get().setExpensesPaid(ex.getTotal() + employee.get().getExpensesPaid());
			}
		}
		return true;
	}
	
	@GetMapping("reviews")
	public ResponseEntity<Iterable<Expense>> GetExpensesInReview(){
		Iterable<Expense> ExpensesInReview = expRepo.findByStatus(Status_Review);
		return new ResponseEntity<Iterable<Expense>>(ExpensesInReview, HttpStatus.OK);
	}
	
	//GetApprovedExpenses
	@GetMapping("approved")
	public ResponseEntity<Iterable<Expense>> GetApprovedExpenses(){
		Iterable<Expense> ApprovedExpenses = expRepo.findByStatus(Status_Approved);
		return new ResponseEntity<Iterable<Expense>>(ApprovedExpenses, HttpStatus.OK);
	}
	
	
	
	@GetMapping
	public ResponseEntity<Iterable<Expense>> getExpenses(){
		Iterable<Expense> Expenses = expRepo.findAll();
		return new ResponseEntity<Iterable<Expense>>(Expenses, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Expense> getExpense(@PathVariable int id){
		Optional<Expense> Expense = expRepo.findById(id);
		if(Expense.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Expense>(Expense.get(), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Expense> postExpense(@RequestBody Expense Expense){
		Expense newExpense = expRepo.save(Expense);
		boolean success = UpdateEmployeeExpensesDueAndPaid(Expense.getEmployee().getId());
		if(!success) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Expense>(newExpense, HttpStatus.CREATED);
	}
	
	//PayExpense(expenseId)
	@SuppressWarnings("rawtypes")
	@PutMapping("pay/{expenseId}")
	public ResponseEntity PayExpense(@PathVariable int expenseId, @RequestBody Expense expense) {
		Optional<Expense> payExpenses = expRepo.findById(expenseId);
		if(expense.getId() != expenseId) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		payExpenses.get().setStatus(Status_Paid);
		Optional<Employee> getEmployee = empRepo.findById(expense.getEmployee().getId());
		getEmployee.get().setExpensesPaid(payExpenses.get().getTotal());
		if(getEmployee.get().getExpensesDue() - payExpenses.get().getTotal() < 0) {
			getEmployee.get().setExpensesDue(0);
		}
		else {
			getEmployee.get().setExpensesDue(getEmployee.get().getExpensesDue() - payExpenses.get().getTotal());
		}
		putExpense(expenseId, payExpenses.get());
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	
	@SuppressWarnings("rawtypes")
	@PutMapping("review/{id}")
	public ResponseEntity ReviewExpense(@PathVariable int id, @RequestBody Expense Expense) {
		String newStatus = Expense.getTotal() <= 75 ? Status_Approved : Status_Review;
		Expense.setStatus(newStatus);
		return putExpense(id, Expense);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("approve/{id}")
	public ResponseEntity ApproveExpense(@PathVariable int id, @RequestBody Expense Expense) {
		Expense.setStatus(Status_Approved);
		return putExpense(id, Expense);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("reject/{id}")
	public ResponseEntity RejectExpense(@PathVariable int id, @RequestBody Expense Expense) {
		Expense.setStatus(Status_Rejected);
		return putExpense(id, Expense);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putExpense(@PathVariable int id, @RequestBody Expense Expense) {
		if(Expense.getId() != id) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		expRepo.save(Expense);
		boolean success = UpdateEmployeeExpensesDueAndPaid(Expense.getEmployee().getId());
		if(!success) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteExpense(@PathVariable int id) {
		Optional<Expense> Expense = expRepo.findById(id);
		if(Expense.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		expRepo.delete(Expense.get());
		boolean success = UpdateEmployeeExpensesDueAndPaid(Expense.get().getEmployee().getId());
		if(!success) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
