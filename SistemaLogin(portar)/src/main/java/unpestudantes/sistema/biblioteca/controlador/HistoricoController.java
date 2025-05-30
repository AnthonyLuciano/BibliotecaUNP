package unpestudantes.sistema.biblioteca.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import unpestudantes.sistema.biblioteca.modelo.Usuario;
import unpestudantes.sistema.biblioteca.repositorio.HistoricoLeituraRepository;

@Controller
public class HistoricoController {

    @Autowired
    private HistoricoLeituraRepository historicoLeituraRepository;

    @GetMapping("/historico")
    public String historicoUsuario(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/login";
        }
        model.addAttribute("historico", historicoLeituraRepository.findByUsuarioOrderByDataAcessoDesc(usuario));
        return "historico";
    }
}
