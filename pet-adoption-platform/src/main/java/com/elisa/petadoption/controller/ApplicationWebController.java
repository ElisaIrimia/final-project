package com.elisa.petadoption.controller;

import com.elisa.petadoption.dto.ApplicationRequest;
import com.elisa.petadoption.entity.ApplicationStatus;
import com.elisa.petadoption.service.AdoptionApplicationService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ApplicationWebController {
    private final AdoptionApplicationService applicationService;

    public ApplicationWebController(AdoptionApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/pets/{petId}/apply")
    public String apply(@PathVariable Long petId, @Valid ApplicationRequest request, BindingResult bindingResult,
                        Authentication authentication, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Motivation must have between 20 and 1200 characters.");
            return "redirect:/pets/" + petId;
        }
        applicationService.create(petId, authentication.getName(), request);
        redirectAttributes.addFlashAttribute("success", "Application submitted.");
        return "redirect:/dashboard";
    }

    @PostMapping("/applications/{id}/cancel")
    public String cancel(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        applicationService.cancel(id, authentication.getName());
        redirectAttributes.addFlashAttribute("success", "Application cancelled.");
        return "redirect:/dashboard";
    }

    @PostMapping("/admin/applications/{id}/{status}")
    public String changeStatus(@PathVariable Long id, @PathVariable ApplicationStatus status, RedirectAttributes redirectAttributes) {
        applicationService.changeStatus(id, status);
        redirectAttributes.addFlashAttribute("success", "Application updated.");
        return "redirect:/dashboard";
    }
}
