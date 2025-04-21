package com.biblioteca.bibliotecavirtual.servicos;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.bibliotecavirtual.modelos.Livro;
import com.biblioteca.bibliotecavirtual.repositorios.LivroRepositorio;

@Service
public class livroservicos {
    @Autowired
    private LivroRepositorio livroRepositorio;

    public List<Livro> buscarTodosLivros() {
        return livroRepositorio.findAll();
    }

    public Optional<Livro> buscarLivroPorId(Long id) {
        return livroRepositorio.findById(id);
    }

    public List<Livro> buscarLivrosDisponiveis() {
        return livroRepositorio.findByDisponivelTrue();
    }

    public List<Livro> buscarLivrosPorAutor(Long autorId) {
        return livroRepositorio.findByAutorId(autorId);
    }

    public List<Livro> buscarLivrosPorTitulo(String titulo) {
        return livroRepositorio.findByTituloContainingIgnoreCase(titulo);
    }

    public Livro salvarLivro(Livro livro) {
        return livroRepositorio.save(livro);
    }

    public void deletarLivro(Long id) {
        livroRepositorio.deleteById(id);
    }
}