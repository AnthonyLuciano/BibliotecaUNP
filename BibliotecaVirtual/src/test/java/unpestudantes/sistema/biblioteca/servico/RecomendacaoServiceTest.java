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
    void recomendarPorGeneroEAutorDeveRetornarLivros() {
        Usuario usuario = new Usuario();
        Emprestimo emp = mock(Emprestimo.class);
        when(emp.getEditionKey()).thenReturn("OL12345M");
        when(emp.getIsbn()).thenReturn(null);

        List<Emprestimo> emprestimos = List.of(emp);
        when(emprestimoRepository.findByUsuario(usuario)).thenReturn(emprestimos);

        DetalhesLivroOpenLibrary detalhes = new DetalhesLivroOpenLibrary();
        detalhes.setGeneros(List.of("science_fiction"));
        detalhes.setAutores(List.of("Autor Teste"));
        when(openLibraryService.buscarDetalhesLivro("OL12345M")).thenReturn(detalhes);

        List<LivroOpenLibrary> livrosRecomendados = List.of(new LivroOpenLibrary());
        when(openLibraryService.buscarLivrosPorGeneroEAutor(anyString(), anyString())).thenReturn(livrosRecomendados);

        List<LivroOpenLibrary> resultado = recomendacaoService.recomendarPorGeneroEAutor(usuario);

        assertEquals(livrosRecomendados, resultado);
        System.out.println("✅ [RecomendacaoServiceTest] Recomendações retornadas corretamente para o usuário.");
    }
}