package org.marcospardavila.practicaTW2025Maven.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "DETALLE_PEDIDO")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @Setter
    private Integer cantidad;

    @Setter
    private BigDecimal precioUnitario;

    @Setter
    private BigDecimal subtotal;

    // Builder seguro: sin 'id'
    @Builder
    public DetallePedido(Pedido pedido, Producto producto, Integer cantidad,
                         BigDecimal precioUnitario, BigDecimal subtotal) {
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }
}
