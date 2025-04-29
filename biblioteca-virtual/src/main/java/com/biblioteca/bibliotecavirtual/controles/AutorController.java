package com.biblioteca.bibliotecavirtual.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.bibliotecavirtual.modelos.Autor;
import com.biblioteca.bibliotecavirtual.servicos.AutorServico;

@RestController
@RequestMapping("/api/autores")
public class AutorController {
    @Autowired
    private AutorServico autorService;

    @GetMapping
    public List<Autor> listarTodos() {
        return autorService.buscarTodosAutores();
    }

    @PostMapping
    public Autor adicionar(@RequestBody Autor autorDTO) {
        return autorService.salvarAutor(autorDTO);
    }
}