package org.marcospardavila.practicaTW2025Maven.payload; // O el paquete donde tengas tus DTOs

import lombok.Data;

@Data // Lombok para getters, setters, toString, equals, hashCode
public class RegisterRequest {
    private String tipo; // Cliente, Personal, Administrador
    private String nombre;
    private String apellidos;
    private String email;
    private String contrasena;
    private String direccion;
    private String poblacion;
    private String provincia;
    private String codigoPostal;
    private String telefono;
    private String numeroTarjetaCredito;
}