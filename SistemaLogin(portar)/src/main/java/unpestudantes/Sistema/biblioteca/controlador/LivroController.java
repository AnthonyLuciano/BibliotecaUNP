package unpestudantes.Sistema.biblioteca.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import jakarta.servlet.http.HttpSession;
import unpestudantes.Sistema.biblioteca.modelo.Livro;
import unpestudantes.Sistema.biblioteca.modelo.Usuario;
import unpestudantes.Sistema.biblioteca.repositorio.LivroRepository;

@Controller
public class LivroController {

    @Autowired
    private LivroRepository livroRepository;

    /**
     * Lista todos os livros ou faz busca por palavra-chave.
     * Comunica com o template livros.html.
     * -Anthony
     */
    @GetMapping("/livros")
    public String listarLivros(@RequestParam(required = false) String busca, Model model, HttpSession session) {
        List<Livro> livros;
        if (busca != null && !busca.isEmpty()) {
            livros = livroRepository.pesquisar(busca);
        } else {
            livros = livroRepository.findAll();
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
    @GetMapping("/livros/{id}")
    public String detalhesLivro(@PathVariable Long id, Model model, HttpSession session) {
        Livro livro = livroRepository.findById(id).orElse(null);
        if (livro == null) {
            return "redirect:/livros";
        }
        model.addAttribute("livro", livro);

        // Adiciona isAdmin ao model para mostrar o botão admin apenas para administradores
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        model.addAttribute("isAdmin", usuario != null && usuario.isAdmin());

        return "detalhes";
    }
}