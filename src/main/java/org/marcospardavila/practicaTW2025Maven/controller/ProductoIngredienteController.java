package org.marcospardavila.practicaTW2025Maven.controller;

import org.marcospardavila.practicaTW2025Maven.model.ProductoIngrediente;
import org.marcospardavila.practicaTW2025Maven.model.ProductoIngredienteId;
import org.marcospardavila.practicaTW2025Maven.service.ProductoIngredienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/producto-ingrediente")
public class ProductoIngredienteController {

    private final ProductoIngredienteService productoIngredienteService;

    public ProductoIngredienteController(ProductoIngredienteService productoIngredienteService) {
        this.productoIngredienteService = productoIngredienteService;
    }

    @GetMapping
    public List<ProductoIngrediente> getAll() {
        return productoIngredienteService.findAll();
    }

    @GetMapping("/{idProducto}/{idIngrediente}")
    public ResponseEntity<ProductoIngrediente> getById(@PathVariable Integer idProducto, @PathVariable Integer idIngrediente) {
        ProductoIngredienteId id = new ProductoIngredienteId(idProducto, idIngrediente);
        Optional<ProductoIngrediente> pi = productoIngredienteService.findById(id);
        return pi.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ProductoIngrediente create(@RequestBody ProductoIngrediente pi) {
        return productoIngredienteService.save(pi);
    }

    @DeleteMapping("/{idProducto}/{idIngrediente}")
    public ResponseEntity<Void> delete(@PathVariable Integer idProducto, @PathVariable Integer idIngrediente) {
        ProductoIngredienteId id = new ProductoIngredienteId(idProducto, idIngrediente);
        if (productoIngredienteService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Ingredientes de un producto
    @GetMapping("/producto/{idProducto}")
    public List<ProductoIngrediente> getByProducto(@PathVariable Integer idProducto) {
        return productoIngredienteService.findByIdProducto(idProducto);
    }

    // Productos que usan un ingrediente
    @GetMapping("/ingrediente/{idIngrediente}")
    public List<ProductoIngrediente> getByIngrediente(@PathVariable Integer idIngrediente) {
        return productoIngredienteService.findByIdIngrediente(idIngrediente);
    }
}
