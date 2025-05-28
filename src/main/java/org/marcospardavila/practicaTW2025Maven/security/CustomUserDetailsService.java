package org.marcospardavila.practicaTW2025Maven.security;

import org.marcospardavila.practicaTW2025Maven.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Carga los detalles de un usuario por su nombre de usuario (en este caso, el email).
     * Este métodoes llamado por Spring Security durante el proceso de autenticación.
     *
     * @param username El email del usuario a buscar.
     * @return UserDetails que representa el usuario encontrado.
     * @throws UsernameNotFoundException Si no se encuentra ningún usuario con el email dado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca el usuario en la base de datos por su email
        // Devuelve el objeto Usuario, que ya implementa UserDetails
        // Spring Security usará este objeto para comparar la contraseña y determinar los roles/autoridades
        return usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + username));
    }
}