package unpestudantes.Sistema.biblioteca.servico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unpestudantes.Sistema.biblioteca.modelo.HistoricoLeitura;
import unpestudantes.Sistema.biblioteca.modelo.Usuario;
import unpestudantes.Sistema.biblioteca.modelo.LivroOpenLibrary;
import unpestudantes.Sistema.biblioteca.repositorio.HistoricoLeituraRepository;

import java.util.*;

@Service
public class RecomendacaoService {

    @Autowired
    private HistoricoLeituraRepository historicoLeituraRepository;

    @Autowired
    private OpenLibraryService openLibraryService;

    /**
     * Recomenda livros ao usuário com base no gênero mais lido em seu histórico.
     * Busca o histórico do usuário, identifica o gênero mais frequente e retorna livros desse gênero.
     * Conversa com HistoricoLeituraRepository e OpenLibraryService.
     */
    public List<LivroOpenLibrary> recomendarPorGenero(Usuario usuario) {
        // Busca o histórico de leitura do usuário
        List<HistoricoLeitura> historico = historicoLeituraRepository.findByUsuarioOrderByDataAcessoDesc(usuario);
        if (historico.isEmpty()) {
            // Se não há histórico, retorna uma lista vazia ou recomenda livros populares
            return Collections.emptyList();
        }

        // Conta a frequência de cada gênero lido
        Map<String, Integer> contagem = new HashMap<>();
        for (HistoricoLeitura h : historico) {
            String genero = h.getGenero();
            if (genero != null && !genero.isBlank()) {
                contagem.put(genero, contagem.getOrDefault(genero, 0) + 1);
            }
        }

        // Identifica o gênero mais lido
        String generoMaisLido = contagem.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);

        if (generoMaisLido != null) {
            // Busca livros desse gênero na OpenLibrary
            return openLibraryService.buscarLivros(generoMaisLido);
        } else {
            // Se não encontrou gênero, retorna vazio ou recomenda livros populares
            return Collections.emptyList();
        }
    }
}
