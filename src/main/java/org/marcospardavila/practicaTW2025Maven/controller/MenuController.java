package org.marcospardavila.practicaTW2025Maven.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    @GetMapping("/")
    public String home() {
        System.out.println("Estamos en la página home");
        return "cliente/menu";
    }

    @GetMapping("/menu")
    public String menu() {
        return "cliente/menu";
    }

    @GetMapping("/test")
    public String test() {
        return "jsp/test";
    }

}
