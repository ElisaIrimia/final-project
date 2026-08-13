package com.elisa.petadoption.controller;

import com.elisa.petadoption.entity.Role;
import com.elisa.petadoption.service.AdoptionApplicationService;
import com.elisa.petadoption.service.PetService;
import com.elisa.petadoption.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final PetService petService;
    private final UserService userService;
    private final AdoptionApplicationService applicationService;

    public HomeController(PetService petService, UserService userService, AdoptionApplicationService applicationService) {
        this.petService = petService;
        this.userService = userService;
        this.applicationService = applicationService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pets", petService.search(null, null));
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        var user = userService.findByUsername(authentication.getName());
        model.addAttribute("user", user);
        if (user.getRole() == Role.ROLE_ADMIN) {
            model.addAttribute("pets", petService.findAll());
            model.addAttribute("applications", applicationService.findAll());
            model.addAttribute("users", userService.findAll());
            return "admin/dashboard";
        }
        model.addAttribute("applications", applicationService.findForUser(user.getUsername()));
        return "user/dashboard";
    }
}
