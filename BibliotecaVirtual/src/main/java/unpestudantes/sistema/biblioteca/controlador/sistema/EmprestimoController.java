package unpestudantes.sistema.biblioteca.controlador.sistema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import unpestudantes.sistema.biblioteca.modelo.livro.DetalhesLivroOpenLibrary;
import unpestudantes.sistema.biblioteca.modelo.emprestimo.Emprestimo;
import unpestudantes.sistema.biblioteca.modelo.usuario.Usuario;
import unpestudantes.sistema.biblioteca.repositorio.EmprestimoRepository;
import unpestudantes.sistema.biblioteca.servico.OpenLibraryService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;

@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {
    private static final Logger logger = LoggerFactory.getLogger(EmprestimoController.class);

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private OpenLibraryService openLibraryService;

    // Empréstimo por ISBN
    @PostMapping("/emprestar/isbn/{isbn}")
    public String emprestarPorIsbn(@PathVariable String isbn, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";

        boolean jaEmprestado = emprestimoRepository.findByUsuarioAndDataDevolucaoRealIsNull(usuario)
            .stream().anyMatch(e -> isbn.equals(e.getIsbn()));
        if (jaEmprestado) {
            redirectAttributes.addFlashAttribute("mensagem", "Você já está com este livro emprestado.");
            return "redirect:/livros/" + isbn;
        }

        DetalhesLivroOpenLibrary detalhes = openLibraryService.buscarDetalhesPorIsbn(isbn);

        Emprestimo emp = new Emprestimo();
        emp.setUsuario(usuario);
        emp.setIsbn(isbn);
        emp.setTitulo(detalhes != null ? detalhes.getTitulo() : "Livro");
        emp.setDataEmprestimo(LocalDateTime.now());
        emp.setDataDevolucaoPrevista(LocalDateTime.now().plus(14, ChronoUnit.DAYS));
        emprestimoRepository.save(emp);
        logger.info("EMPRESTIMO SALVO: {}", emp);

        redirectAttributes.addFlashAttribute("mensagem",
            "Você poderá retirar o livro na biblioteca de " +
            emp.getDataEmprestimo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " até " +
            emp.getDataDevolucaoPrevista().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".");

        return "redirect:/livros/" + isbn;
    }

    // Empréstimo por editionKey
    @PostMapping("/emprestar/edition/{editionKey}")
    public String emprestarLivro(@PathVariable String editionKey, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        List<Emprestimo> emprestimos = emprestimoRepository.findByUsuarioAndDataDevolucaoRealIsNull(usuario);

        boolean temMulta = emprestimos.stream()
            .anyMatch(e -> e.getMulta() != null && e.getMulta().compareTo(BigDecimal.ZERO) > 0);

        if (temMulta) {
            redirectAttributes.addFlashAttribute("mensagem", "Você possui multas em aberto. Regularize antes de novos empréstimos.");
            return "redirect:/livros";
        }

        boolean jaEmprestado = emprestimoRepository.findByUsuarioAndDataDevolucaoRealIsNull(usuario)
            .stream().anyMatch(e -> editionKey.equals(e.getEditionKey()));
        if (jaEmprestado) {
            redirectAttributes.addFlashAttribute("mensagem", "Você já está com este livro emprestado.");
            return "redirect:/livros/" + editionKey;
        }

        DetalhesLivroOpenLibrary detalhes = openLibraryService.buscarDetalhesLivro(editionKey);

        Emprestimo emp = new Emprestimo();
        emp.setUsuario(usuario);
        emp.setEditionKey(editionKey);
        emp.setTitulo(detalhes != null ? detalhes.getTitulo() : "Livro");
        emp.setIsbn(detalhes != null && detalhes.getIsbn10() != null && !detalhes.getIsbn10().isEmpty() ? detalhes.getIsbn10().get(0) : "-");
        emp.setDataEmprestimo(LocalDateTime.now());
        emp.setDataDevolucaoPrevista(LocalDateTime.now().plusDays(7));
        emprestimoRepository.save(emp);
        /*logando o empréstimo no console e no log
        *para depuração 
        */
        System.out.println("EMPRESTIMO SALVO: " + emp);
        logger.info("EMPRESTIMO SALVO: {}", emp);

        redirectAttributes.addFlashAttribute("mensagem",
            "Você poderá retirar o livro na biblioteca de " +
            emp.getDataEmprestimo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " até " +
            emp.getDataDevolucaoPrevista().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".");

        return "redirect:/detalhes/" + editionKey; // <-- altere para o caminho correto da página de detalhes
    }

    /* Devolução de livro
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
        comentando o método de devolução para evitar conflitos com o método de listar empréstimos
        TODO: tirar duvidas com o professor sobre a necessidade de manter esse método ou não
    */
    // Listar empréstimos do usuário
    @GetMapping("/meus-emprestimos")
    public String listarEmprestimos(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";

        List<Emprestimo> emprestimos = emprestimoRepository.findByUsuario(usuario);
        model.addAttribute("emprestimos", emprestimos);

        for (Emprestimo emp : emprestimos) {
            System.out.println("Emprestimo: " + emp);
            System.out.println("Titulo: " + emp.getTitulo());
            System.out.println("ISBN: " + emp.getIsbn());
            System.out.println("Data: " + emp.getDataEmprestimo());
        }

        return "usuario/meus-emprestimos";
    }
}