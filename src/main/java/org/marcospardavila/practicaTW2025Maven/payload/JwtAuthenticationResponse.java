package org.marcospardavila.practicaTW2025Maven.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer"; // Prefijo estándar para JWTs
    private String tipoUsuario; // Nuevo campo para el tipo de usuario
    private String nombreUsuario; // Nuevo campo para el nombre del usuario
    private Integer idUsuario; // Nuevo campo para el ID del usuario
    private String email; // Nuevo campo para el email del usuario
}
