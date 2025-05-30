package org.marcospardavila.practicaTW2025Maven.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
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
