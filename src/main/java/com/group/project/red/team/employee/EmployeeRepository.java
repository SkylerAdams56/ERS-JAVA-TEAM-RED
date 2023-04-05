package com.group.project.red.team.employee;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Integer>{
	Optional<Employee> findByEmailAndPassword(String email, String password);

	Optional<Employee> findByEmployeeId(int employeeId);
}
