package com.critter.repository;

import com.critter.entity.Pet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByCustomerId(Long customerId);
}
