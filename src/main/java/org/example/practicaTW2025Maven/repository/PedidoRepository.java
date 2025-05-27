package org.example.practicaTW2025Maven.repository;

import org.example.practicaTW2025Maven.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByUsuario_Id(Integer idUsuario);
    List<Pedido> findByEstado(String estado);
}
