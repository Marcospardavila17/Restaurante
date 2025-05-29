package org.marcospardavila.practicaTW2025Maven.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy; // Importación para sesiones sin estado
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Importación para añadir el filtro JWT

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Necesitamos inyectar estos servicios para poder usarlos en la configuración
    private final CustomUserDetailsService customUserDetailsService;

    // Inyectar CustomUserDetailsService a través del constructor
    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    // Bean para nuestro filtro JWT personalizado
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        // Asegúrate de que JwtAuthenticationFilter tiene un constructor adecuado
        return new JwtAuthenticationFilter(jwtTokenProvider(), customUserDetailsService);
    }

    // Bean para exponer JwtTokenProvider (si no lo tienes ya como @Component)
    // Pero ya lo tienes como @Component en tu archivo, así que no es estrictamente necesario aquí.
    // Solo si necesitas inyectarlo en otro bean de configuración.
    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(); // Spring manejará la inyección de @Value
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configura sesiones sin estado
                .authorizeHttpRequests(authz -> authz
                        // Permitir acceso a los endpoints de autenticación
                        .requestMatchers("/api/auth/**").permitAll() // <-- ¡IMPORTANTE! Sólo los de autenticación
                        .requestMatchers("/api/usuarios/**").permitAll()
                        // Permitir acceso a la documentación de Swagger
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/admin/reload-data").permitAll()
                        // Aquí puedes añadir reglas de autorización por rol, si las necesitas
                        // .requestMatchers("/api/usuarios/**").hasRole("ADMINISTRADOR") // Ejemplo: Solo administradores pueden gestionar usuarios
                        // Todas las demás solicitudes requieren autenticación
                        .anyRequest().authenticated()
                )
                // Añade nuestro filtro JWT personalizado en la cadena de filtros de Spring Security.
                // Se ejecuta ANTES del filtro UsernamePasswordAuthenticationFilter de Spring.
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

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