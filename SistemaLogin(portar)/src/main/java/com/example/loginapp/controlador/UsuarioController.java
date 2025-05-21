package com.example.loginapp.controlador;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.loginapp.model.Usuario;
import com.example.loginapp.modelo.Usuario;
import com.example.loginapp.repositorio.UsuarioRepository;

import java.util.List;
import jakarta.servlet.http.HttpSession;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isPresent() && password.equals(usuarioOpt.get().getPassword())) {
            session.setAttribute("usuarioLogado", usuarioOpt.get());
            if (usuarioOpt.get().isAdmin()) {
                return "redirect:/admin";
            } else {
                return "cliente";
            }
        }
        model.addAttribute("erro", "Usuário ou senha inválidos");
        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastroForm() {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastro(@RequestParam String username, @RequestParam String password, @RequestParam(defaultValue = "false") boolean admin, Model model) {
        if (usuarioRepository.findByUsername(username).isPresent()) {
            model.addAttribute("erro", "Usuário já existe");
            return "cadastro";
        }
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setAdmin(admin); 
        usuarioRepository.save(usuario);
        return "redirect:/login";
    }

@GetMapping("/cliente")
public String clienteView(Model model, HttpSession session) {
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
    if (usuario != null) {
        model.addAttribute("isAdmin", usuario.isAdmin());
    }
    return "cliente";
}
    public class GestaoUsuarios {
    public static void aprovarUsuario(Usuario usuario) {
        usuario.setAprovado(true);
        System.out.println("Usuário aprovado: " + usuario.getNome());
    }

    public static void listarUsuariosPendentes(List<Usuario> usuarios) {
        System.out.println("=== Usuários Pendentes de Aprovação ===");
        for (Usuario u : usuarios) {
            if (!u.isAprovado()) {
                System.out.println("ID: " + u.getId() + " | Nome: " + u.getNome());
            }
        }
    }
}
}
