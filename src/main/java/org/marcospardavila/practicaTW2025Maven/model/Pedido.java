package org.marcospardavila.practicaTW2025Maven.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "PEDIDO")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Setter
    @Column(nullable = false)
    private LocalDateTime fecha;

    @Setter
    @Column(nullable = false, length = 20)
    private String estado;

    @Setter
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Builder
    public Pedido(Usuario usuario, LocalDateTime fecha, String estado, BigDecimal total) {
        this.usuario = usuario;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
    }
}
