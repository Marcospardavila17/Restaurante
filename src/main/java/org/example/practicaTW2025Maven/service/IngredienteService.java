package org.example.practicaTW2025Maven.service;

import org.example.practicaTW2025Maven.model.Ingrediente;
import org.example.practicaTW2025Maven.repository.IngredienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredienteService {

    @Autowired
    private IngredienteRepository ingredienteRepository;

    public List<Ingrediente> findAll() {
        return ingredienteRepository.findAll();
    }

    public Optional<Ingrediente> findById(Integer id) {
        return ingredienteRepository.findById(id);
    }

    public Ingrediente save(Ingrediente ingrediente) {
        return ingredienteRepository.save(ingrediente);
    }

    public Optional<Ingrediente> update(Integer id, Ingrediente datos) {
        return ingredienteRepository.findById(id).map(ing -> {
            ing.setNombre(datos.getNombre());
            ing.setCantidad(datos.getCantidad());
            ing.setUnidad(datos.getUnidad());
            return ingredienteRepository.save(ing);
        });
    }

    public boolean delete(Integer id) {
        if (ingredienteRepository.existsById(id)) {
            ingredienteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
