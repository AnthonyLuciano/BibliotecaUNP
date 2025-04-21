package com.biblioteca.bibliotecavirtual.servicos;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.bibliotecavirtual.DTO.LivroDTO;
import com.biblioteca.bibliotecavirtual.repositorios.LivroRepositorio;

@Service
public class livroservicos {
    @Autowired
    private LivroRepositorio livroRepositorio;

    public List<LivroDTO> buscarTodosLivros() {
        return livroRepositorio.findAll();
    }

    public Optional<LivroDTO> buscarLivroPorId(Long id) {
        return livroRepositorio.findById(id);
    }

    public List<LivroDTO> buscarLivrosDisponiveis() {
        return livroRepositorio.findByDisponivelTrue();
    }

    public List<LivroDTO> buscarLivrosPorAutor(Long autorId) {
        return livroRepositorio.findByAutorId(autorId);
    }

    public List<LivroDTO> buscarLivrosPorTitulo(String titulo) {
        return livroRepositorio.findByTituloContainingIgnoreCase(titulo);
    }

    public LivroDTO salvarLivro(LivroDTO livro) {
        return livroRepositorio.save(livro);
    }

    public void deletarLivro(Long id) {
        livroRepositorio.deleteById(id);
    }
}