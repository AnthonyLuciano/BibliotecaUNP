package unpestudantes.sistema.biblioteca.controlador;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import unpestudantes.sistema.biblioteca.controlador.sistema.EmprestimoController;
import unpestudantes.sistema.biblioteca.modelo.emprestimo.Emprestimo;
import unpestudantes.sistema.biblioteca.modelo.livro.DetalhesLivroOpenLibrary;
import unpestudantes.sistema.biblioteca.modelo.usuario.Usuario;
import unpestudantes.sistema.biblioteca.repositorio.EmprestimoRepository;
import unpestudantes.sistema.biblioteca.servico.OpenLibraryService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class EmprestimoControllerTest {

    @InjectMocks
    private EmprestimoController controller;

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private OpenLibraryService openLibraryService;

    @Mock
    private HttpSession session;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private Model model;

    @Test
    void emprestarPorIsbnRedirecionaSeNaoLogado() {
        when(session.getAttribute("usuarioLogado")).thenReturn(null);
        String result = controller.emprestarPorIsbn("123", session, redirectAttributes);
        assertEquals("redirect:/login", result);
    }

    @Test
    void emprestarPorIsbnNaoPermiteEmprestimoDuplicado() {
        Usuario usuario = new Usuario();
        Emprestimo emp = new Emprestimo();
        emp.setIsbn("123");
        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);
        when(emprestimoRepository.findByUsuarioAndDataDevolucaoRealIsNull(usuario)).thenReturn(List.of(emp));

        String result = controller.emprestarPorIsbn("123", session, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute(eq("mensagem"), contains("Você já está com este livro emprestado"));
        assertEquals("redirect:/livros/123", result);
    }

    @Test
    void emprestarPorIsbnSalvaEmprestimo() {
        Usuario usuario = new Usuario();
        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);
        when(emprestimoRepository.findByUsuarioAndDataDevolucaoRealIsNull(usuario)).thenReturn(Collections.emptyList());
        when(openLibraryService.buscarDetalhesPorIsbn("123")).thenReturn(new DetalhesLivroOpenLibrary());

        String result = controller.emprestarPorIsbn("123", session, redirectAttributes);

        verify(emprestimoRepository).save(any(Emprestimo.class));
        assertTrue(result.startsWith("redirect:/livros/123"));
    }

    @Test
    void devolverLivroRedirecionaSeNaoLogado() {
        when(session.getAttribute("usuarioLogado")).thenReturn(null);
        String result = controller.devolverLivro(1L, session);
        assertEquals("redirect:/login", result);
    }

    @Test
    void devolverLivroSalvaDevolucao() {
        Usuario usuario = new Usuario();
        Emprestimo emp = new Emprestimo();
        emp.setUsuario(usuario);
        emp.setDataDevolucaoReal(null);

        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emp));

        String result = controller.devolverLivro(1L, session);

        verify(emprestimoRepository).save(emp);
        assertEquals("redirect:/perfil", result);
    }

    @Test
    void listarEmprestimosRedirecionaSeNaoLogado() {
        when(session.getAttribute("usuarioLogado")).thenReturn(null);
        String result = controller.listarEmprestimos(model, session);
        assertEquals("redirect:/login", result);
    }

    @Test
    void listarEmprestimosAdicionaAoModel() {
        Usuario usuario = new Usuario();
        List<Emprestimo> emprestimos = List.of(new Emprestimo());
        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);
        when(emprestimoRepository.findByUsuario(usuario)).thenReturn(emprestimos);

        String result = controller.listarEmprestimos(model, session);

        verify(model).addAttribute("emprestimos", emprestimos);
        assertEquals("usuario/meus-emprestimos", result);
    }
}
