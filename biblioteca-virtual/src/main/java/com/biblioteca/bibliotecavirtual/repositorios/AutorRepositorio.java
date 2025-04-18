package com.biblioteca.bibliotecavirtual.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.bibliotecavirtual.modelos.Autor;

public interface AutorRepositorio extends JpaRepository<Autor, Long> {
}