package com.biblioteca.bibliotecavirtual.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.bibliotecavirtual.modelos.Funcionario;

public interface FuncionarioRepositorio extends JpaRepository<Funcionario, Long> {
}
