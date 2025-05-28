package org.marcospardavila.practicaTW2025Maven.repository;

import org.marcospardavila.practicaTW2025Maven.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {
    List<DetallePedido> findByPedido_Id(Integer idPedido);
}
