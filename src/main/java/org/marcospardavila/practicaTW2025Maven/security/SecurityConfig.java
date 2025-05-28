package org.marcospardavila.practicaTW2025Maven.security; // Mantenemos tu paquete original para SecurityConfig

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager; // Nueva importación
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // Nueva importación
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Nueva importación
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy; // Nueva importación para sesiones sin estado
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Nueva importación para añadir el filtro JWT

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Necesitamos inyectar estos servicios para poder usarlos en la configuración
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    // Inyección de dependencias por constructor
    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Este bean se encarga de crear y exponer nuestro JwtAuthenticationFilter.
    // Spring inyectará automáticamente sus dependencias (JwtTokenProvider y CustomUserDetailsService)
    // gracias al constructor que definimos en JwtAuthenticationFilter.java.
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilita la protección CSRF (Cross-Site Request Forgery)
                // Es esencial deshabilitarlo para APIs RESTful que usan JWT
                .csrf(AbstractHttpConfigurer::disable)
                // Configura la gestión de sesiones para que sea SIN ESTADO (stateless).
                // Esto significa que Spring Security no creará ni usará sesiones HTTP para JWT.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configura las reglas de autorización para las diferentes rutas HTTP
                .authorizeHttpRequests(authz -> authz
                        // Permite acceso público a los endpoints de autenticación (ej. /api/auth/login)
                        .requestMatchers("/api/auth/**").permitAll() // NUEVA LÍNEA: para el login/registro
                        // Mantenemos tus reglas existentes para /api/**, /test/** y Swagger UI
                        .requestMatchers("/api/**", "/test/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/admin/reload-data").permitAll()
                        // Cualquier otra solicitud debe ser autenticada
                        .anyRequest().authenticated()
                );

        // Añade nuestro filtro JWT personalizado en la cadena de filtros de Spring Security.
        // Se ejecuta ANTES del filtro UsernamePasswordAuthenticationFilter de Spring.
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para exponer el AuthenticationManager, que se usará para autenticar a los usuarios
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Bean para configurar el DaoAuthenticationProvider.
    // Este proveedor es el que sabe cómo cargar los detalles del usuario (usando CustomUserDetailsService)
    // y cómo comparar las contraseñas (usando PasswordEncoder).
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService); // Usa nuestro CustomUserDetailsService
        authProvider.setPasswordEncoder(passwordEncoder()); // Usa nuestro PasswordEncoder
        return authProvider;
    }
}