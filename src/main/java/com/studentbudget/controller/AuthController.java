package com.studentbudget.controller;

import com.studentbudget.model.RegistrationForm;
import com.studentbudget.service.AppUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final AppUserService userService;
    public AuthController(AppUserService userService) { this.userService = userService; }
    @GetMapping("/login")
    public String login() { return "login"; }
    @GetMapping("/register")
    public String registerForm(Model model) { model.addAttribute("form", new RegistrationForm()); return "register"; }
    @PostMapping("/register")
    public String register(@ModelAttribute("form") RegistrationForm form, Model model) {
        String error = userService.register(form);
        if (error == null) return "redirect:/login?registered";
        model.addAttribute("error", error); return "register";
    }
}
