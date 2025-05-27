package unpestudantes.sistema.biblioteca.controlador;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import unpestudantes.Sistema.biblioteca.controlador.LivroController;
import unpestudantes.Sistema.biblioteca.modelo.Livro;
import unpestudantes.Sistema.biblioteca.modelo.Usuario;
import unpestudantes.Sistema.biblioteca.repositorio.LivroRepository;

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
        System.out.println("✅ [LivroController] Todos os livros buscados com sucesso!");
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
        System.out.println("✅ [LivroController] Livros buscados por palavra-chave com sucesso!");
    }

    @Test
    void listarLivros_DeveListarComSucesso() {
        List<Livro> livros = Arrays.asList(new Livro());
        when(livroRepository.findAll()).thenReturn(livros);
        when(session.getAttribute("usuarioLogado")).thenReturn(new Usuario());

        String view = livroController.listarLivros(null, model, session);

        verify(model).addAttribute("livros", livros);
        System.out.println("✅ [LivroController] Listagem de livros realizada com sucesso!");
        assertEquals("livros", view);
    }

    @Test
    void listarLivros_DeveTratarSqlInjectionNaBusca() {
        String injecao = "' OR '1'='1";
        List<Livro> livros = Arrays.asList(new Livro());
        when(livroRepository.pesquisar(injecao)).thenReturn(livros);
        when(session.getAttribute("usuarioLogado")).thenReturn(new Usuario());

        String view = livroController.listarLivros(injecao, model, session);

        verify(livroRepository).pesquisar(injecao);
        System.out.println("✅ [LivroController] SQL Injection tratado como texto na busca.");
        assertEquals("livros", view);
    }
}