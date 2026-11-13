package com.critter.controller;

import com.critter.dto.CustomerDTO;
import com.critter.dto.EmployeeAvailabilityRequestDTO;
import com.critter.dto.EmployeeDTO;
import com.critter.entity.Customer;
import com.critter.entity.Employee;
import com.critter.service.UserService;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setId(customerDTO.getId());
        customer.setName(customerDTO.getName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setAddress(customerDTO.getAddress());
        return toCustomerDTO(userService.saveCustomer(customer, customerDTO.getPetIds()));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getCustomers(@RequestParam(required = false) String name) {
        List<Customer> customers = name == null || name.isBlank()
                ? userService.getAllCustomers() : userService.getCustomersByName(name);
        return customers.stream()
                .map(UserController::toCustomerDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public CustomerDTO getCustomer(@PathVariable Long customerId) {
        return toCustomerDTO(userService.getCustomer(customerId));
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return toEmployeeDTO(userService.saveEmployee(toEmployee(employeeDTO)));
    }

    @GetMapping("/employee")
    public List<EmployeeDTO> getEmployees() {
        return userService.getAllEmployees().stream()
                .map(UserController::toEmployeeDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable Long employeeId) {
        return toEmployeeDTO(userService.getEmployee(employeeId));
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO setEmployeeAvailability(@RequestBody Set<DayOfWeek> daysAvailable,
                                               @PathVariable Long employeeId) {
        return toEmployeeDTO(userService.setEmployeeAvailability(daysAvailable, employeeId));
    }

    @PostMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeAvailabilityRequestDTO request) {
        return userService.findAvailableEmployees(request.getSkills(), request.getDate()).stream()
                .map(UserController::toEmployeeDTO)
                .collect(Collectors.toList());
    }

    private static Employee toEmployee(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setName(dto.getName());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setSkills(dto.getSkills());
        employee.setDaysAvailable(dto.getDaysAvailable());
        return employee;
    }

    private static EmployeeDTO toEmployeeDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setPhoneNumber(employee.getPhoneNumber());
        dto.setSkills(employee.getSkills());
        dto.setDaysAvailable(employee.getDaysAvailable());
        return dto;
    }

    private static CustomerDTO toCustomerDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setAddress(customer.getAddress());
        dto.setPetIds(customer.getPets().stream().map(pet -> pet.getId()).collect(Collectors.toList()));
        return dto;
    }
}
