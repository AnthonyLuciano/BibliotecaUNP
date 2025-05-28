package unpestudantes.Sistema.biblioteca.controlador;

//bibliotecas do java/spring;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;
import unpestudantes.Sistema.biblioteca.modelo.Usuario;
import unpestudantes.Sistema.biblioteca.repositorio.UsuarioRepository;
import unpestudantes.Sistema.biblioteca.servico.EmailService;
import unpestudantes.Sistema.biblioteca.repositorio.EmprestimoRepository;
import unpestudantes.Sistema.biblioteca.servico.RecomendacaoService;
import unpestudantes.Sistema.biblioteca.repositorio.HistoricoLeituraRepository;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RecomendacaoService recomendacaoService;

    @Autowired
    private HistoricoLeituraRepository historicoLeituraRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    /**
     * Redireciona a raiz ("/") para a página de login.
     * Não recebe parâmetros e apenas faz redirect.
     * -Anthony
     */
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    /**
     * Exibe o formulário de login.
     * Comunica apenas com o template login.html.
     * -Anthony
     */
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    /**
     * Processa o login do usuário.
     * Busca usuário no banco (UsuarioRepository), valida senha e define sessão.
     * Redireciona para /admin ou /home conforme o tipo.
     * -Anthony
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isPresent() && passwordEncoder.matches(password, usuarioOpt.get().getPassword())) {
            Usuario usuario = usuarioOpt.get();
            if (!usuario.isEmailVerificado()) {
                model.addAttribute("username", usuario.getUsername());
                model.addAttribute("erro", "Você precisa confirmar seu e-mail para acessar o sistema.");
                return "confirmar-email";
            }
            session.setAttribute("usuarioLogado", usuario);
            if (usuario.isAdmin()) {
                return "redirect:/admin";
            } else {
                return "redirect:/home";
            }
        }
        model.addAttribute("erro", "Usuário ou senha inválidos");
        return "login";
    }

    /**
     * Exibe o formulário de cadastro.
     * Comunica apenas com o template cadastro.html.
     * -Anthony
     */
    @GetMapping("/cadastro")
    public String cadastroForm() {
        return "cadastro";
    }

    /**
     * Exibe a página inicial do usuário logado.
     * Adiciona o usuário ao model e pode futuramente recomendar livros.
     * -Anthony
     */
    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("recomendados", recomendacaoService.recomendarPorGenero(usuario));
        return "home";
    }  

    /**
     * Processa o cadastro do usuário.
     * Salva novo usuário no banco, gera código de verificação e envia e-mail (EmailService).
     * Redireciona para a tela de confirmação de e-mail.
     * -Anthony
     */
    @PostMapping("/cadastro")
    public String cadastro(@RequestParam String username, @RequestParam String email,
                       @RequestParam String password, Model model) {
        if (usuarioRepository.findByUsername(username).isPresent()) {
            model.addAttribute("erro", "Usuário já existe");
            return "cadastro";
        }
        String codigo = String.format("%06d", new java.util.Random().nextInt(999999));
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setEmailVerificado(false);
        usuario.setCodigoVerificacao(codigo);
        usuarioRepository.save(usuario);

        emailService.enviarCodigoVerificacao(email, codigo);

        model.addAttribute("username", username);
        return "confirmar-email";
    }

    /**
     * Exibe o formulário para o usuário digitar o código de confirmação.
     * Recebe o username como parâmetro e comunica com confirmar-email.html.
     * -Anthony
     */
    @GetMapping("/confirmar-email")
    public String confirmarEmailForm(@RequestParam String username, Model model) {
        model.addAttribute("username", username);
        return "confirmar-email";
    }

    /**
     * Gera e envia um novo código de verificação para o e-mail do usuário.
     * Atualiza o código no banco e mostra mensagem de sucesso.
     * Comunica com EmailService e UsuarioRepository.
     * -Anthony
     */
    @GetMapping("/reenviar-codigo")
    public String reenviarCodigo(@RequestParam String username, Model model) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String novoCodigo = String.format("%06d", new java.util.Random().nextInt(999999));
            usuario.setCodigoVerificacao(novoCodigo);
            usuarioRepository.save(usuario);
            emailService.enviarCodigoVerificacao(usuario.getEmail(), novoCodigo);
            model.addAttribute("mensagem", "Novo código enviado para seu e-mail.");
            model.addAttribute("username", username);
        } else {
            model.addAttribute("erro", "Usuário não encontrado.");
        }
        return "confirmar-email";
    }

    /**
     * Processa a confirmação do código enviado por e-mail.
     * Valida o código, ativa o e-mail do usuário e redireciona para login.
     * Comunica com UsuarioRepository.
     * -Anthony
     */
    @PostMapping("/confirmar-email")
    public String confirmarEmail(@RequestParam String username, @RequestParam String codigo, Model model) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isPresent() && usuarioOpt.get().getCodigoVerificacao().equals(codigo)) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEmailVerificado(true);
            usuario.setCodigoVerificacao(null);
            usuarioRepository.save(usuario);
            return "redirect:/login";
        }
        model.addAttribute("erro", "Código inválido");
        model.addAttribute("username", username);
        return "confirmar-email";
    }

    /**
     * Exibe o perfil do usuário.
     * Carrega informações do usuário e seus empréstimos.
     * -Anthony
     */
    @GetMapping("/perfil")
    public String perfil(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";
        model.addAttribute("usuario", usuario);
        model.addAttribute("emprestimos", emprestimoRepository.findByUsuario(usuario));
        model.addAttribute("historico", historicoLeituraRepository.findByUsuarioOrderByDataAcessoDesc(usuario));
        model.addAttribute("recomendados", recomendacaoService.recomendarPorGenero(usuario));
        // ... outros atributos
        return "perfil";
    }

    /**
     * Atualiza a foto de perfil do usuário.
     * Recebe o arquivo da foto, salva em disco e atualiza o usuário.
     * -Anthony
     */
    @PostMapping("/perfil/foto")
    public String uploadFoto(@RequestParam("foto") MultipartFile file, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null || file.isEmpty()) return "redirect:/perfil";

        try {
            String pasta = "src/main/resources/static/img/fotos/";
            Files.createDirectories(Paths.get(pasta));
            String nomeArquivo = "user_" + usuario.getId() + "_" + file.getOriginalFilename();
            Path caminho = Paths.get(pasta + nomeArquivo);
            file.transferTo(caminho);

            usuario.setFotoUrl("/img/fotos/" + nomeArquivo);
            // Salve o usuário no banco (adicione o repository se necessário)
            usuarioRepository.save(usuario);
            session.setAttribute("usuarioLogado", usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/perfil";
    }
}
