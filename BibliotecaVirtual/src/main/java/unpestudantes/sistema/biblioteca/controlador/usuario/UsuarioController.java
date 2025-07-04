package unpestudantes.sistema.biblioteca.controlador.usuario;

//bibliotecas do java/spring;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;
import unpestudantes.sistema.biblioteca.modelo.emprestimo.Emprestimo;
import unpestudantes.sistema.biblioteca.modelo.livro.ListaLivro;
import unpestudantes.sistema.biblioteca.modelo.livro.LivroOpenLibrary;
import unpestudantes.sistema.biblioteca.modelo.usuario.Usuario;
import unpestudantes.sistema.biblioteca.repositorio.EmprestimoRepository;
import unpestudantes.sistema.biblioteca.repositorio.UsuarioRepository;
import unpestudantes.sistema.biblioteca.repositorio.ListaLivroRepository;
import unpestudantes.sistema.biblioteca.servico.EmailService;
import unpestudantes.sistema.biblioteca.servico.OpenLibraryService;
import unpestudantes.sistema.biblioteca.servico.RecomendacaoService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;


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
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private ListaLivroRepository listaLivroRepository;

    @Autowired
    private OpenLibraryService openLibraryService;

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
            if (!usuario.isAtivo()) {
                model.addAttribute("erro", "Sua conta está inativa. Entre em contato com o suporte.");
                return "login";
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
        model.addAttribute("recomendados", recomendacaoService.recomendarPorGeneroEAutor(usuario));
        
        List<String> generosPopulares = Arrays.asList(
            "science_fiction", "romance", "biography", "history", "self_help",
            "fantasy", "technology", "psychology", "thriller", "education"
        );

        List<LivroOpenLibrary> livrosPorGenero = new ArrayList<>();
        for (String genero : generosPopulares) {
            List<LivroOpenLibrary> livros = openLibraryService.buscarLivrosPorGenero(genero);
            if (livros != null && !livros.isEmpty()) {
                LivroOpenLibrary escolhido = livros.get(new Random().nextInt(livros.size()));
                livrosPorGenero.add(escolhido);
            } else {
                livrosPorGenero.add(new LivroOpenLibrary()); // vazio, para manter o índice
            }
        }
        model.addAttribute("generosPopulares", generosPopulares);
        model.addAttribute("livrosPorGenero", livrosPorGenero);
        
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
        model.addAttribute("usuario", usuario); // <-- ESSENCIAL
        List<ListaLivro> listas = listaLivroRepository.findByUsuario(usuario);
        Set<String> nomesListas = listas.stream()
            .map(ListaLivro::getNomeLista)
            .collect(Collectors.toSet());
        Map<String, List<ListaLivro>> listasPorNome = listas.stream()
            .collect(Collectors.groupingBy(ListaLivro::getNomeLista));
        model.addAttribute("nomesListas", nomesListas);
        model.addAttribute("listas", listas);
        model.addAttribute("listasPorNome", listasPorNome);

        // Histórico de empréstimos
        List<Emprestimo> emprestimos = emprestimoRepository.findByUsuario(usuario);
        model.addAttribute("emprestimos", emprestimos);

        System.out.println("Listas encontradas para o usuário: " + listas.size());
        listas.forEach(l -> System.out.println(l.getTitulo() + " | " + l.getNomeLista()));
        System.out.println("nomesListas:");
        nomesListas.forEach(System.out::println);
        System.out.println("listasPorNome keys:");
        listasPorNome.keySet().forEach(System.out::println);

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
            // Caminho absoluto da pasta static/img/fotos
            String staticDir = new File("uploads/fotos").getAbsolutePath();
            Files.createDirectories(Paths.get(staticDir));
            String extensao = "";
            String original = file.getOriginalFilename();
            if (original != null && original.contains(".")) {
                extensao = original.substring(original.lastIndexOf('.'));
            }
            String nomeArquivo = "user_" + usuario.getId() + "_" + UUID.randomUUID() + extensao;
            Path caminho = Paths.get(staticDir, nomeArquivo);
            file.transferTo(caminho.toFile());

            usuario.setFotoUrl("/uploads/fotos/" + nomeArquivo);
            usuarioRepository.save(usuario);
            session.setAttribute("usuarioLogado", usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/perfil";
    }

    /**
     * Atualiza a senha do usuário.
     * Recebe a senha atual, nova senha e confirmação.
     * Valida e altera a senha, mostrando mensagem de sucesso ou erro.
     * -Anthony
     */
    @PostMapping("/perfil/trocar-senha")
    public String trocarSenha(
        @RequestParam String senhaAtual,
        @RequestParam String novaSenha,
        @RequestParam String confirmarSenha,
        Model model,
        HttpSession session
    ) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";

        // Valida senha atual
        if (!passwordEncoder.matches(senhaAtual, usuario.getPassword())) {
            model.addAttribute("erroSenha", "Senha atual incorreta.");
        } else if (!novaSenha.equals(confirmarSenha)) {
            model.addAttribute("erroSenha", "Nova senha e confirmação não coincidem.");
        } else {
            usuario.setPassword(passwordEncoder.encode(novaSenha));
            usuarioRepository.save(usuario);
            session.setAttribute("usuarioLogado", usuario);
            model.addAttribute("msgSenha", "Senha alterada com sucesso!");
        }

        // Recarrega dados do perfil
        List<ListaLivro> listas = listaLivroRepository.findByUsuario(usuario);
        model.addAttribute("usuario", usuario);
        model.addAttribute("listas", listas);
        Map<String, List<ListaLivro>> listasPorNome = listas.stream()
            .collect(Collectors.groupingBy(ListaLivro::getNomeLista));
        model.addAttribute("listasPorNome", listasPorNome);
        model.addAttribute("nomesListas", listasPorNome.keySet());
        List<Emprestimo> emprestimos = emprestimoRepository.findByUsuario(usuario);
        model.addAttribute("emprestimos", emprestimos);

        return "perfil";
    }

}
