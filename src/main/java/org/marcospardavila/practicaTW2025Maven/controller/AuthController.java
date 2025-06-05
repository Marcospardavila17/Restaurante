package org.marcospardavila.practicaTW2025Maven.controller;

import org.marcospardavila.practicaTW2025Maven.model.Usuario;
import org.marcospardavila.practicaTW2025Maven.payload.JwtAuthenticationResponse;
import org.marcospardavila.practicaTW2025Maven.payload.LoginRequest;
import org.marcospardavila.practicaTW2025Maven.payload.RegisterRequest;
import org.marcospardavila.practicaTW2025Maven.security.JwtTokenProvider;
import org.marcospardavila.practicaTW2025Maven.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          UsuarioService usuarioService,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getContrasena()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            Optional<Usuario> usuarioOpt = usuarioService.findByEmail(loginRequest.getEmail());
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("message", "Error interno: usuario no encontrado después de autenticación exitosa."));
            }

            Usuario usuario = usuarioOpt.get();

            // ✅ AÑADIR URL DE REDIRECCIÓN SEGÚN TIPO DE USUARIO
            String redirectUrl = "cliente/menu"; // Por defecto al menú
            if ("Cliente".equals(usuario.getTipo())) {
                redirectUrl = "cliente/menu";
            } else if ("Personal".equals(usuario.getTipo())) {
                redirectUrl = "cliente/menu";
            } else if ("Administrador".equals(usuario.getTipo())) {
                redirectUrl = "cliente/menu";
            }

            return ResponseEntity.ok(new JwtAuthenticationResponse(
                    jwt,
                    "Bearer",
                    usuario.getTipo(),
                    usuario.getNombre(),
                    usuario.getId(),
                    usuario.getEmail()
            ));

        } catch (Exception e) {
            System.err.println("Error durante la autenticación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "Credenciales inválidas."));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (usuarioService.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "El email ya está registrado."));
        }

        Usuario nuevoUsuario = Usuario.usuarioBuilder()
                .tipo("Cliente")
                .nombre(registerRequest.getNombre())
                .apellidos(registerRequest.getApellidos())
                .email(registerRequest.getEmail())
                .contrasena(passwordEncoder.encode(registerRequest.getContrasena()))
                .direccion(registerRequest.getDireccion())
                .poblacion(registerRequest.getPoblacion())
                .provincia(registerRequest.getProvincia())
                .codigoPostal(registerRequest.getCodigoPostal())
                .telefono(registerRequest.getTelefono())
                .numeroTarjetaCredito(registerRequest.getNumeroTarjetaCredito())
                .build();

        Usuario savedUser = usuarioService.save(nuevoUsuario);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getEmail(),
                        registerRequest.getContrasena()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new JwtAuthenticationResponse(
                        jwt,
                        "Bearer",
                        savedUser.getTipo(),
                        savedUser.getNombre(),
                        savedUser.getId(),
                        savedUser.getEmail()
                ));
    }

    @GetMapping("/check-admin")
    public ResponseEntity<?> checkAdmin(Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"))) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
