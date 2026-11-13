package com.critter.service;

import com.critter.entity.Customer;
import com.critter.entity.Employee;
import com.critter.entity.Pet;
import com.critter.model.EmployeeSkill;
import com.critter.repository.CustomerRepository;
import com.critter.repository.EmployeeRepository;
import com.critter.repository.PetRepository;
import javax.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final PetRepository petRepository;

    public UserService(CustomerRepository customerRepository, EmployeeRepository employeeRepository,
                       PetRepository petRepository) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.petRepository = petRepository;
    }

    @Transactional
    public Customer saveCustomer(Customer customer, List<Long> petIds) {
        Customer savedCustomer;
        if (customer.getId() == null) {
            savedCustomer = customerRepository.save(customer);
        } else {
            savedCustomer = customerRepository.findById(customer.getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Customer not found: " + customer.getId()));
            savedCustomer.setName(customer.getName());
            savedCustomer.setPhoneNumber(customer.getPhoneNumber());
            savedCustomer.setAddress(customer.getAddress());
        }
        if (petIds != null) {
            Set<Long> requestedPetIds = new HashSet<>(petIds);
            savedCustomer.getPets().stream()
                    .filter(existingPet -> !requestedPetIds.contains(existingPet.getId()))
                    .forEach(existingPet -> existingPet.setCustomer(null));
            List<Pet> pets = petIds.stream()
                    .distinct()
                    .map(id -> petRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Pet not found: " + id)))
                    .collect(Collectors.toList());
            pets.forEach(pet -> {
                Customer previousCustomer = pet.getCustomer();
                if (previousCustomer != null && !previousCustomer.getId().equals(savedCustomer.getId())) {
                    previousCustomer.getPets().removeIf(existingPet -> existingPet.getId().equals(pet.getId()));
                }
                pet.setCustomer(savedCustomer);
            });
            savedCustomer.setPets(pets);
        }
        return customerRepository.save(savedCustomer);
    }

    @Transactional
    public Employee saveEmployee(Employee employee) {
        if (employee.getSkills() == null) {
            employee.setSkills(Set.of());
        }
        if (employee.getDaysAvailable() == null) {
            employee.setDaysAvailable(Set.of());
        }
        return employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Customer> getCustomersByName(String name) {
        return customerRepository.findByNameIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public Customer getCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + customerId));
    }

    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Employee getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + employeeId));
    }

    @Transactional
    public Employee setEmployeeAvailability(Set<DayOfWeek> daysAvailable, Long employeeId) {
        Employee employee = getEmployee(employeeId);
        employee.setDaysAvailable(daysAvailable == null ? Set.of() : daysAvailable);
        return employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public List<Employee> findAvailableEmployees(Set<EmployeeSkill> skills, LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Availability date is required");
        }
        Set<EmployeeSkill> requestedSkills = skills == null ? Set.of() : skills;
        DayOfWeek requestedDay = date.getDayOfWeek();
        return employeeRepository.findAll().stream()
                .filter(employee -> employee.getDaysAvailable().contains(requestedDay))
                .filter(employee -> employee.getSkills().containsAll(requestedSkills))
                .collect(Collectors.toList());
    }
}
