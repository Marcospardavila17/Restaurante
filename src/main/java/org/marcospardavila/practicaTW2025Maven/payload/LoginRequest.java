package org.marcospardavila.practicaTW2025Maven.payload; // O el paquete donde tengas tus DTOs

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String contrasena;
}