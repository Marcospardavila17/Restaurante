package org.marcospardavila.practicaTW2025Maven.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // ✅ IMPORT AÑADIDO

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ✅ INYECCIÓN DEL FILTRO JWT
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // ✅ CONSTRUCTOR PARA INYECTAR EL FILTRO
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // Bean para el PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para el AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("SecurityConfig: Configurando filtros");
        System.out.println("JwtAuthenticationFilter: " + jwtAuthenticationFilter);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas (sin autenticación)
                        .requestMatchers(
                                "/api/auth/**", "/auth/**",
                                "/static/**", "/js/**", "/css/**", "/images/**",
                                "/", "/menu", "/api/productos/**",
                                "/test", "/test/**", "/WEB-INF/jsp/**"
                        ).permitAll()
                        .requestMatchers("/cliente/perfil").permitAll()

                        // Rutas protegidas por rol
                        .requestMatchers("/cliente/**").hasRole("CLIENTE")
                        .requestMatchers("/personal/**").hasRole("PERSONAL")
                        .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/usuarios/**").hasRole("ADMINISTRADOR")

                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            // Solo redirige a login si no es una petición a la API
                            String requestURI = request.getRequestURI();
                            if (requestURI.startsWith("/api/")) {
                                response.setStatus(401);
                                response.setContentType("application/json");
                                response.getWriter().write("{\"error\":\"No autorizado\"}");
                            } else {
                                response.sendRedirect("/auth/login");
                            }
                        })
                );

        return http.build();
    }
}
