package com.group.project.red.team.expenseline;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import com.group.project.red.team.item.Item;
import com.group.project.red.team.expense.Expense;


@Entity
@Table(name="expenselines")
public class Expenseline {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private int quantity;
	
	@JsonBackReference
	@ManyToOne(optional=false)
	@JoinColumn(name="expenseId", columnDefinition="INT")
	private Expense expense;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="itemId", columnDefinition="INT")
	private Item item;
	
	public Expenseline() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Expense getExpense() {
		return expense;
	}

	public void setExpense(Expense expense) {
		this.expense = expense;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
}
