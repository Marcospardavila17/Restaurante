package org.example.practicaTW2025Maven.service;

import org.example.practicaTW2025Maven.model.Usuario;
import org.example.practicaTW2025Maven.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Listar todos los usuarios
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    // Buscar usuario por ID
    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }

    // Guardar (crear o actualizar) un usuario
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Actualizar datos personales
    public Optional<Usuario> update(Integer id, Usuario datos) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(datos.getNombre());
            usuario.setApellidos(datos.getApellidos());
            usuario.setDireccion(datos.getDireccion());
            usuario.setPoblacion(datos.getPoblacion());
            usuario.setProvincia(datos.getProvincia());
            usuario.setCodigoPostal(datos.getCodigoPostal());
            usuario.setTelefono(datos.getTelefono());
            usuario.setNumeroTarjetaCredito(datos.getNumeroTarjetaCredito());
            // No modificar email ni tipo aquí
            return usuarioRepository.save(usuario);
        });
    }

    // Eliminar usuario
    public boolean delete(Integer id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Buscar usuario por email (para login)
    public Optional<Usuario> findByEmail(String email) {
        // Asegúrate de tener este método en tu repository:
        // Optional<Usuario> findByEmail(String email);
        return usuarioRepository.findByEmail(email);
    }
}
