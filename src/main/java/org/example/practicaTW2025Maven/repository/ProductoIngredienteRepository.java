package org.example.practicaTW2025Maven.repository;

import org.example.practicaTW2025Maven.model.ProductoIngrediente;
import org.example.practicaTW2025Maven.model.ProductoIngredienteId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoIngredienteRepository extends JpaRepository<ProductoIngrediente, ProductoIngredienteId> {
    List<ProductoIngrediente> findByProducto_Id(Integer idProducto);
    List<ProductoIngrediente> findByIngrediente_Id(Integer idIngrediente);
}
