package com.biblioteca.bibliotecavirtual.DTO;

public class LivroDTO {
    private Long id;
    private String titulo;
    private Integer anoPublicacao;
    private Long autorId;
    private boolean disponivel = true;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }
    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }
    public Long getAutorId() {
        return autorId;
    }
    public void setAutorId(Long autorId) {
        this.autorId = autorId;
    }
    public boolean isDisponivel() {
        return disponivel;
    }
    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
    public LivroDTO(Long id, String titulo, Integer anoPublicacao, Long autorId, boolean disponivel) {
        this.id = id;
        this.titulo = titulo;
        this.anoPublicacao = anoPublicacao;
        this.autorId = autorId;
        this.disponivel = disponivel;
    }

    
}
