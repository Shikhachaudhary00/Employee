package com.codeWithShikha.employee.service;

import com.codeWithShikha.employee.entity.Employee;
import com.codeWithShikha.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    // Constructor with JdbcTemplate for database operations like resetting auto-increment
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, JdbcTemplate jdbcTemplate) {
        this.employeeRepository = employeeRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    // Method to add a new employee
    public Employee postEmployee(Employee employee) {
        return employeeRepository.save(employee);  // Save employee to the repository
    }

    // Method to retrieve all employees
    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }

    // Reset auto-increment after deleting an employee (Optional for dev environments)
    public void resetAutoIncrement() {
        String sql = "ALTER TABLE employee AUTO_INCREMENT = 1";
        jdbcTemplate.execute(sql); // Executes the SQL to reset auto-increment value
    }

    // Method to delete an employee by ID and reset auto-increment (optional)
    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EntityNotFoundException("Employee with Id " + id + " not found");
        }
        employeeRepository.deleteById(id);
        // Reset auto-increment after deletion (optional)
        resetAutoIncrement();
    }

    // Method to retrieve an employee by ID
    public Employee getEmployeeById(Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            logger.info("Employee found: " + employee.getName());
            return employee;
        } else {
            logger.warn("Employee not found with ID: " + id);
            return null;
        }
    }

    // Method to update an employee by ID
    @Transactional
    public Employee updateEmployee(Long id, Employee employee) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee existingEmployee = optionalEmployee.get();
            existingEmployee.setName(employee.getName());
            existingEmployee.setEmail(employee.getEmail());
            existingEmployee.setPhone(employee.getPhone());
            existingEmployee.setDepartment(employee.getDepartment());

            return employeeRepository.save(existingEmployee);
        }
        throw new EntityNotFoundException("Employee with ID " + id + " not found for update");
    }
}
