package org.example.practicaTW2025Maven.repository;

import org.example.practicaTW2025Maven.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    /**
     * Busca un usuario por su email (usado para login)
     * @param email El email del usuario a buscar
     * @return Optional con el usuario si existe, empty si no
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica si existe un usuario con el email proporcionado
     * @param email El email a verificar
     * @return true si existe, false si no
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuarios por tipo (Cliente, Personal, Administrador)
     * @param tipo El tipo de usuario a buscar
     * @return Lista de usuarios del tipo especificado
     */
    List<Usuario> findByTipo(String tipo);
}
