package unpestudantes.sistema.biblioteca.controlador.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import unpestudantes.sistema.biblioteca.modelo.livro.ListaLivro;
import unpestudantes.sistema.biblioteca.modelo.usuario.Usuario;
import unpestudantes.sistema.biblioteca.repositorio.ListaLivroRepository;

import java.util.List;

@Controller
@RequestMapping("/lista")
public class ListaLivroController {

    @Autowired
    private ListaLivroRepository listaLivroRepository;

    @GetMapping
    public String verListas(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";
        List<ListaLivro> listas = listaLivroRepository.findByUsuario(usuario);
        model.addAttribute("listas", listas);
        return "listas";
    }

    @PostMapping("/criar")
    public String criarLista(@RequestParam String nomeLista, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";
        // Não cria duplicada
        if (listaLivroRepository.findByUsuarioAndNomeLista(usuario, nomeLista).isEmpty()) {
            ListaLivro lista = new ListaLivro();
            lista.setUsuario(usuario);
            lista.setNomeLista(nomeLista);
            listaLivroRepository.save(lista);
        }
        return "redirect:/lista";
    }

    @PostMapping("/adicionar")
    public String adicionarLivro(@RequestParam String nomeLista, @RequestParam String isbn, @RequestParam String titulo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";
        // Só adiciona se não existir
        if (listaLivroRepository.findByUsuarioAndNomeLista(usuario, nomeLista).stream().noneMatch(l -> l.getIsbn().equals(isbn))) {
            ListaLivro lista = new ListaLivro();
            lista.setUsuario(usuario);
            lista.setNomeLista(nomeLista);
            lista.setIsbn(isbn);
            lista.setTitulo(titulo);
            listaLivroRepository.save(lista);
        }
        return "redirect:/lista";
    }

    @PostMapping("/remover")
    public String removerLivro(@RequestParam Long id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";
        listaLivroRepository.deleteById(id);
        return "redirect:/lista";
    }
}