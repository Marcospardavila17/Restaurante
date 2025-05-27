package org.example.practicaTW2025Maven.controller;

import org.example.practicaTW2025Maven.model.DetallePedido;
import org.example.practicaTW2025Maven.service.DetallePedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/detalles-pedido")
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    @GetMapping
    public List<DetallePedido> getAllDetalles() {
        return detallePedidoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetallePedido> getDetalleById(@PathVariable Integer id) {
        Optional<DetallePedido> detalle = detallePedidoService.findById(id);
        return detalle.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public DetallePedido createDetalle(@RequestBody DetallePedido detalle) {
        return detallePedidoService.save(detalle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetallePedido> updateDetalle(@PathVariable Integer id, @RequestBody DetallePedido detalleDetails) {
        Optional<DetallePedido> updated = detallePedidoService.update(id, detalleDetails);
        return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetalle(@PathVariable Integer id) {
        if (detallePedidoService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Detalles por pedido
    @GetMapping("/pedido/{idPedido}")
    public List<DetallePedido> getDetallesByPedido(@PathVariable Integer idPedido) {
        return detallePedidoService.findByIdPedido(idPedido);
    }
}
