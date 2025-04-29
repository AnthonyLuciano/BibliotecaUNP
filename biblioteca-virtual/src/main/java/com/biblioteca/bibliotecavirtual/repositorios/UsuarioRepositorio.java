package com.biblioteca.bibliotecavirtual.repositorios;
import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.bibliotecavirtual.modelos.Usuario;
/*
 *  @author: anthony 
 */


public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
}