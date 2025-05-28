package org.marcospardavila.practicaTW2025Maven.service;

import org.marcospardavila.practicaTW2025Maven.model.Producto;
import org.marcospardavila.practicaTW2025Maven.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Optional<Producto> findById(Integer id) {
        return productoRepository.findById(id);
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public Optional<Producto> update(Integer id, Producto datos) {
        return productoRepository.findById(id).map(producto -> {
            producto.setNombre(datos.getNombre());
            producto.setDescripcion(datos.getDescripcion());
            producto.setPrecio(datos.getPrecio());
            producto.setCategoria(datos.getCategoria());
            producto.setImagen(datos.getImagen());
            producto.setIngredientes(datos.getIngredientes());
            producto.setAlergenos(datos.getAlergenos());
            producto.setTiempoPreparacion(datos.getTiempoPreparacion());
            return productoRepository.save(producto);
        });
    }

    public boolean delete(Integer id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Producto> findByCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    public List<Producto> findByNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
