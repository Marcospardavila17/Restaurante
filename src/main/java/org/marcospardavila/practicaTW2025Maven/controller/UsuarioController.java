package org.marcospardavila.practicaTW2025Maven.controller;

import org.marcospardavila.practicaTW2025Maven.model.Usuario;
import org.marcospardavila.practicaTW2025Maven.payload.UpdateProfileRequest;
import org.marcospardavila.practicaTW2025Maven.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ MANTÉN TUS MÉTODOS EXISTENTES (para administradores)
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Integer id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        return usuario.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        if (usuarioService.findByEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        Usuario creado = usuarioService.save(usuario);
        return ResponseEntity.ok(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Integer id, @RequestBody Usuario usuarioDetails) {
        Optional<Usuario> updated = usuarioService.update(id, usuarioDetails);
        return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        if (usuarioService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ AÑADE ESTOS MÉTODOS PARA EL PERFIL DEL USUARIO AUTENTICADO
    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerPerfil(Authentication authentication) {
        try {
            String email = authentication.getName();
            Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Usuario usuario = usuarioOpt.get();
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Error al obtener el perfil"));
        }
    }

    @PutMapping("/perfil")
    public ResponseEntity<?> actualizarPerfil(@RequestBody UpdateProfileRequest request,
                                              Authentication authentication) {
        try {
            String email = authentication.getName();
            Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Usuario usuario = usuarioOpt.get();

            // Verificar contraseña actual si se quiere cambiar
            if (request.getContrasenaNueva() != null && !request.getContrasenaNueva().isEmpty()) {
                if (request.getContrasenaActual() == null ||
                        !passwordEncoder.matches(request.getContrasenaActual(), usuario.getContrasena())) {
                    return ResponseEntity.badRequest()
                            .body(Collections.singletonMap("message", "La contraseña actual es incorrecta"));
                }
                usuario.setContrasena(passwordEncoder.encode(request.getContrasenaNueva()));
            }

            // Actualizar datos
            usuario.setNombre(request.getNombre());
            usuario.setApellidos(request.getApellidos());
            usuario.setDireccion(request.getDireccion());
            usuario.setPoblacion(request.getPoblacion());
            usuario.setProvincia(request.getProvincia());
            usuario.setCodigoPostal(request.getCodigoPostal());
            usuario.setTelefono(request.getTelefono());
            usuario.setNumeroTarjetaCredito(request.getNumeroTarjetaCredito());

            Usuario usuarioActualizado = usuarioService.save(usuario);

            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Error al actualizar el perfil"));
        }
    }
}
