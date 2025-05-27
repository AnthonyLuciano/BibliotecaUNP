package unpestudantes.Sistema.biblioteca.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import unpestudantes.Sistema.biblioteca.modelo.Usuario;
import unpestudantes.Sistema.biblioteca.repositorio.UsuarioRepository;
/*
 * @author anthony
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;
/*
 * literalmente um painel de administração, nao tem muito o que explicar
 * a nao ser CRUD basico
 */
    @GetMapping
    public String painel(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "Administrador";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@RequestParam String username, @RequestParam String password, @RequestParam(defaultValue = "false") boolean admin, Model model) {
        if (usuarioRepository.findByUsername(username).isPresent()) {
            model.addAttribute("erro", "Usuário já existe");
            model.addAttribute("usuarios", usuarioRepository.findAll());
            return "Administrador";
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

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/pesquisar")
    public String pesquisarUsuarios(@RequestParam String busca, Model model) {
        var usuarios = usuarioRepository.findByUsernameContainingIgnoreCase(busca);
        model.addAttribute("usuarios", usuarios);
        return "Administrador";
    }
}
