package org.marcospardavila.practicaTW2025Maven.service;

import org.marcospardavila.practicaTW2025Maven.model.Usuario;
import org.marcospardavila.practicaTW2025Maven.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Nueva importación
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // Inyectar PasswordEncoder

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) { // Modificar constructor
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder; // Asignar
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
    // ¡IMPORTANTE! CIFRA LA CONTRASEÑA ANTES DE GUARDARLA
    public Usuario save(Usuario usuario) {
        // Solo cifra si la contraseña no está ya cifrada o si es un nuevo usuario
        // Una forma simple es asumir que si no está encriptada, es una contraseña nueva.
        // En una app real, podrías tener un métodoo register() separado o un campo booleano
        // en el DTO para indicar si la contraseña necesita ser cifrada.
        if (usuario.getContrasena() != null && !usuario.getContrasena().startsWith("$2a$") && !usuario.getContrasena().startsWith("$2b$")) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }
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
            // Si la contraseña se actualiza, DEBE ser cifrada.
            // Para simplicidad, en este métodoo 'update' no se espera que se cambie la contraseña directamente.
            // Si necesitas cambiar la contraseña, crea un métodoo específico para ello.
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
        return usuarioRepository.findByEmail(email);
    }
}