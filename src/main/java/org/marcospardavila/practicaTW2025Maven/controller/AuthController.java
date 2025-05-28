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
import org.springframework.security.crypto.password.PasswordEncoder; // <-- Asegúrate de importar esto
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder; // <-- Inyecta PasswordEncoder aquí

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder; // <-- Inicialízalo aquí
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getContrasena()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);
            // CORRECCIÓN: Pasar ambos argumentos al constructor
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, "Bearer"));
        } catch (Exception e) {
            // Manejo de errores de autenticación
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        // Comprobar si el email ya está en uso
        if (usuarioService.findByEmail(registerRequest.getEmail()).isPresent()) {
            return new ResponseEntity<>("El email ya está registrado.", HttpStatus.BAD_REQUEST);
        }

        // Cifrar la contraseña antes de guardar el usuario
        // Asegúrate de que tu UsuarioService.save() maneje el cifrado o hazlo aquí.
        // Si UsuarioService.save() ya lo cifra, simplemente pasa la contraseña sin cifrar.
        // Considerando que tu UsuarioService tiene un método 'save' que guarda el Usuario
        // y que tu PasswordEncoder está disponible.

        // Crear una instancia de Usuario a partir del RegisterRequest
        Usuario nuevoUsuario = Usuario.builder()
                .tipo(registerRequest.getTipo()) // Asegúrate de que el 'tipo' venga en el RegisterRequest
                .nombre(registerRequest.getNombre())
                .apellidos(registerRequest.getApellidos())
                .email(registerRequest.getEmail())
                .contrasena(passwordEncoder.encode(registerRequest.getContrasena())) // Cifrar la contraseña aquí
                .direccion(registerRequest.getDireccion())
                .poblacion(registerRequest.getPoblacion())
                .provincia(registerRequest.getProvincia())
                .codigoPostal(registerRequest.getCodigoPostal())
                .telefono(registerRequest.getTelefono())
                .numeroTarjetaCredito(registerRequest.getNumeroTarjetaCredito())
                .build();

        // Guarda el nuevo usuario
        Usuario usuarioGuardado = usuarioService.save(nuevoUsuario);

        // Autenticar automáticamente al usuario recién registrado y devolver un JWT
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getEmail(),
                        registerRequest.getContrasena() // Usamos la contraseña sin cifrar para la autenticación inicial
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        // CORRECCIÓN: Pasar ambos argumentos al constructor
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, "Bearer"));
    }
}