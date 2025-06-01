package org.marcospardavila.practicaTW2025Maven.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @GetMapping("/menu")
    public String mostrarMenu() {
        return "cliente/menu";
    }

    @GetMapping("/pedidos")
    public String mostrarPedidos() {
        return "cliente/pedidos";
    }

    @GetMapping("/perfil")
    public String mostrarPerfil() {
        return "cliente/perfil";
    }

    @GetMapping("/carrito")
    public String mostrarCarrito() {
        return "cliente/carrito";
    }
}
