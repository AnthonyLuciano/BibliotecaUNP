package com.example.loginapp.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
/*
 * @author anthony
 */
@Entity
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String autor;
    private String genero;
    private String dataLancamento;
    private String ISBN;
    private boolean disponivel;

    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getDataLancamento() { return dataLancamento; }
    public void setDataLancamento(String dataLancamento) { this.dataLancamento = dataLancamento; }

    public String getISBN() { return ISBN; }
    public void setISBN(String ISBN) { this.ISBN = ISBN; }

    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
}

public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private String categoria;
    private int anoPublicacao;
    private String editora;
    private String isbn;
    private String sinopse;
    private int estoque;
    private boolean disponibilidade;
    private int vezesEmprestado; // usado para o relatório

    public Livro(int id, String titulo, String autor, String categoria, int anoPublicacao,
                 String editora, String isbn, String sinopse, int estoque) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.anoPublicacao = anoPublicacao;
        this.editora = editora;
        this.isbn = isbn;
        this.sinopse = sinopse;
        this.estoque = estoque;
        this.disponibilidade = estoque > 0;
        this.vezesEmprestado = 0;
    }

    public void registrarEmprestimo() {
        this.vezesEmprestado++;
    }

    public int getVezesEmprestado() {
        return vezesEmprestado;
    }

    public String getTitulo() {
        return titulo;
    }


}
