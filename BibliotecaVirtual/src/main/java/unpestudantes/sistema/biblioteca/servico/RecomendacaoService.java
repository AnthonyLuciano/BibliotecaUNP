package unpestudantes.sistema.biblioteca.servico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unpestudantes.sistema.biblioteca.modelo.livro.LivroOpenLibrary;
import unpestudantes.sistema.biblioteca.modelo.livro.DetalhesLivroOpenLibrary;
import unpestudantes.sistema.biblioteca.modelo.usuario.Usuario;
import unpestudantes.sistema.biblioteca.modelo.emprestimo.Emprestimo;
import unpestudantes.sistema.biblioteca.repositorio.EmprestimoRepository;
import java.util.*;

@Service
public class RecomendacaoService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private OpenLibraryService openLibraryService;

    /**
     * Recomenda livros ao usuário com base nos gêneros e autores mais emprestados.
     */
    public List<LivroOpenLibrary> recomendarPorGeneroEAutor(Usuario usuario) {
        List<Emprestimo> emprestimos = emprestimoRepository.findByUsuario(usuario);
        if (emprestimos.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, Integer> generos = new HashMap<>();
        Map<String, Integer> autores = new HashMap<>();

        for (Emprestimo emp : emprestimos) {
            // Busca detalhes do livro na OpenLibrary
            DetalhesLivroOpenLibrary detalhes = null;
            if (emp.getEditionKey() != null && !emp.getEditionKey().isBlank()) {
                detalhes = openLibraryService.buscarDetalhesLivro(emp.getEditionKey());
            } else if (emp.getIsbn() != null && !emp.getIsbn().isBlank()) {
                detalhes = openLibraryService.buscarDetalhesPorIsbn(emp.getIsbn());
            }

            if (detalhes != null) {
                // Gêneros
                if (detalhes.getGeneros() != null) {
                    for (String genero : detalhes.getGeneros()) {
                        if (genero != null && !genero.isBlank()) {
                            generos.put(genero, generos.getOrDefault(genero, 0) + 1);
                        }
                    }
                }
                // Autores
                if (detalhes.getAutores() != null) {
                    for (String autor : detalhes.getAutores()) {
                        if (autor != null && !autor.isBlank()) {
                            autores.put(autor, autores.getOrDefault(autor, 0) + 1);
                        }
                    }
                }
            }
        }

        // Gênero mais frequente
        String generoMaisLido = generos.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);

        // Autor mais frequente
        String autorMaisLido = autores.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);

        // Recomenda por gênero e autor, se possível
        if (generoMaisLido != null && autorMaisLido != null) {
            return openLibraryService.buscarLivrosPorGeneroEAutor(generoMaisLido, autorMaisLido);
        } else if (generoMaisLido != null) {
            return openLibraryService.buscarLivros(generoMaisLido);
        } else if (autorMaisLido != null) {
            return openLibraryService.buscarLivrosPorAutor(autorMaisLido);
        } else {
            return Collections.emptyList();
        }
    }
}
