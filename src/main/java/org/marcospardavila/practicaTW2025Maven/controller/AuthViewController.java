package org.marcospardavila.practicaTW2025Maven.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthViewController {

    @GetMapping("/login")
    public String mostrarLogin() {
        return "auth/login"; // Resuelve a /WEB-INF/views/auth/login.jsp
    }

    @GetMapping("/register")
    public String mostrarRegistro() {
        return "auth/register";
    }
}
