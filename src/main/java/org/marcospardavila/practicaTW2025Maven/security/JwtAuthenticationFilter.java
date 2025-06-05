package org.marcospardavila.practicaTW2025Maven.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    // Constructor injection
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Este método se ejecuta una vez por cada solicitud HTTP.
     * Intercepta la solicitud para validar el token JWT.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        logger.debug(">>>> Invocando JwtAuthenticationFilter para URI: {}", request.getRequestURI()); //
        try {
            // 1. Obtener el token JWT de la solicitud
            String jwt = getJwtFromRequest(request);
            logger.debug("Token JWT extraído: {}", (jwt != null ? "Sí" : "No")); //

            // 2. Si el token es válido y no hay una autenticación existente en el contexto de seguridad
            // La condición '&& SecurityContextHolder.getContext().getAuthentication() == null' no está aquí
            // en tu código original, y se mantiene así para que se procese el JWT si existe.
            if (StringUtils.hasText(jwt)) { //
                if (jwtTokenProvider.validateToken(jwt)) { //
                    // 3. Obtener el nombre de usuario (email) del token
                    String username = jwtTokenProvider.getUsernameFromJWT(jwt);
                    logger.debug("Nombre de usuario del token: {}", username); //

                    // 4. Cargar los detalles del usuario
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                    if (userDetails != null) { //
                        // 5. Crear un objeto de autenticación
                        // Este objeto representa la autenticación del usuario y sus roles/autoridades.
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // 6. Establecer la autenticación en el contexto de seguridad de Spring
                        // Esto indica a Spring Security que el usuario está autenticado para esta solicitud.
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.debug("¡Autenticación establecida en SecurityContextHolder para el usuario: {}!", username); //
                    } else {
                        logger.warn("UserDetails no encontrado para el usuario: {}", username); //
                    }
                } else {
                    logger.debug("Token JWT es inválido."); //
                }
            } else {
                logger.debug("Token JWT no presente en la solicitud."); //
            }
        } catch (Exception ex) {
            logger.error("No se pudo establecer la autenticación del usuario en el contexto de seguridad", ex); //
        }

        // 7. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
        logger.debug("<<<< Terminando JwtAuthenticationFilter para URI: {}", request.getRequestURI()); //
    }

    /**
     * Extrae el token JWT de la cabecera "Authorization" de la solicitud.
     * El token se espera en el formato: "Bearer <token>"
     * @param request La solicitud HTTP.
     * @return El token JWT o null si no se encuentra.
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        // 1. Prioridad 1: Header Authorization (para peticiones AJAX)
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            logger.debug("Token encontrado en header Authorization");
            return bearerToken.substring(7);
        }

        // 2. Prioridad 2: Cookie (para navegación tradicional)
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    logger.debug("Token encontrado en cookie");
                    return cookie.getValue();
                }
            }
        }

        // 3. Prioridad 3: Parámetro de consulta (fallback)
        String tokenParam = request.getParameter("token");
        if (StringUtils.hasText(tokenParam)) {
            logger.debug("Token encontrado en parámetro de consulta");
            return tokenParam;
        }

        logger.debug("No se encontró token JWT en la solicitud");
        return null;
    }
}