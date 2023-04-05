package com.group.project.red.team.expenseline;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.group.project.red.team.expense.Expense;
import com.group.project.red.team.expense.ExpenseRepository;
import com.group.project.red.team.item.Item;
import com.group.project.red.team.item.ItemRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/expenselines")
public class ExpenselineController {
	
	@Autowired
	private ExpenselineRepository explRepo;
	@Autowired
	private ExpenseRepository expRepo;	
	@Autowired
	private ItemRepository itemRepo;
	
//\\ Recalculate: \\//	
	private boolean recalcExpenseTotal(int expenseId) {
		Optional<Expense> anExpense = expRepo.findById(expenseId);
		if(anExpense.isEmpty()) {
			return false;
		}
		Expense expense = anExpense.get();
		Iterable<Expenseline> expenselines = explRepo.findByExpenseId(expenseId);
		double total = 0;
		for(Expenseline el : expenselines) {
			if(el.getItem().getName() == null) {
				Item item = itemRepo.findById(el.getItem().getId()).get();
				el.setItem(item);
			}
			total += el.getQuantity() * el.getItem().getPrice();
		}
		expense.setTotal(total);
		expRepo.save(expense);
		
		return true;
	}
	
	
	
	@GetMapping
	public ResponseEntity<Iterable<Expenseline>> getExpenselines(){
		Iterable<Expenseline> expenselines = explRepo.findAll();
		return new ResponseEntity<Iterable<Expenseline>>(expenselines, HttpStatus.OK);
	}	

	@GetMapping("{id}")
	public ResponseEntity<Expenseline> getExpenseline(@PathVariable int id){
		Optional<Expenseline> expenseline = explRepo.findById(id);
		if(expenseline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Expenseline>(expenseline.get(), HttpStatus.OK);		
	}


//\\ Using Recalculate: \\//
	@PostMapping
	public ResponseEntity<Expenseline> postExpenseline(@RequestBody Expenseline expenseline){
		Expenseline newExpenseline = explRepo.save(expenseline);
		explRepo.findById(newExpenseline.getId());
		Optional<Expense> expense = expRepo.findById(expenseline.getExpense().getId());
		if(!expense.isEmpty()) {
			boolean success = recalcExpenseTotal(expense.get().getId());
			if(!success) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<Expenseline>(newExpenseline, HttpStatus.CREATED);	
	}
	
	@SuppressWarnings("rawtypes")	
	@PutMapping("{id}")
	public ResponseEntity putExpenseline(@PathVariable int id, @RequestBody Expenseline expenseline){
		if(expenseline.getId() != id) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		explRepo.save(expenseline);
		Optional<Expense> expense = expRepo.findById(expenseline.getExpense().getId());
		if(!expense.isEmpty()) {
			boolean success = recalcExpenseTotal(expense.get().getId());
			if(!success) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings({ "rawtypes" })
	@DeleteMapping("{id}")
	public ResponseEntity deleteRequestline(@PathVariable int id){
		Optional<Expenseline> requestline = explRepo.findById(id);
		if(requestline.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		explRepo.delete(requestline.get());
		Optional<Expense> request = expRepo.findById(requestline.get().getExpense().getId());
		if(!request.isEmpty()) {
			boolean success = recalcExpenseTotal(request.get().getId());
			if(!success) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
	}
}
