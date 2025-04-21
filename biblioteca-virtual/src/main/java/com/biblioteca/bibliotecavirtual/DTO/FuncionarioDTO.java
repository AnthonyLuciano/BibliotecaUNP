package com.biblioteca.bibliotecavirtual.DTO;

/**
 *
 * @author anthony
 */
public class FuncionarioDTO {
    private Long id;
    private String nome;
    private String cargo;

    public FuncionarioDTO() {}
    public FuncionarioDTO(Long id, String nome, String cargo) {
        this.id = id;
        this.nome = nome;
        this.cargo = cargo;
    }
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
    public String getCargo() {
        return cargo;
    }
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    
}
