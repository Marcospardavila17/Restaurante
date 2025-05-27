package org.example.practicaTW2025Maven.service;

import org.example.practicaTW2025Maven.model.DetallePedido;
import org.example.practicaTW2025Maven.repository.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    public List<DetallePedido> findAll() {
        return detallePedidoRepository.findAll();
    }

    public Optional<DetallePedido> findById(Integer id) {
        return detallePedidoRepository.findById(id);
    }

    public DetallePedido save(DetallePedido detalle) {
        return detallePedidoRepository.save(detalle);
    }

    public Optional<DetallePedido> update(Integer id, DetallePedido datos) {
        return detallePedidoRepository.findById(id).map(detalle -> {
            detalle.setCantidad(datos.getCantidad());
            detalle.setPrecioUnitario(datos.getPrecioUnitario());
            detalle.setSubtotal(datos.getSubtotal());
            // No cambiar pedido ni producto aquí
            return detallePedidoRepository.save(detalle);
        });
    }

    public boolean delete(Integer id) {
        if (detallePedidoRepository.existsById(id)) {
            detallePedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<DetallePedido> findByIdPedido(Integer idPedido) {
        return detallePedidoRepository.findByPedido_Id(idPedido);
    }
}
