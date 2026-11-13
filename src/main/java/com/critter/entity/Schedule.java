package com.critter.entity;

import com.critter.model.EmployeeSkill;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    private Set<Employee> employees = new HashSet<>();
    @ManyToMany
    private Set<Pet> pets = new HashSet<>();
    private LocalDate date;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<EmployeeSkill> activities = new HashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Set<Employee> getEmployees() { return employees; }
    public void setEmployees(Set<Employee> employees) {
        this.employees = employees == null ? new HashSet<>() : new HashSet<>(employees);
    }
    public Set<Pet> getPets() { return pets; }
    public void setPets(Set<Pet> pets) { this.pets = pets == null ? new HashSet<>() : new HashSet<>(pets); }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Set<EmployeeSkill> getActivities() { return activities; }
    public void setActivities(Set<EmployeeSkill> activities) {
        this.activities = activities == null ? new HashSet<>() : new HashSet<>(activities);
    }
}
