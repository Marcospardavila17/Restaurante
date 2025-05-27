package org.example.practicaTW2025Maven.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(nullable = false, length = 20)
    private String tipo;

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
    private String contrasena;

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

    // Builder seguro: sin 'id'
    @Builder
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
}
