package com.group.project.red.team.expenseline;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.group.project.red.team.expense.ExpenseRepository;
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


	
}
