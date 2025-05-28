package org.marcospardavila.practicaTW2025Maven.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors; // Asegúrate de importar esto si usas roles

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Genera un token JWT a partir de la autenticación del usuario.
     *
     * @param authentication El objeto Authentication de Spring Security,
     * que contiene los detalles del usuario autenticado.
     * @return El token JWT generado.
     */
    public String generateToken(Authentication authentication) {
        // Obtener el nombre de usuario (email) del objeto Authentication.
        // authentication.getName() es el método estándar para obtener el principal
        // (que en tu caso es el email).
        String username = authentication.getName();

        // Opcional: Añadir roles/autoridades al token si es necesario
        // Esto depende de si quieres los roles dentro del payload del JWT.
        String roles = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .subject(username) // El "subject" del token será el email del usuario
                .issuedAt(new Date())
                .expiration(expiryDate)
                .claim("roles", roles) // Añade los roles como un "claim" personalizado
                .signWith(key)
                .compact();
    }

    // El resto de tus métodos validateToken y getUsernameFromJWT son correctos.
    // Los parseSignedClaims().getPayload() en getUsernameFromJWT también está bien.
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Firma JWT inválida: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("Token JWT malformado: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("Token JWT expirado: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Token JWT no soportado: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("La cadena JWT está vacía: {}", ex.getMessage());
        }
        return false;
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
}