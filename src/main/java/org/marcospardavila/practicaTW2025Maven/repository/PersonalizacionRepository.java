package org.marcospardavila.practicaTW2025Maven.repository;

import org.marcospardavila.practicaTW2025Maven.model.Personalizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PersonalizacionRepository extends JpaRepository<Personalizacion, Integer> {
    List<Personalizacion> findByDetallePedido_Id(Integer idDetallePedido);
}
