package com.biblioteca.bibliotecavirtual.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.bibliotecavirtual.DTO.AutorDTO;

public interface AutorRepositorio extends JpaRepository<AutorDTO, Long> {
}