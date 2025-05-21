package com.example.loginapp.controlador;

import com.example.loginapp.modelo.Livro;
import com.example.loginapp.modelo.Usuario;
import com.example.loginapp.repositorio.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LivroControllerTest {

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @InjectMocks
    private LivroController livroController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarLivros_DeveBuscarTodosQuandoBuscaVazia() {
        List<Livro> livros = Arrays.asList(new Livro(), new Livro());
        when(livroRepository.findAll()).thenReturn(livros);
        when(session.getAttribute("usuarioLogado")).thenReturn(new Usuario());

        String view = livroController.listarLivros(null, model, session);

        verify(livroRepository, times(1)).findAll();
        verify(model).addAttribute("livros", livros);
        verify(model).addAttribute(eq("isAdmin"), anyBoolean());
        assertEquals("livros", view);
    }

    @Test
    void listarLivros_DeveBuscarPorPalavraChaveQuandoBuscaInformada() {
        List<Livro> livros = Arrays.asList(new Livro());
        when(livroRepository.pesquisar("teste")).thenReturn(livros);
        when(session.getAttribute("usuarioLogado")).thenReturn(new Usuario());

        String view = livroController.listarLivros("teste", model, session);

        verify(livroRepository, times(1)).pesquisar("teste");
        verify(model).addAttribute("livros", livros);
        verify(model).addAttribute(eq("isAdmin"), anyBoolean());
        assertEquals("livros", view);
    }
}