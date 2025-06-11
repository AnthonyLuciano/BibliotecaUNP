package unpestudantes.sistema.biblioteca.controlador.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import unpestudantes.sistema.biblioteca.modelo.livro.ListaLivro;
import unpestudantes.sistema.biblioteca.modelo.usuario.Usuario;
import unpestudantes.sistema.biblioteca.repositorio.ListaLivroRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller para gerenciamento das listas de livros dos usuários.
 */
@Controller
@RequestMapping("/lista")
public class ListaLivroController {

    @Autowired
    private ListaLivroRepository listaLivroRepository;

    /**
     * Exibe todas as listas do usuário logado.
     */
    @GetMapping
    public String verListas(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";
        List<ListaLivro> listas = listaLivroRepository.findByUsuario(usuario);
        // Supondo que 'listas' é uma lista de objetos com o campo 'nomeLista'
        Set<String> nomesListas = listas.stream()
            .map(ListaLivro::getNomeLista)
            .collect(Collectors.toSet());
        model.addAttribute("nomesListas", nomesListas);
        model.addAttribute("listas", listas);
        return "perfil"; // Corrija aqui
    }

    /**
     * Cria uma nova lista de livros para o usuário.
     */
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
        return "redirect:/perfil";
    }

    /**
     * Adiciona um livro a uma lista do usuário.
     */
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
        return "redirect:/perfil";
    }

    /**
     * Adiciona um livro a uma lista do usuário (versão com ID da lista e editionKey).
     */
    @PostMapping("/adicionar-livro")
    public String adicionarLivroALista(@RequestParam Long listaId, @RequestParam String editionKey, @RequestParam String titulo, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";
        ListaLivro lista = listaLivroRepository.findById(listaId).orElse(null);
        if (lista != null && lista.getUsuario().equals(usuario)) {
            // Evita duplicidade
            boolean jaExiste = listaLivroRepository.findByUsuarioAndNomeLista(usuario, lista.getNomeLista())
                .stream().anyMatch(l -> editionKey.equals(l.getIsbn()));
            if (!jaExiste) {
                ListaLivro novo = new ListaLivro();
                novo.setUsuario(usuario);
                novo.setNomeLista(lista.getNomeLista());
                novo.setIsbn(editionKey); // Usando editionKey como identificador
                novo.setTitulo(titulo);
                listaLivroRepository.save(novo);
                redirectAttributes.addFlashAttribute("mensagem", "Livro adicionado à lista!");
            } else {
                redirectAttributes.addFlashAttribute("mensagem", "Livro já está na lista!");
            }
        }
        return "redirect:/livros/" + editionKey;
    }

    /**
     * Remove um livro de uma lista do usuário.
     */
    @PostMapping("/remover")
    public String removerLivro(@RequestParam Long id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";
        listaLivroRepository.deleteById(id);
        return "redirect:/perfil";
    }

    /**
     * Deleta uma lista inteira do usuário.
     */
    @PostMapping("/deletar")
    public String deletarLista(@RequestParam String nomeLista, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";
        // Deleta todos os livros com o mesmo nomeLista e usuário
        List<ListaLivro> livrosDaLista = listaLivroRepository.findByUsuarioAndNomeLista(usuario, nomeLista);
        listaLivroRepository.deleteAll(livrosDaLista);
        return "redirect:/perfil";
    }
}