package com.critter.entity;

import com.critter.model.EmployeeSkill;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Employee extends User {
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<EmployeeSkill> skills = new HashSet<>();

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> daysAvailable = new HashSet<>();

    public Set<EmployeeSkill> getSkills() { return skills; }
    public void setSkills(Set<EmployeeSkill> skills) {
        this.skills = skills == null ? new HashSet<>() : new HashSet<>(skills);
    }
    public Set<DayOfWeek> getDaysAvailable() { return daysAvailable; }
    public void setDaysAvailable(Set<DayOfWeek> daysAvailable) {
        this.daysAvailable = daysAvailable == null ? new HashSet<>() : new HashSet<>(daysAvailable);
    }
}
