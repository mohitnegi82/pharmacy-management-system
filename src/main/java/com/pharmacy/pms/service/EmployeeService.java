package com.pharmacy.pms.service;

import com.pharmacy.pms.model.Employee;
import com.pharmacy.pms.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee save(Employee employee) {
        // NOTE: Employee uses a primitive `boolean active` field. When Spring's data binder
        // constructs a new Employee via the no-args constructor (as it does for form submissions),
        // Lombok's @Builder.Default does NOT apply — only the Builder path gets it. That left new
        // employees silently saved as active=false with no "Activate" button to recover them.
        // Fix: explicitly default new employees (id == null) to active here.
        if (employee.getId() == null) {
            employee.setActive(true);
        }
        return employeeRepository.save(employee);
    }

    public void deactivate(Long id) {
        Employee e = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + id));
        e.setActive(false);
        employeeRepository.save(e);
    }

    public void activate(Long id) {
        Employee e = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + id));
        e.setActive(true);
        employeeRepository.save(e);
    }
}
