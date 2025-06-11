package unpestudantes.sistema.biblioteca.controlador.sistema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpSession;
import unpestudantes.sistema.biblioteca.modelo.livro.DetalhesLivroOpenLibrary;
import unpestudantes.sistema.biblioteca.modelo.livro.ListaLivro;
import unpestudantes.sistema.biblioteca.modelo.livro.LivroLocal;
import unpestudantes.sistema.biblioteca.modelo.livro.LivroOpenLibrary;
import unpestudantes.sistema.biblioteca.modelo.usuario.Usuario;
import unpestudantes.sistema.biblioteca.servico.OpenLibraryService;
import unpestudantes.sistema.biblioteca.repositorio.ListaLivroRepository;
import unpestudantes.sistema.biblioteca.repositorio.LivroLocalRepository;

import java.time.LocalDateTime;

@SuppressWarnings("unused")
@Controller
public class LivroController {

    @Autowired
    private ListaLivroRepository listaLivroRepository;

    @Autowired
    private OpenLibraryService openLibraryService;

    @Autowired
    private LivroLocalRepository livroLocalRepository;


    /**
     * Lista todos os livros ou faz busca por palavra-chave.
     * Comunica com o template livros.html.
     * -Anthony
     */
    @GetMapping("/livros")
    public String listarLivros(@RequestParam(required = false) String busca, Model model, HttpSession session) {
        List<LivroOpenLibrary> livrosExternos = new ArrayList<>();
        List<LivroLocal> livrosLocais = new ArrayList<>();
        if (busca != null && !busca.isEmpty()) {
            livrosLocais = livroLocalRepository.findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCaseOrIsbn10ContainingIgnoreCaseOrIsbn13ContainingIgnoreCase(busca, busca, busca, busca);
            livrosExternos = openLibraryService.buscarLivros(busca);
        }
        model.addAttribute("livrosLocais", livrosLocais);
        model.addAttribute("livrosExternos", livrosExternos);

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
    public String detalhesLivro(@PathVariable String editionKey, Model model, @ModelAttribute("mensagem") String mensagem, HttpSession session) {
        DetalhesLivroOpenLibrary detalhes = openLibraryService.buscarDetalhesLivro(editionKey);
        model.addAttribute("detalhes", detalhes);
        if (mensagem != null && !mensagem.trim().isEmpty()) {
            model.addAttribute("mensagem", mensagem);
        }
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario != null) {
            List<ListaLivro> listasUsuario = listaLivroRepository.findByUsuario(usuario);
            model.addAttribute("listasUsuario", listasUsuario);
        }
        return "detalhes";
    }

    @ModelAttribute("usuario")
    public Usuario getUsuario(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setFotoUrl(null);
        }
        return usuario;
    }

}