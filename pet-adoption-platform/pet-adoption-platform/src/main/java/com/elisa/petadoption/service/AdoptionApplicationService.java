package com.elisa.petadoption.service;

import com.elisa.petadoption.dto.ApplicationRequest;
import com.elisa.petadoption.entity.AdoptionApplication;
import com.elisa.petadoption.entity.AppUser;
import com.elisa.petadoption.entity.ApplicationStatus;
import com.elisa.petadoption.entity.PetStatus;
import com.elisa.petadoption.exception.ResourceNotFoundException;
import com.elisa.petadoption.exception.UnauthorizedAccessException;
import com.elisa.petadoption.repository.AdoptionApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdoptionApplicationService {
    private final AdoptionApplicationRepository applicationRepository;
    private final PetService petService;
    private final UserService userService;

    public AdoptionApplicationService(AdoptionApplicationRepository applicationRepository, PetService petService, UserService userService) {
        this.applicationRepository = applicationRepository;
        this.petService = petService;
        this.userService = userService;
    }

    public List<AdoptionApplication> findAll() {
        return applicationRepository.findAll();
    }

    public List<AdoptionApplication> findForUser(String username) {
        AppUser user = userService.findByUsername(username);
        return applicationRepository.findByApplicantOrderByCreatedAtDesc(user);
    }

    @Transactional
    public AdoptionApplication create(Long petId, String username, ApplicationRequest request) {
        var pet = petService.findById(petId);
        if (pet.getStatus() == PetStatus.ADOPTED) {
            throw new IllegalArgumentException("This pet is already adopted");
        }
        AdoptionApplication application = new AdoptionApplication();
        application.setPet(pet);
        application.setApplicant(userService.findByUsername(username));
        application.setMotivation(request.getMotivation());
        return applicationRepository.save(application);
    }

    @Transactional
    public AdoptionApplication changeStatus(Long id, ApplicationStatus status) {
        AdoptionApplication application = findById(id);
        application.setStatus(status);
        if (status == ApplicationStatus.APPROVED) {
            application.getPet().setStatus(PetStatus.RESERVED);
        }
        return applicationRepository.save(application);
    }

    @Transactional
    public void cancel(Long id, String username) {
        AdoptionApplication application = findById(id);
        if (!application.getApplicant().getUsername().equals(username)) {
            throw new UnauthorizedAccessException("You can cancel only your own applications");
        }
        application.setStatus(ApplicationStatus.CANCELLED);
        applicationRepository.save(application);
    }

    public AdoptionApplication findById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
    }
}
