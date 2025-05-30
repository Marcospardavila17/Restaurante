package org.marcospardavila.practicaTW2025Maven.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.beans.factory.annotation.Autowired; // Ya no es necesario aquí

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    // private final JwtAuthenticationFilter jwtAuthenticationFilter; // ¡Eliminar esta inyección del constructor!

    // Constructor sin JwtAuthenticationFilter
    // Spring inyectará CustomUserDetailsService automáticamente si es el único constructor o si se usa @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
        // this.jwtAuthenticationFilter = jwtAuthenticationFilter; // Eliminar esta asignación
    }

    // Este bean es correcto, Spring inyectará JwtTokenProvider y CustomUserDetailsService aquí
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService) {
        return new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        // Rutas públicas (no requieren autenticación)
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/admin/reload-data").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/ingredientes/**").permitAll()

                        // Rutas protegidas por rol (requieren autenticación y rol específico)
                        // Gestión de usuarios (solo ADMINISTRADOR)
                        .requestMatchers("/api/usuarios/**").hasRole("ADMINISTRADOR")

                        // Gestión de productos (PERSONAL y ADMINISTRADOR)
                        .requestMatchers("/api/productos/**").hasAnyRole("PERSONAL", "ADMINISTRADOR")

                        // Gestión de ingredientes (PERSONAL y ADMINISTRADOR)
                        .requestMatchers("/api/ingredientes/**").hasAnyRole("PERSONAL", "ADMINISTRADOR")

                        // Gestión de pedidos
                        // Clientes pueden crear pedidos (POST)
                        .requestMatchers(HttpMethod.POST, "/api/pedidos").hasRole("CLIENTE")
                        // Clientes pueden ver sus propios pedidos (GET)
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/cliente/**").hasRole("CLIENTE")
                        // Personal y Administradores gestionan todos los pedidos (CRUD)
                        .requestMatchers("/api/pedidos/**").hasAnyRole("PERSONAL", "ADMINISTRADOR")
                        .requestMatchers("/api/detallepedidos/**").hasAnyRole("PERSONAL", "ADMINISTRADOR")
                        .requestMatchers("/api/personalizaciones/**").hasAnyRole("PERSONAL", "ADMINISTRADOR")

                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )
                // Spring resolverá las dependencias de jwtAuthenticationFilter() automáticamente.
                .addFilterBefore(jwtAuthenticationFilter(null, null), UsernamePasswordAuthenticationFilter.class); // <-- ¡CORRECCIÓN CLAVE AQUÍ!

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
