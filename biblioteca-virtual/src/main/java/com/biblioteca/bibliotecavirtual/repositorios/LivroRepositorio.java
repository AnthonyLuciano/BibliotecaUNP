package com.biblioteca.bibliotecavirtual.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.bibliotecavirtual.DTO.LivroDTO;

public interface LivroRepositorio extends JpaRepository<LivroDTO, Long> {
    List<LivroDTO> findByDisponivelTrue();
    List<LivroDTO> findByAutorId(Long autorId);
    List<LivroDTO> findByTituloContainingIgnoreCase(String titulo);
}
