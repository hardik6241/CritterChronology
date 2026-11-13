package com.critter.dto;

import java.util.List;

public class CustomerDTO {
    private Long id;
    private String name;
    private String phoneNumber;
    private String address;
    private List<Long> petIds;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public List<Long> getPetIds() { return petIds; }
    public void setPetIds(List<Long> petIds) { this.petIds = petIds; }
}
