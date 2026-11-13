package com.critter.dto;

import com.critter.model.PetType;
import java.time.LocalDate;

public class PetDTO {
    private Long id;
    private String name;
    private PetType type;
    private Long ownerId;
    private LocalDate birthDate;
    private String notes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public PetType getType() { return type; }
    public void setType(PetType type) { this.type = type; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
