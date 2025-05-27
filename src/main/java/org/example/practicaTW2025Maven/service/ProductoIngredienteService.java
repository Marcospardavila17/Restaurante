package org.example.practicaTW2025Maven.service;

import org.example.practicaTW2025Maven.model.ProductoIngrediente;
import org.example.practicaTW2025Maven.model.ProductoIngredienteId;
import org.example.practicaTW2025Maven.repository.ProductoIngredienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoIngredienteService {

    private final ProductoIngredienteRepository productoIngredienteRepository;

    public ProductoIngredienteService(ProductoIngredienteRepository productoIngredienteRepository) {
        this.productoIngredienteRepository = productoIngredienteRepository;
    }

    public List<ProductoIngrediente> findAll() {
        return productoIngredienteRepository.findAll();
    }

    public Optional<ProductoIngrediente> findById(ProductoIngredienteId id) {
        return productoIngredienteRepository.findById(id);
    }

    public ProductoIngrediente save(ProductoIngrediente pi) {
        return productoIngredienteRepository.save(pi);
    }

    public boolean delete(ProductoIngredienteId id) {
        if (productoIngredienteRepository.existsById(id)) {
            productoIngredienteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<ProductoIngrediente> findByIdProducto(Integer idProducto) {
        return productoIngredienteRepository.findByProducto_Id(idProducto);
    }

    public List<ProductoIngrediente> findByIdIngrediente(Integer idIngrediente) {
        return productoIngredienteRepository.findByIngrediente_Id(idIngrediente);
    }
}
