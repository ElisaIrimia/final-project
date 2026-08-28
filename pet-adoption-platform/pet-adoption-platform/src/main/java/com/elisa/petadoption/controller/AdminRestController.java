package com.elisa.petadoption.controller;

import com.elisa.petadoption.entity.AdoptionApplication;
import com.elisa.petadoption.entity.ApplicationStatus;
import com.elisa.petadoption.entity.Shelter;
import com.elisa.petadoption.service.AdoptionApplicationService;
import com.elisa.petadoption.service.ShelterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {
    private final ShelterService shelterService;
    private final AdoptionApplicationService applicationService;

    public AdminRestController(ShelterService shelterService, AdoptionApplicationService applicationService) {
        this.shelterService = shelterService;
        this.applicationService = applicationService;
    }

    @GetMapping("/shelters")
    public List<Shelter> shelters() {
        return shelterService.findAll();
    }

    @PostMapping("/shelters")
    @ResponseStatus(HttpStatus.CREATED)
    public Shelter createShelter(@Valid @RequestBody Shelter shelter) {
        return shelterService.save(shelter);
    }

    @GetMapping("/applications")
    public List<AdoptionApplication> applications() {
        return applicationService.findAll();
    }

    @PutMapping("/applications/{id}/{status}")
    public AdoptionApplication updateApplication(@PathVariable Long id, @PathVariable ApplicationStatus status) {
        return applicationService.changeStatus(id, status);
    }
}
