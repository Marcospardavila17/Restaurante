package org.marcospardavila.practicaTW2025Maven.service;

import org.marcospardavila.practicaTW2025Maven.model.Pedido;
import org.marcospardavila.practicaTW2025Maven.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> findById(Integer id) {
        return pedidoRepository.findById(id);
    }

    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public Optional<Pedido> update(Integer id, Pedido datos) {
        return pedidoRepository.findById(id).map(pedido -> {
            pedido.setEstado(datos.getEstado());
            pedido.setTotal(datos.getTotal());
            // No cambiar usuario ni fecha aquí
            return pedidoRepository.save(pedido);
        });
    }

    public boolean delete(Integer id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Pedido> findByIdUsuario(Integer idUsuario) {
        return pedidoRepository.findByUsuario_Id(idUsuario);
    }

    public List<Pedido> findByEstado(String estado) {
        return pedidoRepository.findByEstado(estado);
    }
}
