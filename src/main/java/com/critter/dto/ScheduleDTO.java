package com.critter.dto;

import com.critter.model.EmployeeSkill;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ScheduleDTO {
    private Long id;
    private Set<Long> employeeIds = new HashSet<>();
    private Set<Long> petIds = new HashSet<>();
    private LocalDate date;
    private Set<EmployeeSkill> activities = new HashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Set<Long> getEmployeeIds() { return employeeIds; }
    public void setEmployeeIds(Set<Long> employeeIds) { this.employeeIds = employeeIds; }
    public Set<Long> getPetIds() { return petIds; }
    public void setPetIds(Set<Long> petIds) { this.petIds = petIds; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Set<EmployeeSkill> getActivities() { return activities; }
    public void setActivities(Set<EmployeeSkill> activities) { this.activities = activities; }
}
