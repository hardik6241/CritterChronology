package com.critter.dto;

import com.critter.model.EmployeeSkill;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class EmployeeAvailabilityRequestDTO {
    private LocalDate date;
    private Set<EmployeeSkill> skills = new HashSet<>();

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Set<EmployeeSkill> getSkills() { return skills; }
    public void setSkills(Set<EmployeeSkill> skills) { this.skills = skills; }
}
