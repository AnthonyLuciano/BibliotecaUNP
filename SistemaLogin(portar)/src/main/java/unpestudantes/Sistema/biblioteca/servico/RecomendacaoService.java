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

    public List<LivroOpenLibrary> recomendarPorGenero(Usuario usuario) {
        List<HistoricoLeitura> historico = historicoLeituraRepository.findByUsuarioOrderByDataAcessoDesc(usuario);
        Map<String, Integer> contagem = new HashMap<>();
        for (HistoricoLeitura h : historico) {
            if (h.getGenero() != null && !h.getGenero().isEmpty()) {
                contagem.put(h.getGenero(), contagem.getOrDefault(h.getGenero(), 0) + 1);
            }
        }
        String generoMaisLido = contagem.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);

        if (generoMaisLido != null) {
            return openLibraryService.buscarLivros(generoMaisLido);
        }
        return Collections.emptyList();
    }
}
