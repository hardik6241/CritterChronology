package com.critter.dto;

import com.critter.model.EmployeeSkill;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

public class EmployeeDTO {
    private Long id;
    private String name;
    private String phoneNumber;
    private Set<EmployeeSkill> skills = new HashSet<>();
    private Set<DayOfWeek> daysAvailable = new HashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public Set<EmployeeSkill> getSkills() { return skills; }
    public void setSkills(Set<EmployeeSkill> skills) { this.skills = skills; }
    public Set<DayOfWeek> getDaysAvailable() { return daysAvailable; }
    public void setDaysAvailable(Set<DayOfWeek> daysAvailable) { this.daysAvailable = daysAvailable; }
}
