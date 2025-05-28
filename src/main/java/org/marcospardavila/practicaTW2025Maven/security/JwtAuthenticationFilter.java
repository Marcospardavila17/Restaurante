package org.marcospardavila.practicaTW2025Maven.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    // Use constructor injection instead of @Autowired fields
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService) {
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
        try {
            // 1. Obtener el token JWT de la solicitud
            String jwt = getJwtFromRequest(request);

            // 2. Si el token es válido y no hay una autenticación existente en el contexto de seguridad
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                // 3. Obtener el nombre de usuario (email) del token
                String username = jwtTokenProvider.getUsernameFromJWT(jwt);

                // 4. Cargar los detalles del usuario
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                // 5. Crear un objeto de autenticación
                // Este objeto representa la autenticación del usuario y sus roles/autoridades.
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Establecer la autenticación en el contexto de seguridad de Spring
                // Esto indica a Spring Security que el usuario está autenticado para esta solicitud.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("No se pudo establecer la autenticación del usuario en el contexto de seguridad", ex);
        }

        // 7. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT de la cabecera "Authorization" de la solicitud.
     * El token se espera en el formato: "Bearer <TOKEN_JWT>"
     * @param request La solicitud HTTP.
     * @return El token JWT o null si no se encuentra.
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // Comprobar si la cabecera Authorization contiene el prefijo "Bearer "
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // Extraer el token (eliminar el prefijo "Bearer ")
            return bearerToken.substring(7);
        }
        return null;
    }
}