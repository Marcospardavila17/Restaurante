package org.marcospardavila.practicaTW2025Maven.repository;

import org.marcospardavila.practicaTW2025Maven.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByCategoria(String categoria);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}
