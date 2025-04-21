package com.biblioteca.bibliotecavirtual.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.bibliotecavirtual.modelos.Funcionario;
import com.biblioteca.bibliotecavirtual.servicos.FuncionarioServico;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {
    @Autowired
    private FuncionarioServico funcionarioService;

    @GetMapping
    public List<Funcionario> listarTodos() {
        return funcionarioService.buscarTodosFuncionarios();
    }

    @PostMapping
    public Funcionario adicionar(@RequestBody Funcionario funcionario) {
        return funcionarioService.salvarFuncionario(funcionario);
    }
}