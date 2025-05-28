package org.marcospardavila.practicaTW2025Maven.controller;

import org.marcospardavila.practicaTW2025Maven.model.Pedido;
import org.marcospardavila.practicaTW2025Maven.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Integer id) {
        Optional<Pedido> pedido = pedidoService.findById(id);
        return pedido.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Pedido createPedido(@RequestBody Pedido pedido) {
        return pedidoService.save(pedido);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> updatePedido(@PathVariable Integer id, @RequestBody Pedido pedidoDetails) {
        Optional<Pedido> updated = pedidoService.update(id, pedidoDetails);
        return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Integer id) {
        if (pedidoService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Pedidos por usuario
    @GetMapping("/usuario/{idUsuario}")
    public List<Pedido> getPedidosByUsuario(@PathVariable Integer idUsuario) {
        return pedidoService.findByIdUsuario(idUsuario);
    }

    // Pedidos por estado
    @GetMapping("/estado/{estado}")
    public List<Pedido> getPedidosByEstado(@PathVariable String estado) {
        return pedidoService.findByEstado(estado);
    }
}
