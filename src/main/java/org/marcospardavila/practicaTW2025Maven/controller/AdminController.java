package org.marcospardavila.practicaTW2025Maven.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/usuarios")
    public String mostrarUsuarios() {
        return "admin/usuarios";
    }
}
