package unpestudantes.Sistema.biblioteca.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpSession;
import unpestudantes.Sistema.biblioteca.modelo.Usuario;
import unpestudantes.Sistema.biblioteca.modelo.LivroOpenLibrary;
import unpestudantes.Sistema.biblioteca.servico.OpenLibraryService;
import unpestudantes.Sistema.biblioteca.modelo.DetalhesLivroOpenLibrary;
import unpestudantes.Sistema.biblioteca.modelo.HistoricoLeitura;
import unpestudantes.Sistema.biblioteca.repositorio.HistoricoLeituraRepository;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
@Controller
public class LivroController {

    @Autowired
    private OpenLibraryService openLibraryService;

    @Autowired
    private HistoricoLeituraRepository historicoLeituraRepository;

    /**
     * Lista todos os livros ou faz busca por palavra-chave.
     * Comunica com o template livros.html.
     * -Anthony
     */
    @GetMapping("/livros")
    public String listarLivros(@RequestParam(required = false) String busca, Model model, HttpSession session) {
    List<LivroOpenLibrary> livros = new ArrayList<>();
    if (busca != null && !busca.isEmpty()) {
        livros = openLibraryService.buscarLivros(busca);
    } else {
        livros = new ArrayList<>(); // Inicializa a lista vazia
    }
    model.addAttribute("livros", livros);

    // Adiciona isAdmin ao model para mostrar o botão admin apenas para administradores
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
    model.addAttribute("isAdmin", usuario != null && usuario.isAdmin());

    return "livros";
}


    /**
     * Mostra os detalhes de um livro específico pelo ID.
     * Busca o livro no banco e envia para o template detalhes.html.
     * Redireciona para /livros se não encontrar.
     * -Anthony
     */
    @GetMapping("/livros/{editionKey}")
    public String detalhesLivro(@PathVariable String editionKey, Model model) {
        DetalhesLivroOpenLibrary detalhes = openLibraryService.buscarDetalhesPorEditionKey(editionKey);
        model.addAttribute("detalhes", detalhes);
        return "detalhes";
    }
}