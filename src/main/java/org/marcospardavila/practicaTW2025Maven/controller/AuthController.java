package org.marcospardavila.practicaTW2025Maven.controller;

import org.marcospardavila.practicaTW2025Maven.model.Usuario;
import org.marcospardavila.practicaTW2025Maven.payload.JwtAuthenticationResponse;
import org.marcospardavila.practicaTW2025Maven.payload.LoginRequest;
import org.marcospardavila.practicaTW2025Maven.payload.RegisterRequest; // Importa el DTO RegisterRequest
import org.marcospardavila.practicaTW2025Maven.security.JwtTokenProvider;
import org.marcospardavila.practicaTW2025Maven.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Error interno: usuario no encontrado después de autenticación exitosa."));
            }
            Usuario usuario = usuarioOpt.get();

            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, "Bearer", usuario.getTipo(), usuario.getNombre(), usuario.getId(), usuario.getEmail()));

        } catch (Exception e) {
            System.err.println("Error durante la autenticación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Credenciales inválidas."));
        }
    }

    /**
     * Endpoint para el registro de nuevos usuarios (clientes por defecto).
     * Recibe un DTO RegisterRequest, codifica la contraseña y guarda el usuario en la base de datos.
     * @param registerRequest DTO con los datos del nuevo usuario.
     * @return ResponseEntity con un mensaje de éxito o un error 400 si el email ya está registrado.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) { // <-- ¡CAMBIO CRÍTICO AQUÍ!
        // 1. Verificar si el email ya está registrado
        if (usuarioService.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "El email ya está registrado."));
        }

        // 2. Crear un nuevo objeto Usuario usando el builder "usuarioBuilder".
        // Este builder (en Usuario.java) asignará automáticamente el tipo "Cliente".
        Usuario nuevoUsuario = Usuario.usuarioBuilder()
                .tipo("Cliente")
                .nombre(registerRequest.getNombre())
                .apellidos(registerRequest.getApellidos())
                .email(registerRequest.getEmail())
                .contrasena(passwordEncoder.encode(registerRequest.getContrasena())) // Cifra la contraseña aquí
                .direccion(registerRequest.getDireccion())
                .poblacion(registerRequest.getPoblacion())
                .provincia(registerRequest.getProvincia())
                .codigoPostal(registerRequest.getCodigoPostal())
                .telefono(registerRequest.getTelefono())
                .numeroTarjetaCredito(registerRequest.getNumeroTarjetaCredito())
                .build();

        // 3. Guarda el nuevo usuario. El UsuarioService no necesita cifrarla de nuevo.
        Usuario savedUser = usuarioService.save(nuevoUsuario);

        // 4. Autenticar automáticamente al usuario recién registrado y devolver un JWT
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getEmail(),
                        registerRequest.getContrasena() // Usamos la contraseña sin cifrar para la autenticación inicial
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        // 5. Devuelve una respuesta con el token JWT y otros datos relevantes
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new JwtAuthenticationResponse(jwt, "Bearer", savedUser.getTipo(), savedUser.getNombre(), savedUser.getId(), savedUser.getEmail())
        );
    }
}
