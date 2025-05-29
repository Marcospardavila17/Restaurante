package org.marcospardavila.practicaTW2025Maven.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Importar HttpMethod para especificar métodos HTTP
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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider(), customUserDetailsService);
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        // Permitir acceso a los endpoints de autenticación (registro y login)
                        .requestMatchers("/api/auth/**").permitAll()
                        // Permitir acceso a la documentación de Swagger
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/admin/reload-data").permitAll()

                        // === REGLAS DE ACCESO A LA CARTA (PÚBLICAS) ===
                        // Cualquiera puede ver productos (GET)
                        .requestMatchers(HttpMethod.GET, "/api/productos", "/api/productos/**").permitAll()
                        // Cualquiera puede ver ingredientes (GET)
                        .requestMatchers(HttpMethod.GET, "/api/ingredientes", "/api/ingredientes/**").permitAll()

                        // === REGLAS DE AUTORIZACIÓN BASADAS EN ROLES (EJEMPLOS) ===
                        // USUARIOS: Solo los administradores pueden gestionar usuarios (CRUD completo)
                        .requestMatchers("/api/usuarios/**").hasRole("ADMINISTRADOR") // Simplificado para todos los métodos

                        // PRODUCTOS: Solo los administradores o personal pueden añadir/modificar/eliminar productos
                        .requestMatchers(HttpMethod.POST, "/api/productos").hasAnyRole("ADMINISTRADOR", "PERSONAL")
                        .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasAnyRole("ADMINISTRADOR", "PERSONAL")
                        .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasAnyRole("ADMINISTRADOR", "PERSONAL")
                        // INGREDIENTES: Solo los administradores o personal pueden gestionar ingredientes
                        .requestMatchers("/api/ingredientes/**").hasAnyRole("ADMINISTRADOR", "PERSONAL") // CRUD para POST, PUT, DELETE

                        // PEDIDOS:
                        // Clientes pueden crear pedidos
                        .requestMatchers(HttpMethod.POST, "/api/pedidos").hasRole("CLIENTE")
                        // Clientes pueden ver SUS propios pedidos (la lógica de "sus propios" debe estar en el controlador)
                        // Para este ejemplo, permitimos a los CLIENTES acceder a la ruta de pedidos de cliente.
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/cliente/**").hasRole("CLIENTE")
                        // Personal y Administradores pueden gestionar todos los pedidos (CRUD completo)
                        .requestMatchers("/api/pedidos/**").hasAnyRole("PERSONAL", "ADMINISTRADOR")
                        .requestMatchers("/api/detallepedidos/**").hasAnyRole("PERSONAL", "ADMINISTRADOR")
                        .requestMatchers("/api/personalizaciones/**").hasAnyRole("PERSONAL", "ADMINISTRADOR")

                        // Cualquier otra solicitud que no haya sido especificada, requiere autenticación
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

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