package com.critter.service;

import com.critter.entity.Customer;
import com.critter.entity.Pet;
import com.critter.repository.CustomerRepository;
import com.critter.repository.PetRepository;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PetService {
    private final PetRepository petRepository;
    private final CustomerRepository customerRepository;

    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Pet savePet(Pet pet, Long customerId) {
        Pet existingPet = null;
        if (pet.getId() != null) {
            existingPet = petRepository.findById(pet.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Pet not found: " + pet.getId()));
        }
        Customer previousCustomer = existingPet == null ? null : existingPet.getCustomer();
        if (previousCustomer != null) {
            Long existingPetId = existingPet.getId();
            previousCustomer.getPets().removeIf(existing -> existing.getId().equals(existingPetId));
        }
        if (customerId != null) {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + customerId));
            pet.setCustomer(customer);
            customer.getPets().add(pet);
        } else {
            pet.setCustomer(null);
        }
        return petRepository.save(pet);
    }

    @Transactional(readOnly = true)
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pet getPet(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found: " + petId));
    }

    @Transactional(readOnly = true)
    public List<Pet> findPetsByOwner(Long ownerId) {
        if (!customerRepository.existsById(ownerId)) {
            throw new EntityNotFoundException("Customer not found: " + ownerId);
        }
        return petRepository.findByCustomerId(ownerId);
    }
}
