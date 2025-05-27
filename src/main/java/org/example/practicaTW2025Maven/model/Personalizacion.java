package org.example.practicaTW2025Maven.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "PERSONALIZACION")
public class Personalizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_detalle_pedido", nullable = false)
    private DetallePedido detallePedido;

    @Setter
    @Column(nullable = false, length = 100)
    private String nombre;

    @Setter
    @Column(length = 255)
    private String descripcion;

    // Builder seguro: sin 'id'
    @Builder
    public Personalizacion(DetallePedido detallePedido, String nombre, String descripcion) {
        this.detallePedido = detallePedido;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
}
