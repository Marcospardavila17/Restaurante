package org.example.practicaTW2025Maven.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "INGREDIENTE")
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(nullable = false, length = 100)
    private String nombre;

    @Setter
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;

    @Setter
    @Column(length = 20)
    private String unidad;

    // Builder seguro: sin 'id'
    @Builder
    public Ingrediente(String nombre, BigDecimal cantidad, String unidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.unidad = unidad;
    }
}
