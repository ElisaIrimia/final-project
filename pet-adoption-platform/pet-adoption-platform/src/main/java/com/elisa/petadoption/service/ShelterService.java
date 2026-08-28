package com.elisa.petadoption.service;

import com.elisa.petadoption.entity.Shelter;
import com.elisa.petadoption.exception.ResourceNotFoundException;
import com.elisa.petadoption.repository.ShelterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShelterService {
    private final ShelterRepository shelterRepository;

    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    public List<Shelter> findAll() {
        return shelterRepository.findAll();
    }

    public Shelter findById(Long id) {
        return shelterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shelter not found"));
    }

    public Shelter save(Shelter shelter) {
        return shelterRepository.save(shelter);
    }

    public void delete(Long id) {
        shelterRepository.delete(findById(id));
    }
}
