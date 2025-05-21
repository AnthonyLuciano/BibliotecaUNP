package com.example.loginapp.controlador;

import com.example.loginapp.modelo.Livro;
import com.example.loginapp.repositorio.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.loginapp.model.Livro;
import java.util.*;

import java.util.List;

@Controller
public class LivroController {

    @Autowired
    private LivroRepository livroRepository;

    @GetMapping("/livros")
    public String listarLivros(@RequestParam(required = false) String busca, Model model) {
        List<Livro> livros;
        if (busca != null && !busca.isEmpty()) {
            livros = livroRepository.pesquisar(busca);
        } else {
            livros = livroRepository.findAll();
        }
        model.addAttribute("livros", livros);
        return "cliente";
    }
    public class Relatorio {
    public static void gerarRelatorioLivrosMaisEmprestados(List<Livro> livros) {
        livros.sort((l1, l2) -> Integer.compare(l2.getVezesEmprestado(), l1.getVezesEmprestado()));

        System.out.println("=== Livros Mais Emprestados ===");
        for (Livro livro : livros) {
            System.out.println("Título: " + livro.getTitulo() + " | Empréstimos: " + livro.getVezesEmprestado());
        }
    }
}
}
