package com.critter.controller;

import com.critter.dto.PetDTO;
import com.critter.entity.Pet;
import com.critter.service.PetService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pet")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = new Pet();
        pet.setId(petDTO.getId());
        pet.setName(petDTO.getName());
        pet.setType(petDTO.getType());
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setNotes(petDTO.getNotes());
        return toDTO(petService.savePet(pet, petDTO.getOwnerId()));
    }

    @GetMapping
    public List<PetDTO> getPets() {
        return petService.getAllPets().stream().map(PetController::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable Long petId) {
        return toDTO(petService.getPet(petId));
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable Long ownerId) {
        return petService.findPetsByOwner(ownerId).stream()
                .map(PetController::toDTO)
                .collect(Collectors.toList());
    }

    private static PetDTO toDTO(Pet pet) {
        PetDTO dto = new PetDTO();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setType(pet.getType());
        dto.setOwnerId(pet.getCustomer() == null ? null : pet.getCustomer().getId());
        dto.setBirthDate(pet.getBirthDate());
        dto.setNotes(pet.getNotes());
        return dto;
    }
}
