package com.biblioteca.bibliotecavirtual.modelos;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "O nome do autor é obrigatório")
    private String nome;
    
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
    private Set<Livro> livros;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Livro> getLivros() {
        return livros;
    }

    public void setLivros(Set<Livro> livros) {
        this.livros = livros;
    }
}
