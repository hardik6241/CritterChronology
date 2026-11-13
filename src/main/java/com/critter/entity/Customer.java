package com.critter.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer extends User {
    private String address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Pet> pets = new ArrayList<>();

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public List<Pet> getPets() { return pets; }
    public void setPets(List<Pet> pets) { this.pets = pets == null ? new ArrayList<>() : new ArrayList<>(pets); }
}
