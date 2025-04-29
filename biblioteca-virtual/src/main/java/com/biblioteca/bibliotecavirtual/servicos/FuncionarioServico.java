package com.biblioteca.bibliotecavirtual.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.bibliotecavirtual.modelos.Funcionario;
import com.biblioteca.bibliotecavirtual.repositorios.FuncionarioRepositorio;

@Service
public class FuncionarioServico {
    @Autowired
    private FuncionarioRepositorio funcionarioRepositorio;

    public List<Funcionario> buscarTodosFuncionarios() {
        return funcionarioRepositorio.findAll();
    }

    public Funcionario salvarFuncionario(Funcionario funcionario) {
        return funcionarioRepositorio.save(funcionario);
    }
}