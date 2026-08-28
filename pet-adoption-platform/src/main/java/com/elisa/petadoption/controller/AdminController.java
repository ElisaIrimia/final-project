package com.elisa.petadoption.controller;

import com.elisa.petadoption.entity.Pet;
import com.elisa.petadoption.entity.PetStatus;
import com.elisa.petadoption.entity.Shelter;
import com.elisa.petadoption.service.PetService;
import com.elisa.petadoption.service.PetImageStorageService;
import com.elisa.petadoption.service.ShelterService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class AdminController {
    private final PetService petService;
    private final ShelterService shelterService;
    private final PetImageStorageService petImageStorageService;

    public AdminController(PetService petService, ShelterService shelterService, PetImageStorageService petImageStorageService) {
        this.petService = petService;
        this.shelterService = shelterService;
        this.petImageStorageService = petImageStorageService;
    }

    @GetMapping("/admin/pets/new")
    public String newPet(Model model) {
        model.addAttribute("pet", new Pet());
        addPetFormData(model);
        return "admin/pet-form";
    }

    @GetMapping("/admin/pets/{id}/edit")
    public String editPet(@PathVariable Long id, Model model) {
        model.addAttribute("pet", petService.findById(id));
        addPetFormData(model);
        return "admin/pet-form";
    }

    @PostMapping("/admin/pets")
    public String savePet(@Valid @ModelAttribute Pet pet, BindingResult bindingResult, @RequestParam(required = false) MultipartFile imageFile, Model model) {
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                pet.setImageUrl(petImageStorageService.store(imageFile));
            } catch (IllegalArgumentException | IOException exception) {
                bindingResult.rejectValue("imageUrl", "image.invalid", exception.getMessage());
            }
        }
        if (bindingResult.hasErrors()) {
            addPetFormData(model);
            return "admin/pet-form";
        }
        petService.save(pet);
        return "redirect:/dashboard";
    }

    @PostMapping("/admin/pets/{id}/delete")
    public String deletePet(@PathVariable Long id) {
        petService.delete(id);
        return "redirect:/dashboard";
    }

    @GetMapping("/admin/shelters/new")
    public String newShelter(Model model) {
        model.addAttribute("shelter", new Shelter());
        return "admin/shelter-form";
    }

    @PostMapping("/admin/shelters")
    public String saveShelter(@Valid @ModelAttribute Shelter shelter, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/shelter-form";
        }
        shelterService.save(shelter);
        return "redirect:/dashboard";
    }

    private void addPetFormData(Model model) {
        model.addAttribute("shelters", shelterService.findAll());
        model.addAttribute("statuses", PetStatus.values());
    }
}
