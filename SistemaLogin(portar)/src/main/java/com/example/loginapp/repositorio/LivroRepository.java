package com.example.loginapp.repositorio;

import com.example.loginapp.modelo.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
//repositorio para pesquisa de livros
public interface LivroRepository extends JpaRepository<Livro, Long> {
    @Query("SELECT l FROM Livro l WHERE " +
    "LOWER(l.titulo) LIKE LOWER(CONCAT('%', :busca, '%')) OR " +
    "LOWER(l.autor) LIKE LOWER(CONCAT('%', :busca, '%')) OR " +
    "LOWER(l.genero) LIKE LOWER(CONCAT('%', :busca, '%')) OR " +
    "LOWER(l.ISBN) LIKE LOWER(CONCAT('%', :busca, '%')) OR " +
    "LOWER(l.datalancamento) LIKE LOWER(CONCAT('%', :busca, '%'))")
    List<Livro> pesquisar(@Param("busca") String busca);
}