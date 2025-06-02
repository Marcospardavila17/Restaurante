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
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Listar todos los usuarios
    public List<Usuario> findAll() {
        System.out.println("UsuarioService Listando todos los usuarios");
        return usuarioRepository.findAll();
    }

    // Buscar usuario por ID
    public Optional<Usuario> findById(Integer id) {
        System.out.println("UsuarioService Buscando usuario por ID: " + id);
        return usuarioRepository.findById(id);
    }

    /**
     * Guarda (crea o actualiza) un usuario.
     * Cifra la contraseña si no está ya cifrada (útil para nuevos registros o cambios de contraseña).
     * @param usuario El objeto Usuario a guardar.
     * @return El usuario guardado.
     */
    public Usuario save(Usuario usuario) {
        System.out.println("UsuarioService Guardando usuario: " + usuario);
        // Cifra la contraseña solo si no está ya cifrada (los hashes BCrypt empiezan con $2a$ o $2b$)
        if (usuario.getContrasena() != null && !usuario.getContrasena().startsWith("$2a$") && !usuario.getContrasena().startsWith("$2b$")) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> update(Integer id, Usuario datos) {
        System.out.println("UsuarioService Actualizando usuario: " + datos);
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(datos.getNombre());
            usuario.setApellidos(datos.getApellidos());
            usuario.setDireccion(datos.getDireccion());
            usuario.setPoblacion(datos.getPoblacion());
            usuario.setProvincia(datos.getProvincia());
            usuario.setCodigoPostal(datos.getCodigoPostal());
            usuario.setTelefono(datos.getTelefono());
            usuario.setNumeroTarjetaCredito(datos.getNumeroTarjetaCredito());

            if (datos.getContrasena() != null && !datos.getContrasena().isEmpty()) {
                usuario.setContrasena(datos.getContrasena());
            }

            return this.save(usuario);
        });
    }


    // Eliminar usuario
    public boolean delete(Integer id) {
        System.out.println("UsuarioService Eliminando usuario con ID: " + id);
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Buscar usuario por email (para login y registro)
    public Optional<Usuario> findByEmail(String email) {
        System.out.println("UsuarioService Buscando usuario por email: " + email);
        return usuarioRepository.findByEmail(email);
    }
}