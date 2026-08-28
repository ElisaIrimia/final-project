package com.elisa.petadoption.controller;

import com.elisa.petadoption.dto.ApplicationRequest;
import com.elisa.petadoption.entity.PetStatus;
import com.elisa.petadoption.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PetWebController {
    private final PetService petService;

    public PetWebController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/pets")
    public String list(@RequestParam(required = false) String q, @RequestParam(required = false) PetStatus status, Model model) {
        model.addAttribute("pets", petService.search(q, status));
        model.addAttribute("q", q);
        model.addAttribute("statuses", PetStatus.values());
        return "pets/list";
    }

    @GetMapping("/pets/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("pet", petService.findById(id));
        model.addAttribute("applicationRequest", new ApplicationRequest());
        return "pets/detail";
    }
}
