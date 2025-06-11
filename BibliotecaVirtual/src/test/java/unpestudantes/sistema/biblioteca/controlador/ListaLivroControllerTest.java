package unpestudantes.sistema.biblioteca.controlador;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import unpestudantes.sistema.biblioteca.controlador.usuario.ListaLivroController;
import unpestudantes.sistema.biblioteca.modelo.livro.ListaLivro;
import unpestudantes.sistema.biblioteca.modelo.usuario.Usuario;
import unpestudantes.sistema.biblioteca.repositorio.ListaLivroRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ListaLivroControllerTest {

    @InjectMocks
    private ListaLivroController controller;

    @Mock
    private ListaLivroRepository listaLivroRepository;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Test
    void verListasDeveRedirecionarSeNaoLogado() {
        when(session.getAttribute("usuarioLogado")).thenReturn(null);
        String result = controller.verListas(model, session);
        assertEquals("redirect:/login", result);
    }

    @Test
    void verListasDeveAdicionarListasAoModel() {
        Usuario usuario = new Usuario();
        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);
        List<ListaLivro> listas = List.of(new ListaLivro());
        when(listaLivroRepository.findByUsuario(usuario)).thenReturn(listas);

        String result = controller.verListas(model, session);

        verify(model).addAttribute("listas", listas);
        assertEquals("perfil", result); // Corrigido
    }

    @Test
    void criarListaDeveRedirecionarSeNaoLogado() {
        when(session.getAttribute("usuarioLogado")).thenReturn(null);
        String result = controller.criarLista("Minha Lista", session);
        assertEquals("redirect:/login", result);
    }

    @Test
    void criarListaDeveSalvarNovaListaSeNaoExistir() {
        Usuario usuario = new Usuario();
        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);
        when(listaLivroRepository.findByUsuarioAndNomeLista(usuario, "Minha Lista")).thenReturn(Collections.emptyList());

        String result = controller.criarLista("Minha Lista", session);

        verify(listaLivroRepository).save(any(ListaLivro.class));
        assertEquals("redirect:/perfil", result); // Corrigido
    }

    @Test
    void adicionarLivroDeveRedirecionarSeNaoLogado() {
        when(session.getAttribute("usuarioLogado")).thenReturn(null);
        String result = controller.adicionarLivro("Minha Lista", "123", "Livro", session);
        assertEquals("redirect:/login", result);
    }

    @Test
    void removerLivroDeveRedirecionarSeNaoLogado() {
        when(session.getAttribute("usuarioLogado")).thenReturn(null);
        String result = controller.removerLivro(1L, session);
        assertEquals("redirect:/login", result);
    }

    @Test
    void removerLivroDeveChamarDeleteById() {
        Usuario usuario = new Usuario();
        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);

        String result = controller.removerLivro(1L, session);

        verify(listaLivroRepository).deleteById(1L);
        assertEquals("redirect:/perfil", result); // Corrigido
    }
}
