package unpestudantes.sistema.biblioteca.controlador;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import unpestudantes.sistema.biblioteca.modelo.livro.DetalhesLivroOpenLibrary;
import unpestudantes.sistema.biblioteca.modelo.livro.LivroOpenLibrary;
import unpestudantes.sistema.biblioteca.modelo.livro.LivroLocal;
import unpestudantes.sistema.biblioteca.modelo.usuario.Usuario;
import unpestudantes.sistema.biblioteca.servico.OpenLibraryService;
import unpestudantes.sistema.biblioteca.controlador.sistema.LivroController;
import unpestudantes.sistema.biblioteca.repositorio.LivroLocalRepository;
import unpestudantes.sistema.biblioteca.modelo.livro.ListaLivro;
import unpestudantes.sistema.biblioteca.repositorio.ListaLivroRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LivroControllerTest {

    @Mock
    private OpenLibraryService openLibraryService;

    @Mock
    private LivroLocalRepository livroLocalRepository;

    @Mock
    private ListaLivroRepository listaLivroRepository;

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

        verify(model).addAttribute("livrosLocais", Collections.emptyList());
        verify(model).addAttribute("livrosExternos", Collections.emptyList());
        verify(model).addAttribute("isAdmin", false);
        assertEquals("livros", view);
        System.out.println("✅ [LivroControllerTest] Listagem de livros sem busca realizada com sucesso.");
    }

    @Test
    void listarLivros_DeveBuscarPorPalavraChaveQuandoBuscaInformada() {
        List<LivroOpenLibrary> livrosExternos = Arrays.asList(new LivroOpenLibrary());
        List<LivroLocal> livrosLocais = Arrays.asList(new LivroLocal());
        when(openLibraryService.buscarLivros("teste")).thenReturn(livrosExternos);
        when(livroLocalRepository.findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCaseOrIsbn10ContainingIgnoreCaseOrIsbn13ContainingIgnoreCase(
            anyString(), anyString(), anyString(), anyString()
        )).thenReturn(livrosLocais);

        Usuario usuario = new Usuario();
        usuario.setAdmin(true);
        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);

        String view = livroController.listarLivros("teste", model, session);

        verify(openLibraryService, times(1)).buscarLivros("teste");
        verify(livroLocalRepository, times(1))
            .findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCaseOrIsbn10ContainingIgnoreCaseOrIsbn13ContainingIgnoreCase(
                "teste", "teste", "teste", "teste"
            );
        verify(model).addAttribute("livrosLocais", livrosLocais);
        verify(model).addAttribute("livrosExternos", livrosExternos);
        verify(model).addAttribute("isAdmin", true);
        assertEquals("livros", view);
    }

    @Test
    void listarLivros_DeveTratarSqlInjectionNaBusca() {
        String injecao = "' OR '1'='1";
        List<LivroOpenLibrary> livrosExternos = Arrays.asList(new LivroOpenLibrary());
        List<LivroLocal> livrosLocais = Arrays.asList(new LivroLocal());
        when(openLibraryService.buscarLivros(injecao)).thenReturn(livrosExternos);
        when(livroLocalRepository.findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCaseOrIsbn10ContainingIgnoreCaseOrIsbn13ContainingIgnoreCase(
            anyString(), anyString(), anyString(), anyString()
        )).thenReturn(livrosLocais);
        Usuario usuario = new Usuario();
        usuario.setAdmin(false);
        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);

        String view = livroController.listarLivros(injecao, model, session);

        verify(openLibraryService).buscarLivros(injecao);
        verify(livroLocalRepository).findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCaseOrIsbn10ContainingIgnoreCaseOrIsbn13ContainingIgnoreCase(
            injecao, injecao, injecao, injecao
        );
        verify(model).addAttribute("livrosLocais", livrosLocais);
        verify(model).addAttribute("livrosExternos", livrosExternos);
        verify(model).addAttribute("isAdmin", false);
        assertEquals("livros", view);
    }

    @Test
    void listarLivros_DeveRetornarListaVaziaQuandoUsuarioNaoLogado() {
        when(session.getAttribute("usuarioLogado")).thenReturn(null);

        String view = livroController.listarLivros(null, model, session);

        verify(model).addAttribute("livrosLocais", Collections.emptyList());
        verify(model).addAttribute("livrosExternos", Collections.emptyList());
        verify(model).addAttribute("isAdmin", false);
        assertEquals("livros", view);
    }

    @Test
    void listarLivros_DeveRetornarListaVaziaQuandoBuscaVazia() {
        Usuario usuario = new Usuario();
        usuario.setAdmin(false);
        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);

        String view = livroController.listarLivros("", model, session);

        verify(model).addAttribute("livrosLocais", Collections.emptyList());
        verify(model).addAttribute("livrosExternos", Collections.emptyList());
        verify(model).addAttribute("isAdmin", false);
        assertEquals("livros", view);
    }

    @Test
    void detalhesLivro_DeveRetornarDetalhesQuandoEncontrado() {
        DetalhesLivroOpenLibrary detalhes = new DetalhesLivroOpenLibrary();
        when(openLibraryService.buscarDetalhesLivro("123")).thenReturn(detalhes);

        String view = livroController.detalhesLivro("123", model, null, session);

        verify(openLibraryService, times(1)).buscarDetalhesLivro("123");
        verify(model).addAttribute("detalhes", detalhes);
        assertEquals("detalhes", view);
    }

    @Test
    void detalhesLivro_DeveRetornarDetalhesNuloQuandoNaoEncontrado() {
        when(openLibraryService.buscarDetalhesLivro("999")).thenReturn(null);

        String view = livroController.detalhesLivro("999", model, null, session);

        verify(openLibraryService, times(1)).buscarDetalhesLivro("999");
        verify(model).addAttribute("detalhes", (Object) null);
        assertEquals("detalhes", view);
    }

    @Test
    void detalhesLivro_DeveAdicionarListasUsuarioAoModeloQuandoLogado() {
        DetalhesLivroOpenLibrary detalhes = new DetalhesLivroOpenLibrary();
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        List<ListaLivro> listas = Arrays.asList(new ListaLivro(), new ListaLivro());

        when(openLibraryService.buscarDetalhesLivro("123")).thenReturn(detalhes);
        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);
        when(listaLivroRepository.findByUsuario(usuario)).thenReturn(listas);

        String view = livroController.detalhesLivro("123", model, null, session);

        verify(openLibraryService, times(1)).buscarDetalhesLivro("123");
        verify(model).addAttribute("detalhes", detalhes);
        verify(model).addAttribute("listasUsuario", listas);
        assertEquals("detalhes", view);
    }

    @Test
    void detalhesLivro_DeveRetornarDetalhesQuandoEncontradoMesmoSemUsuario() {
        DetalhesLivroOpenLibrary detalhes = new DetalhesLivroOpenLibrary();
        when(openLibraryService.buscarDetalhesLivro("123")).thenReturn(detalhes);
        when(session.getAttribute("usuarioLogado")).thenReturn(null);

        String view = livroController.detalhesLivro("123", model, null, session);

        verify(openLibraryService, times(1)).buscarDetalhesLivro("123");
        verify(model).addAttribute("detalhes", detalhes);
        // Não adiciona listasUsuario ao modelo
        verify(model, never()).addAttribute(eq("listasUsuario"), any());
        assertEquals("detalhes", view);
    }
}