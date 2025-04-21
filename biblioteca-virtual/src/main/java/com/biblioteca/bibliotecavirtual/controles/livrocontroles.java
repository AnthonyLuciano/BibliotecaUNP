package com.biblioteca.bibliotecavirtual.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.bibliotecavirtual.DTO.LivroDTO;
import com.biblioteca.bibliotecavirtual.servicos.livroservicos;

@RestController
@RequestMapping("/api/livros")
public class livrocontroles {
    @Autowired
    private livroservicos livroService;

    @GetMapping
    public List<LivroDTO> listarTodos() {
        return livroService.buscarTodosLivros();
    }

    @GetMapping("/disponiveis")
    public List<LivroDTO> listarDisponiveis() {
        return livroService.buscarLivrosDisponiveis();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroDTO> buscarPorId(@PathVariable Long id) {
        return livroService.buscarLivroPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public LivroDTO adicionar(@RequestBody LivroDTO livro) {
        return livroService.salvarLivro(livro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        livroService.deletarLivro(id);
        return ResponseEntity.noContent().build();
    }
}