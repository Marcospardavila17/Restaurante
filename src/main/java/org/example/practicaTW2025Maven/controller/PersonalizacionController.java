package org.example.practicaTW2025Maven.controller;

import org.example.practicaTW2025Maven.model.Personalizacion;
import org.example.practicaTW2025Maven.service.PersonalizacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/personalizaciones")
public class PersonalizacionController {

    private final PersonalizacionService personalizacionService;

    public PersonalizacionController(PersonalizacionService personalizacionService) {
        this.personalizacionService = personalizacionService;
    }

    @GetMapping
    public List<Personalizacion> getAllPersonalizaciones() {
        return personalizacionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Personalizacion> getPersonalizacionById(@PathVariable Integer id) {
        Optional<Personalizacion> personalizacion = personalizacionService.findById(id);
        return personalizacion.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Personalizacion createPersonalizacion(@RequestBody Personalizacion personalizacion) {
        return personalizacionService.save(personalizacion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Personalizacion> updatePersonalizacion(@PathVariable Integer id, @RequestBody Personalizacion personalizacionDetails) {
        Optional<Personalizacion> updated = personalizacionService.update(id, personalizacionDetails);
        return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonalizacion(@PathVariable Integer id) {
        if (personalizacionService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Personalizaciones por detalle de pedido
    @GetMapping("/detalle/{idDetallePedido}")
    public List<Personalizacion> getPersonalizacionesByDetalle(@PathVariable Integer idDetallePedido) {
        return personalizacionService.findByIdDetallePedido(idDetallePedido);
    }
}
