package org.example.practicaTW2025Maven.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "PRODUCTO")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(nullable = false, length = 100)
    private String nombre;

    @Setter
    @Column(length = 255)
    private String descripcion;

    @Setter
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Setter
    @Column(nullable = false, length = 50)
    private String categoria;

    @Setter
    @Column(length = 255)
    private String imagen;

    @Setter
    @Column(length = 255)
    private String ingredientes;

    @Setter
    @Column(length = 255)
    private String alergenos;

    @Setter
    @Column(name = "tiempo_preparacion")
    private Integer tiempoPreparacion;

    @Builder
    public Producto(String nombre, String descripcion, BigDecimal precio, String categoria,
                    String imagen, String ingredientes, String alergenos, Integer tiempoPreparacion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.imagen = imagen;
        this.ingredientes = ingredientes;
        this.alergenos = alergenos;
        this.tiempoPreparacion = tiempoPreparacion;
    }
}
