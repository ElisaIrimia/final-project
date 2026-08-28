package com.elisa.petadoption.service;

import com.elisa.petadoption.entity.Pet;
import com.elisa.petadoption.entity.PetStatus;
import com.elisa.petadoption.exception.ResourceNotFoundException;
import com.elisa.petadoption.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {
    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> search(String query, PetStatus status) {
        String normalizedQuery = query == null || query.isBlank() ? null : query.trim();
        return petRepository.search(normalizedQuery, status);
    }

    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    public Pet findById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
    }

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    public void delete(Long id) {
        petRepository.delete(findById(id));
    }
}
