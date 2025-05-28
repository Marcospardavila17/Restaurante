package org.marcospardavila.practicaTW2025Maven.controller;

import org.marcospardavila.practicaTW2025Maven.model.Producto;
import org.marcospardavila.practicaTW2025Maven.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> getAllProductos() {
        return productoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Integer id) {
        Optional<Producto> producto = productoService.findById(id);
        return producto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Producto createProducto(@RequestBody Producto producto) {
        return productoService.save(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Integer id, @RequestBody Producto productoDetails) {
        Optional<Producto> updated = productoService.update(id, productoDetails);
        return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Integer id) {
        if (productoService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Búsqueda por categoría
    @GetMapping("/categoria/{categoria}")
    public List<Producto> getProductosByCategoria(@PathVariable String categoria) {
        return productoService.findByCategoria(categoria);
    }

    // Búsqueda por nombre (parcial)
    @GetMapping("/buscar")
    public List<Producto> buscarPorNombre(@RequestParam String nombre) {
        return productoService.findByNombre(nombre);
    }
}
