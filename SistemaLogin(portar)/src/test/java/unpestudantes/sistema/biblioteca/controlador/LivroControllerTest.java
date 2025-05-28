package unpestudantes.sistema.biblioteca.controlador;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import unpestudantes.Sistema.biblioteca.controlador.LivroController;
import unpestudantes.Sistema.biblioteca.modelo.LivroOpenLibrary;
import unpestudantes.Sistema.biblioteca.modelo.DetalhesLivroOpenLibrary;
import unpestudantes.Sistema.biblioteca.modelo.Usuario;
import unpestudantes.Sistema.biblioteca.servico.OpenLibraryService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LivroControllerTest {

    @Mock
    private OpenLibraryService openLibraryService;

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
        Usuario usuario = new Usuario();
        usuario.setAdmin(false);
        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);

        String view = livroController.listarLivros(null, model, session);

        verify(model).addAttribute("livros", null);
        verify(model).addAttribute("isAdmin", false);
        assertEquals("livros", view);
        System.out.println("✅ [LivroController] Todos os livros buscados com sucesso!");
    }

    @Test
    void listarLivros_DeveBuscarPorPalavraChaveQuandoBuscaInformada() {
        List<LivroOpenLibrary> livros = Arrays.asList(new LivroOpenLibrary());
        when(openLibraryService.buscarLivros("teste")).thenReturn(livros);
        Usuario usuario = new Usuario();
        usuario.setAdmin(true);
        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);

        String view = livroController.listarLivros("teste", model, session);

        verify(openLibraryService, times(1)).buscarLivros("teste");
        verify(model).addAttribute("livros", livros);
        verify(model).addAttribute("isAdmin", true);
        assertEquals("livros", view);
        System.out.println("✅ [LivroController] Livros buscados por palavra-chave com sucesso!");
    }

    @Test
    void listarLivros_DeveListarComSucesso() {
        Usuario usuario = new Usuario();
        usuario.setAdmin(false);
        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);

        String view = livroController.listarLivros(null, model, session);

        verify(model).addAttribute("livros", null);
        verify(model).addAttribute("isAdmin", false);
        System.out.println("✅ [LivroController] Listagem de livros realizada com sucesso!");
        assertEquals("livros", view);
    }

    @Test
    void listarLivros_DeveTratarSqlInjectionNaBusca() {
        String injecao = "' OR '1'='1";
        List<LivroOpenLibrary> livros = Arrays.asList(new LivroOpenLibrary());
        when(openLibraryService.buscarLivros(injecao)).thenReturn(livros);
        Usuario usuario = new Usuario();
        usuario.setAdmin(false);
        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);

        String view = livroController.listarLivros(injecao, model, session);

        verify(openLibraryService).buscarLivros(injecao);
        verify(model).addAttribute("livros", livros);
        verify(model).addAttribute("isAdmin", false);
        System.out.println("✅ [LivroController] SQL Injection tratado como texto na busca.");
        assertEquals("livros", view);
    }

    @Test
    void detalhesLivro_DeveRetornarDetalhesQuandoEncontrado() {
        DetalhesLivroOpenLibrary detalhes = new DetalhesLivroOpenLibrary();
        when(openLibraryService.buscarDetalhesPorIsbn("123")).thenReturn(detalhes);
        Usuario usuario = new Usuario();
        usuario.setAdmin(true);
        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);

        String view = livroController.detalhesLivro("123", model);

        verify(openLibraryService, times(1)).buscarDetalhesPorIsbn("123");
        verify(model).addAttribute("livro", detalhes);
        verify(model).addAttribute("isAdmin", true);
        assertEquals("detalhes", view);
        System.out.println("✅ [LivroController] Detalhes do livro exibidos com sucesso!");
    }

    @Test
    void detalhesLivro_DeveRedirecionarQuandoNaoEncontrado() {
        when(openLibraryService.buscarDetalhesPorIsbn("999")).thenReturn(null);
        Usuario usuario = new Usuario();
        usuario.setAdmin(false);
        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);

        String view = livroController.detalhesLivro("999", model);

        assertEquals("redirect:/livros", view);
        System.out.println("✅ [LivroController] Redirecionamento realizado quando livro não encontrado!");
    }
}