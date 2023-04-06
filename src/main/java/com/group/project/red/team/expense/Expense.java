package com.group.project.red.team.expense;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.group.project.red.team.employee.Employee;
import com.group.project.red.team.expenseline.Expenseline;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="Expenses")
public class Expense {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(length=80, nullable=false)
	private String description;
	
	@Column(length=10, nullable=false)
	private String status;
	
	@Column(columnDefinition="decimal (11,2) not null default 0")
	private double total;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="employeeId", columnDefinition="INT")
	private Employee employee;
	
	@JsonManagedReference
	@OneToMany(mappedBy="expense")
	private List<Expenseline> expenselines;
	
	public Expense() {}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public List<Expenseline> getExpenselines() {
		return expenselines;
	}

	public void setExpenselines(List<Expenseline> expenselines) {
		this.expenselines = expenselines;
	}
	
}
