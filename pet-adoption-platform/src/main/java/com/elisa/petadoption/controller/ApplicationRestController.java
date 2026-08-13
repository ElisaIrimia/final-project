package com.elisa.petadoption.controller;

import com.elisa.petadoption.dto.ApplicationRequest;
import com.elisa.petadoption.entity.AdoptionApplication;
import com.elisa.petadoption.service.AdoptionApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationRestController {
    private final AdoptionApplicationService applicationService;

    public ApplicationRestController(AdoptionApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/mine")
    public List<AdoptionApplication> mine(Authentication authentication) {
        return applicationService.findForUser(authentication.getName());
    }

    @PostMapping("/pets/{petId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AdoptionApplication apply(@PathVariable Long petId, @Valid @RequestBody ApplicationRequest request, Authentication authentication) {
        return applicationService.create(petId, authentication.getName(), request);
    }
}
