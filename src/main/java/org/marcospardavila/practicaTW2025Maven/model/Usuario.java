package org.marcospardavila.practicaTW2025Maven.model;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor 
@Builder // Para poder construir objetos Usuario más fácilmente sin todos los campos (como id)
@Entity
@Table(name = "USUARIO")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 20)
    private String tipo; // Cliente, Personal, Administrador

    @Setter
    @Column(nullable = false, length = 50)
    private String nombre;

    @Setter
    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Setter
    @Column(nullable = false, length = 255)
    private String contrasena; // Necesita setter para que el servicio pueda cifrarla

    @Setter
    @Column(length = 255)
    private String direccion;

    @Setter
    @Column(length = 100)
    private String poblacion;

    @Setter
    @Column(length = 100)
    private String provincia;

    @Setter
    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;

    @Setter
    @Column(length = 20)
    private String telefono;

    @Setter
    @Column(name = "numero_tarjeta_credito", length = 20)
    private String numeroTarjetaCredito;

    // Constructor @Builder para crear instancias sin ID y con tipo "Cliente" por defecto.
    // Este es el builder principal para el registro de clientes.
    @Builder(builderMethodName = "usuarioBuilder")
    public Usuario(String tipo, String nombre, String apellidos, String email, String contrasena,
                   String direccion, String poblacion, String provincia, String codigoPostal,
                   String telefono, String numeroTarjetaCredito) {
        this.tipo = tipo; 
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.contrasena = contrasena;
        this.direccion = direccion;
        this.poblacion = poblacion;
        this.provincia = provincia;
        this.codigoPostal = codigoPostal;
        this.telefono = telefono;
        this.numeroTarjetaCredito = numeroTarjetaCredito;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + tipo.toUpperCase()));
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
