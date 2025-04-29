package com.biblioteca.bibliotecavirtual.servicos;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.bibliotecavirtual.modelos.Autor;
import com.biblioteca.bibliotecavirtual.repositorios.AutorRepositorio;

@Service
public class AutorServico {
    @Autowired
    private AutorRepositorio autorRepositorio;

    public List<Autor> buscarTodosAutores() {
        return autorRepositorio.findAll().stream()
            .map(autor -> new Autor(autor.getId(), autor.getNome()))
            .collect(Collectors.toList());
    }

    public Autor salvarAutor(Autor dto) {
        Autor autor = new Autor();
        autor.setId(dto.getId());
        autor.setNome(dto.getNome());
        Autor salvo = autorRepositorio.save(autor);
        return new Autor(salvo.getId(), salvo.getNome());
    }
}