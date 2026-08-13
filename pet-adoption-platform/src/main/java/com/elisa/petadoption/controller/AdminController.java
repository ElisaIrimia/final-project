package com.elisa.petadoption.controller;

import com.elisa.petadoption.entity.Pet;
import com.elisa.petadoption.entity.PetStatus;
import com.elisa.petadoption.entity.Shelter;
import com.elisa.petadoption.service.PetService;
import com.elisa.petadoption.service.ShelterService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {
    private final PetService petService;
    private final ShelterService shelterService;

    public AdminController(PetService petService, ShelterService shelterService) {
        this.petService = petService;
        this.shelterService = shelterService;
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
    public String savePet(@Valid @ModelAttribute Pet pet, BindingResult bindingResult, Model model) {
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
