package org.example.practicaTW2025Maven.controller;

import org.example.practicaTW2025Maven.model.Ingrediente;
import org.example.practicaTW2025Maven.service.IngredienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ingredientes")
public class IngredienteController {

    private final IngredienteService ingredienteService;

    public IngredienteController(IngredienteService ingredienteService) {
        this.ingredienteService = ingredienteService;
    }

    @GetMapping
    public List<Ingrediente> getAllIngredientes() {
        return ingredienteService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingrediente> getIngredienteById(@PathVariable Integer id) {
        Optional<Ingrediente> ingrediente = ingredienteService.findById(id);
        return ingrediente.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Ingrediente createIngrediente(@RequestBody Ingrediente ingrediente) {
        return ingredienteService.save(ingrediente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ingrediente> updateIngrediente(@PathVariable Integer id, @RequestBody Ingrediente ingredienteDetails) {
        Optional<Ingrediente> updated = ingredienteService.update(id, ingredienteDetails);
        return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngrediente(@PathVariable Integer id) {
        if (ingredienteService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
