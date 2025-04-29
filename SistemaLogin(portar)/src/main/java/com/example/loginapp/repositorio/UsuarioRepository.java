package com.example.loginapp.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.loginapp.modelo.Usuario;

// Interface de repositório para acessar dados dos usuários no banco
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Busca um usuário pelo nome de usuário
    Optional<Usuario> findByUsername(String username);
}