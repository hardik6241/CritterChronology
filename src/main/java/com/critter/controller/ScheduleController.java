package com.critter.controller;

import com.critter.dto.ScheduleDTO;
import com.critter.entity.Schedule;
import com.critter.service.ScheduleService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO dto) {
        Schedule schedule = new Schedule();
        schedule.setId(dto.getId());
        schedule.setDate(dto.getDate());
        schedule.setActivities(dto.getActivities());
        return toDTO(scheduleService.saveSchedule(schedule, dto.getPetIds(), dto.getEmployeeIds()));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleService.getAllSchedules().stream()
                .map(ScheduleController::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getSchedulesForPet(@PathVariable Long petId) {
        return scheduleService.getSchedulesForPet(petId).stream()
                .map(ScheduleController::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getSchedulesForEmployee(@PathVariable Long employeeId) {
        return scheduleService.getSchedulesForEmployee(employeeId).stream()
                .map(ScheduleController::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getSchedulesForCustomer(@PathVariable Long customerId) {
        return scheduleService.getSchedulesForCustomer(customerId).stream()
                .map(ScheduleController::toDTO)
                .collect(Collectors.toList());
    }

    private static ScheduleDTO toDTO(Schedule schedule) {
        ScheduleDTO dto = new ScheduleDTO();
        dto.setId(schedule.getId());
        dto.setDate(schedule.getDate());
        dto.setActivities(schedule.getActivities());
        dto.setPetIds(schedule.getPets().stream().map(pet -> pet.getId()).collect(Collectors.toSet()));
        dto.setEmployeeIds(schedule.getEmployees().stream().map(employee -> employee.getId())
                .collect(Collectors.toSet()));
        return dto;
    }
}
