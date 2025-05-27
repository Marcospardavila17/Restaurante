package org.example.practicaTW2025Maven.service;

import org.example.practicaTW2025Maven.model.Personalizacion;
import org.example.practicaTW2025Maven.repository.PersonalizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonalizacionService {

    private final PersonalizacionRepository personalizacionRepository;

    public PersonalizacionService(PersonalizacionRepository personalizacionRepository) {
        this.personalizacionRepository = personalizacionRepository;
    }

    public List<Personalizacion> findAll() {
        return personalizacionRepository.findAll();
    }

    public Optional<Personalizacion> findById(Integer id) {
        return personalizacionRepository.findById(id);
    }

    public Personalizacion save(Personalizacion personalizacion) {
        return personalizacionRepository.save(personalizacion);
    }

    public Optional<Personalizacion> update(Integer id, Personalizacion datos) {
        return personalizacionRepository.findById(id).map(pers -> {
            pers.setNombre(datos.getNombre());
            pers.setDescripcion(datos.getDescripcion());
            // No cambiar idDetallePedido aquí
            return personalizacionRepository.save(pers);
        });
    }

    public boolean delete(Integer id) {
        if (personalizacionRepository.existsById(id)) {
            personalizacionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Personalizacion> findByIdDetallePedido(Integer idDetallePedido) {
        return personalizacionRepository.findByDetallePedido_Id(idDetallePedido);
    }
}
