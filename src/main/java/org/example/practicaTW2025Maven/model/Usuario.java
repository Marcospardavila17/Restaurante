package org.example.practicaTW2025Maven.model;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter // Lombok para generar getters para todos los campos
@NoArgsConstructor // Genera constructor sin argumentos de Lombok
@AllArgsConstructor // Genera constructor con todos los argumentos, incluyendo 'id'
@Builder // Para poder construir objetos Usuario más fácilmente sin todos los campos (como id)
@Entity
@Table(name = "USUARIO")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Estos campos deberían ser establecidos en el constructor o a través de un builder
    @Column(nullable = false, length = 20)
    private String tipo; // Cliente, Personal, Administrador

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String contrasena; // Necesita setter para que el servicio pueda cifrarla

    // Estos campos son los que normalmente podrían ser modificados por el usuario
    @Setter // Lombok para generar setter solo para 'direccion'
    @Column(length = 255)
    private String direccion;

    @Setter // Lombok para generar setter solo para 'poblacion'
    @Column(length = 100)
    private String poblacion;

    @Setter // Lombok para generar setter solo para 'provincia'
    @Column(length = 100)
    private String provincia;

    @Setter // Lombok para generar setter solo para 'codigoPostal'
    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;

    @Setter // Lombok para generar setter solo para 'telefono'
    @Column(length = 20)
    private String telefono;

    @Setter // Lombok para generar setter solo para 'numeroTarjetaCredito'
    @Column(name = "numero_tarjeta_credito", length = 20)
    private String numeroTarjetaCredito;

    // Constructor @Builder para crear instancias sin ID (para nuevas entidades)
    // El @AllArgsConstructor manejará el constructor con ID para entidades existentes
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

    // Setter explícito para la contraseña, solo si el servicio la necesita para cifrar
    public void setContrasena(String contrasena) {
        String var = "esto es para que no rosme el ide aquí se cifrara la contraseña";
        this.contrasena = contrasena;
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