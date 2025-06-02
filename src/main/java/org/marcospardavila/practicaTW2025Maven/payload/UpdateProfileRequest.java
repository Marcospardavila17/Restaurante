package org.marcospardavila.practicaTW2025Maven.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.marcospardavila.practicaTW2025Maven.model.Usuario;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
    private String nombre;
    private String apellidos;
    private String direccion;
    private String poblacion;
    private String provincia;
    private String codigoPostal;
    private String telefono;
    private String numeroTarjetaCredito;
    private String contrasenaActual;
    private String contrasenaNueva;

    public Usuario toUsuario(Usuario usuarioActual) {
        return Usuario.usuarioBuilder()
                .tipo(usuarioActual.getTipo())
                .email(usuarioActual.getEmail())
                .nombre(this.nombre)
                .apellidos(this.apellidos)
                .direccion(this.direccion)
                .poblacion(this.poblacion)
                .provincia(this.provincia)
                .codigoPostal(this.codigoPostal)
                .telefono(this.telefono)
                .numeroTarjetaCredito(this.numeroTarjetaCredito)
                .contrasena(this.contrasenaNueva != null && !this.contrasenaNueva.isEmpty()
                        ? this.contrasenaNueva
                        : usuarioActual.getContrasena())
                .build();
    }
}
