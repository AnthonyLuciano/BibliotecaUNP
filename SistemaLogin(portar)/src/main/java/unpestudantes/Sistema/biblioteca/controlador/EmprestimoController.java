package unpestudantes.Sistema.biblioteca.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import unpestudantes.Sistema.biblioteca.modelo.Emprestimo;
import unpestudantes.Sistema.biblioteca.modelo.Usuario;
import unpestudantes.Sistema.biblioteca.repositorio.EmprestimoRepository;
import unpestudantes.Sistema.biblioteca.servico.OpenLibraryService;
import unpestudantes.Sistema.biblioteca.modelo.DetalhesLivroOpenLibrary;

@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private OpenLibraryService openLibraryService;

    @PostMapping("/emprestar/{isbn}")
    public String emprestarLivro(@PathVariable String isbn, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";

        // Verifica se já existe empréstimo em aberto desse livro para o usuário
        boolean jaEmprestado = emprestimoRepository.findByUsuarioAndDataDevolucaoRealIsNull(usuario)
            .stream().anyMatch(e -> e.getIsbn().equals(isbn));
        if (jaEmprestado) {
            model.addAttribute("erro", "Você já está com este livro emprestado.");
            return "redirect:/livros/" + isbn;
        }

        DetalhesLivroOpenLibrary detalhes = openLibraryService.buscarDetalhesPorIsbn(isbn);

        Emprestimo emp = new Emprestimo();
        emp.setUsuario(usuario);
        emp.setIsbn(isbn);
        emp.setTitulo(detalhes != null ? detalhes.getTitulo() : "Livro");
        emp.setDataEmprestimo(LocalDateTime.now());
        emp.setDataDevolucaoPrevista(LocalDateTime.now().plus(14, ChronoUnit.DAYS)); // 14 dias
        emprestimoRepository.save(emp);

        return "redirect:/perfil";
    }

    @PostMapping("/devolver/{id}")
    public String devolverLivro(@PathVariable Long id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";

        Emprestimo emp = emprestimoRepository.findById(id).orElse(null);
        if (emp != null && emp.getUsuario().getId().equals(usuario.getId()) && emp.getDataDevolucaoReal() == null) {
            emp.setDataDevolucaoReal(LocalDateTime.now());
            emprestimoRepository.save(emp);
        }
        return "redirect:/perfil";
    }
}