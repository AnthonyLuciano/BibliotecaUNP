package unpestudantes.sistema.biblioteca.controlador.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import unpestudantes.sistema.biblioteca.modelo.usuario.Usuario;
import unpestudantes.sistema.biblioteca.repositorio.UsuarioRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import unpestudantes.sistema.biblioteca.modelo.emprestimo.Emprestimo;
import unpestudantes.sistema.biblioteca.repositorio.EmprestimoRepository;
import unpestudantes.sistema.biblioteca.repositorio.LivroLocalRepository;
/*
 * @author anthony
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private LivroLocalRepository livroLocalRepository;

    String uploadDir = "src/main/resources/static/capas/";
    /*
     * literalmente um painel de administração, nao tem muito o que explicar
     * a nao ser CRUD basico
     */
    @GetMapping
    public String painel(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("livros", livroLocalRepository.findAll());
        return "Administrador"; // Corrija aqui!
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@RequestParam String username, @RequestParam String password, @RequestParam(defaultValue = "false") boolean admin, Model model) {
        if (usuarioRepository.findByUsername(username).isPresent()) {
            model.addAttribute("erro", "Usuário já existe");
            model.addAttribute("usuarios", usuarioRepository.findAll());
            model.addAttribute("livros", livroLocalRepository.findAll());
            return "Administrador"; // <-- Corrija aqui!
        }
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setAdmin(admin);
        usuarioRepository.save(usuario);
        return "redirect:/admin";
    }

    @PostMapping("/editar")
    public String editar(@RequestParam Long id, @RequestParam String username, @RequestParam(defaultValue = "false") boolean admin) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuario.setUsername(username);
            usuario.setAdmin(admin);
            usuarioRepository.save(usuario);
        }
        return "redirect:/admin";
    }

    @PostMapping("/desativar/{id}")
    public String desativar(@PathVariable Long id) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            usuario.setAtivo(false);
            usuarioRepository.save(usuario);
        });
        return "redirect:/admin";
    }

    @PostMapping("/reativar/{id}")
    public String reativar(@PathVariable Long id) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            usuario.setAtivo(true);
            usuarioRepository.save(usuario);
        });
        return "redirect:/admin";
    }

    @GetMapping("/pesquisar")
    public String pesquisarUsuarios(@RequestParam String busca, Model model) {
        var usuarios = usuarioRepository.findByUsernameContainingIgnoreCase(busca);
        model.addAttribute("usuarios", usuarios);

        if (usuarios.size() == 1) {
            Usuario usuario = usuarios.get(0);
            model.addAttribute("usuarioRelatorio", usuario);
            model.addAttribute("emprestimosRelatorio", emprestimoRepository.findByUsuario(usuario));
        } else {
            model.addAttribute("usuarioRelatorio", null);
            model.addAttribute("emprestimosRelatorio", null);
        }

        model.addAttribute("livros", livroLocalRepository.findAll());
        return "Administrador"; // <-- Corrija aqui!
    }

    @PostMapping("/admin/emprestimos/zerar-multa/{id}")
    public String zerarMulta(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Emprestimo> optEmp = emprestimoRepository.findById(id);
        if (optEmp.isPresent()) {
            Emprestimo emp = optEmp.get();
            emp.setMulta(BigDecimal.ZERO);
            emprestimoRepository.save(emp);
            redirectAttributes.addFlashAttribute("mensagem", "Multa zerada com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("mensagem", "Empréstimo não encontrado.");
        }
        return "redirect:/admin/emprestimos";
    }

    @PostMapping("/emprestimos/marcar-devolvido/{id}")
    public String marcarDevolvido(@PathVariable Long id, @RequestParam String busca, RedirectAttributes redirectAttributes) {
        Optional<Emprestimo> optEmp = emprestimoRepository.findById(id);
        if (optEmp.isPresent()) {
            Emprestimo emp = optEmp.get();
            emp.setDataDevolucaoReal(java.time.LocalDateTime.now());
            emprestimoRepository.save(emp);
            redirectAttributes.addFlashAttribute("mensagem", "Devolução marcada com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("mensagem", "Empréstimo não encontrado.");
        }
        return "redirect:/admin/pesquisar?busca=" + busca + "&aba=relatorios";
    }

    @PostMapping("/emprestimos/remover-devolucao/{id}")
    public String removerDevolucao(@PathVariable Long id, @RequestParam String busca, RedirectAttributes redirectAttributes) {
        Optional<Emprestimo> optEmp = emprestimoRepository.findById(id);
        if (optEmp.isPresent()) {
            Emprestimo emp = optEmp.get();
            emp.setDataDevolucaoReal(null);
            emprestimoRepository.save(emp);
            redirectAttributes.addFlashAttribute("mensagem", "Devolução removida com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("mensagem", "Empréstimo não encontrado.");
        }
        return "redirect:/admin/pesquisar?busca=" + busca + "&aba=relatorios";
    }

}