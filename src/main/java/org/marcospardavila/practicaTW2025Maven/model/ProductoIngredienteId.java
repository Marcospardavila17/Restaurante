package org.marcospardavila.practicaTW2025Maven.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Clave compuesta para ProductoIngrediente: (id_producto, id_ingrediente)
 */
@Getter
@NoArgsConstructor // Necesario para JPA
@EqualsAndHashCode // Imprescindible para claves compuestas
@Embeddable
public class ProductoIngredienteId implements Serializable {

    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(name = "id_ingrediente")
    private Integer idIngrediente;

    public ProductoIngredienteId(Integer idProducto, Integer idIngrediente) {
        this.idProducto = idProducto;
        this.idIngrediente = idIngrediente;
    }
}
