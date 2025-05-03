package com.codeWithShikha.employee.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.codeWithShikha.employee.entity.Employee;
import com.codeWithShikha.employee.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow CORS requests from React app on port 5173
public class EmployeeController {

    private final EmployeeService employeeService;

    // Constructor to inject EmployeeService
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Endpoint to add a new employee
    @PostMapping("/employee")
    public ResponseEntity<Employee> postEmployee(@RequestBody Employee employee) {
        Employee savedEmployee = employeeService.postEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee); // Returning 201 Created status
    }

    // Endpoint to get all employees
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployee() {
        List<Employee> employees = employeeService.getAllEmployee();
        if (employees.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Returning 204 if no employees are found
        }
        return ResponseEntity.ok(employees); // Returning 200 with the list of employees
    }

    // Endpoint to delete an employee by ID
    @DeleteMapping("/employee/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.status(HttpStatus.OK).body("Employee with ID " + id + " deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with ID: " + id); // 404 if employee not found
        }
    }

    // Endpoint to get an employee by ID
    @GetMapping("/employee/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with ID: " + id); // Returning 404 if not found
        }
        return ResponseEntity.ok(employee); // Returning 200 with the employee details
    }

    // Endpoint to update an employee by ID
    @PatchMapping("/employee/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        Employee updateEmployee = employeeService.updateEmployee(id, employee);
        if (updateEmployee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee with ID " + id + " not found for update");
        }
        return ResponseEntity.ok(updateEmployee);
    }
}
