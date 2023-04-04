package com.group.project.red.team.item;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/api/items")
public class ItemController {

	@Autowired
	private ItemRepository itemRepo;
	
	@GetMapping
	public ResponseEntity<Iterable<Item>> getItems(){
		Iterable<Item> items = itemRepo.findAll();
		return new ResponseEntity<Iterable<Item>>(items, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Item> getItem(@PathVariable int id){
		Optional<Item> item = itemRepo.findById(id);
		if(item.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Item>(item.get(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Item> postItem(@RequestBody Item item) {
		Item newItem = itemRepo.save(item);
		return new ResponseEntity<Item>(newItem, HttpStatus.CREATED);
	}
		
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putItem(@PathVariable int id, @RequestBody Item item) {
		itemRepo.save(item);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteItem(@PathVariable int id) {
		Optional<Item> item = itemRepo.findById(id);
		if(item.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		itemRepo.delete(item.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
}
