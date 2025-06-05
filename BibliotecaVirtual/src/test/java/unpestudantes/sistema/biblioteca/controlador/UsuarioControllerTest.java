package unpestudantes.sistema.biblioteca.controlador;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;
import unpestudantes.sistema.biblioteca.controlador.usuario.UsuarioController;
import unpestudantes.sistema.biblioteca.modelo.usuario.Usuario;
import unpestudantes.sistema.biblioteca.repositorio.UsuarioRepository;
import unpestudantes.sistema.biblioteca.servico.EmailService;
import unpestudantes.sistema.biblioteca.servico.OpenLibraryService;
import unpestudantes.sistema.biblioteca.servico.RecomendacaoService;
import unpestudantes.sistema.biblioteca.modelo.livro.LivroOpenLibrary;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Model model;

    @Mock
    private OpenLibraryService openLibraryService;

    @Mock
    private EmailService emailService;

    @Mock
    private RecomendacaoService recomendacaoService;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cadastro_DeveRetornarConfirmarEmail_QuandoUsuarioNovo() {
        when(usuarioRepository.findByUsername("novoUsuario")).thenReturn(Optional.empty());

        String view = usuarioController.cadastro("novoUsuario", "novo@email.com", "senha123", model);

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(emailService, times(1)).enviarCodigoVerificacao(eq("novo@email.com"), anyString());
        assertEquals("confirmar-email", view);
    }

    @Test
    void cadastro_DeveRetornarCadastro_QuandoUsuarioJaExiste() {
        when(usuarioRepository.findByUsername("existente")).thenReturn(Optional.of(new Usuario()));

        String view = usuarioController.cadastro("existente", "existente@email.com", "senha123", model);

        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(emailService, never()).enviarCodigoVerificacao(anyString(), anyString());
        assertEquals("cadastro", view);
    }

    @Test
    void cadastro_DeveCadastrarUsuarioComSucesso() {
        when(usuarioRepository.findByUsername("joao")).thenReturn(Optional.empty());

        String view = usuarioController.cadastro("joao", "joao@email.com", "senha123", model);

        verify(usuarioRepository).save(any(Usuario.class));
        assertEquals("confirmar-email", view);
    }

    @Test
    void cadastro_DeveTratarSqlInjectionComoTexto() {
        String injecao = "usuario' OR '1'='1";
        when(usuarioRepository.findByUsername(injecao)).thenReturn(Optional.empty());

        String view = usuarioController.cadastro(injecao, "email@teste.com", "senha123", model);

        verify(usuarioRepository).save(any(Usuario.class));
        assertEquals("confirmar-email", view);
    }

    @Test
    void login_DeveFalharComSenhaIncorreta() {
        Usuario usuario = new Usuario();
        usuario.setUsername("joao");
        usuario.setPassword("senha123");
        when(usuarioRepository.findByUsername("joao")).thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        jakarta.servlet.http.HttpSession session = mock(jakarta.servlet.http.HttpSession.class);
        String view = usuarioController.login("joao", "senhaErrada", model, session);

        assertEquals("login", view);
    }

    @Test
    void homeDeveAdicionarTodosAtributosNoModel() {
        Usuario usuario = new Usuario();
        jakarta.servlet.http.HttpSession session = mock(jakarta.servlet.http.HttpSession.class);
        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);

        when(recomendacaoService.recomendarPorGeneroEAutor(usuario)).thenReturn(Collections.emptyList());
        when(openLibraryService.buscarLivrosPorGenero(anyString()))
            .thenReturn(List.of(new LivroOpenLibrary()));

        String view = usuarioController.home(model, session);

        verify(model).addAttribute(eq("usuario"), eq(usuario));
        verify(model).addAttribute(eq("recomendados"), anyList());
        verify(model).addAttribute(eq("generosPopulares"), anyList());
        verify(model).addAttribute(eq("livrosPorGenero"), anyList());
        assertEquals("home", view);
    }
}