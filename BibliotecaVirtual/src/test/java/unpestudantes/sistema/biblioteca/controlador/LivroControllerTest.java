package unpestudantes.sistema.biblioteca.controlador;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import unpestudantes.sistema.biblioteca.modelo.livro.DetalhesLivroOpenLibrary;
import unpestudantes.sistema.biblioteca.modelo.livro.LivroOpenLibrary;
import unpestudantes.sistema.biblioteca.modelo.usuario.Usuario;
import unpestudantes.sistema.biblioteca.servico.OpenLibraryService;
import unpestudantes.sistema.biblioteca.controlador.sistema.LivroController;

import java.util.Arrays;
import java.util.Collections;
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

        verify(model).addAttribute("livros", Collections.emptyList());
        verify(model).addAttribute("isAdmin", false);
        assertEquals("livros", view);
        System.out.println("✅ [LivroControllerTest] Listagem de livros sem busca realizada com sucesso.");
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
    }

    @Test
    void listarLivros_DeveListarComSucesso() {
        Usuario usuario = new Usuario();
        usuario.setAdmin(false);
        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);

        String view = livroController.listarLivros(null, model, session);

        verify(model).addAttribute("livros", Collections.emptyList());
        verify(model).addAttribute("isAdmin", false);
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
        assertEquals("livros", view);
    }

    @Test
    void listarLivros_DeveRetornarListaVaziaQuandoUsuarioNaoLogado() {
        when(session.getAttribute("usuarioLogado")).thenReturn(null);

        String view = livroController.listarLivros(null, model, session);

        verify(model).addAttribute("livros", Collections.emptyList());
        verify(model).addAttribute("isAdmin", false);
        assertEquals("livros", view);
    }

    @Test
    void listarLivros_DeveRetornarListaVaziaQuandoBuscaVazia() {
        Usuario usuario = new Usuario();
        usuario.setAdmin(false);
        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);

        String view = livroController.listarLivros("", model, session);

        verify(model).addAttribute("livros", Collections.emptyList());
        verify(model).addAttribute("isAdmin", false);
        assertEquals("livros", view);
    }

    @Test
    void detalhesLivro_DeveRetornarDetalhesQuandoEncontrado() {
        DetalhesLivroOpenLibrary detalhes = new DetalhesLivroOpenLibrary();
        when(openLibraryService.buscarDetalhesPorEditionKey("123")).thenReturn(detalhes);

        String view = livroController.detalhesLivro("123", model, null);

        verify(openLibraryService, times(1)).buscarDetalhesPorEditionKey("123");
        verify(model).addAttribute("detalhes", detalhes);
        assertEquals("detalhes", view);
    }

    @Test
    void detalhesLivro_DeveRetornarDetalhesNuloQuandoNaoEncontrado() {
        when(openLibraryService.buscarDetalhesPorEditionKey("999")).thenReturn(null);

        String view = livroController.detalhesLivro("999", model, null);

        verify(openLibraryService, times(1)).buscarDetalhesPorEditionKey("999");
        verify(model).addAttribute("detalhes", (Object) null);
        assertEquals("detalhes", view);
    }
}