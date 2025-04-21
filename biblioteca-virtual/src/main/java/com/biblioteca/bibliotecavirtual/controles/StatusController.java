package com.biblioteca.bibliotecavirtual.controles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.biblioteca.bibliotecavirtual.repositorios.FuncionarioRepositorio;
/*programa de teste para ver se o sistema esta conectando ao banco de dados
verifique digitando "http://localhost:8080/status-banco" no seu navegador
Eu usaria Junit, mas Junit não funciona com o Spring Boot quando se
vai fazer o frontend com o Thymeleaf ;-;*/
@RestController
public class StatusController {

    @Autowired
    private FuncionarioRepositorio funcionarioRepositorio;

    @GetMapping("/status-banco")
    public String statusBanco() {
        try {
            funcionarioRepositorio.count(); // Tenta acessar o banco
            return "<html><body><h2 style='color:green'>Conexão com o banco de dados: SUCESSO!</h2></body></html>";
        } catch (Exception e) {
            return "<html><body><h2 style='color:red'>Conexão com o banco de dados: FALHOU!<br/>" + e.getMessage() + "</h2></body></html>";
        }
    }
}