package unpestudantes.sistema.biblioteca.servico;

import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import unpestudantes.sistema.biblioteca.modelo.emprestimo.Emprestimo;
import unpestudantes.sistema.biblioteca.modelo.livro.DetalhesLivroOpenLibrary;
import unpestudantes.sistema.biblioteca.modelo.livro.LivroOpenLibrary;
import unpestudantes.sistema.biblioteca.modelo.usuario.Usuario;
import unpestudantes.sistema.biblioteca.repositorio.EmprestimoRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class RecomendacaoServiceTest {

    @InjectMocks
    private RecomendacaoService recomendacaoService;

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private OpenLibraryService openLibraryService;

    @Test
    void recomendarPorGeneroEAutorDeveRetornarLivrosPorAutorApenas() {
        Usuario usuario = new Usuario();
        Emprestimo emp = mock(Emprestimo.class);
        when(emp.getEditionKey()).thenReturn(null);
        when(emp.getIsbn()).thenReturn("1234567890");

        List<Emprestimo> emprestimos = List.of(emp);
        when(emprestimoRepository.findByUsuario(usuario)).thenReturn(emprestimos);

        DetalhesLivroOpenLibrary detalhes = new DetalhesLivroOpenLibrary();
        detalhes.setGeneros(null); // Nenhum gênero
        detalhes.setAutores(List.of("Autor Único"));
        when(openLibraryService.buscarDetalhesPorIsbn("1234567890")).thenReturn(detalhes);

        List<LivroOpenLibrary> livrosRecomendados = List.of(new LivroOpenLibrary());
        when(openLibraryService.buscarLivrosPorAutor("Autor Único")).thenReturn(livrosRecomendados);

        List<LivroOpenLibrary> resultado = recomendacaoService.recomendarPorGeneroEAutor(usuario);

        assertEquals(livrosRecomendados, resultado);
    }

    @Test
    void recomendarPorGeneroEAutorDeveRetornarListaVaziaSeSemEmprestimos() {
        Usuario usuario = new Usuario();
        when(emprestimoRepository.findByUsuario(usuario)).thenReturn(Collections.emptyList());

        List<LivroOpenLibrary> resultado = recomendacaoService.recomendarPorGeneroEAutor(usuario);

        assertTrue(resultado.isEmpty());
    }
}