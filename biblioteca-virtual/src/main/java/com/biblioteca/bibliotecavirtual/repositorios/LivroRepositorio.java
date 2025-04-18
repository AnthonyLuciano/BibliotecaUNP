package com.biblioteca.bibliotecavirtual.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.bibliotecavirtual.modelos.Livro;

public interface LivroRepositorio extends JpaRepository<Livro, Long> {
    List<Livro> findByDisponivelTrue();
    List<Livro> findByAutorId(Long autorId);
    List<Livro> findByTituloContainingIgnoreCase(String titulo);
}
