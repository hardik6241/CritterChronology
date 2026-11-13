package com.critter.service;

import com.critter.entity.Employee;
import com.critter.entity.Pet;
import com.critter.entity.Schedule;
import com.critter.repository.EmployeeRepository;
import com.critter.repository.PetRepository;
import com.critter.repository.ScheduleRepository;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final PetRepository petRepository;
    private final EmployeeRepository employeeRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, PetRepository petRepository,
                           EmployeeRepository employeeRepository) {
        this.scheduleRepository = scheduleRepository;
        this.petRepository = petRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Schedule saveSchedule(Schedule schedule, Set<Long> petIds, Set<Long> employeeIds) {
        if (schedule.getDate() == null) {
            throw new IllegalArgumentException("Schedule date is required");
        }
        Set<Pet> pets = (petIds == null ? Set.<Long>of() : petIds).stream()
                .distinct()
                .map(id -> petRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Pet not found: " + id)))
                .collect(Collectors.toSet());
        Set<Employee> employees = (employeeIds == null ? Set.<Long>of() : employeeIds).stream()
                .distinct()
                .map(id -> employeeRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + id)))
                .collect(Collectors.toSet());
        schedule.setPets(pets);
        schedule.setEmployees(employees);
        return scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesForPet(Long petId) {
        if (!petRepository.existsById(petId)) {
            throw new EntityNotFoundException("Pet not found: " + petId);
        }
        return scheduleRepository.findByPetsId(petId);
    }

    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesForEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EntityNotFoundException("Employee not found: " + employeeId);
        }
        return scheduleRepository.findByEmployeesId(employeeId);
    }

    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesForCustomer(Long customerId) {
        return scheduleRepository.findByPetsCustomerId(customerId);
    }
}
