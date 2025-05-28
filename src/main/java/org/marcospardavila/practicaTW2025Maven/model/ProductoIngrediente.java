package org.marcospardavila.practicaTW2025Maven.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "PRODUCTO_INGREDIENTE")
public class ProductoIngrediente {

    @EmbeddedId
    private ProductoIngredienteId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idProducto")
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idIngrediente")
    @JoinColumn(name = "id_ingrediente")
    private Ingrediente ingrediente;

    @Column(name = "cantidad_unitaria", nullable = false)
    private BigDecimal cantidadUnitaria;

    public ProductoIngrediente(Producto producto, Ingrediente ingrediente, BigDecimal cantidadUnitaria) {
        this.producto = producto;
        this.ingrediente = ingrediente;
        this.cantidadUnitaria = cantidadUnitaria;
        this.id = new ProductoIngredienteId(
                producto.getId(),
                ingrediente.getId()
        );
    }
}
