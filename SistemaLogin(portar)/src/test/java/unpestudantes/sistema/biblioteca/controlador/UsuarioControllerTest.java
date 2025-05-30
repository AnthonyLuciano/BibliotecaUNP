package unpestudantes.sistema.biblioteca.controlador;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import unpestudantes.sistema.biblioteca.controlador.usuario.UsuarioController;
import unpestudantes.sistema.biblioteca.modelo.usuario.Usuario;
import unpestudantes.sistema.biblioteca.repositorio.UsuarioRepository;
import unpestudantes.sistema.biblioteca.servico.EmailService;
import unpestudantes.sistema.biblioteca.servico.OpenLibraryService;
import unpestudantes.sistema.biblioteca.modelo.livro.LivroOpenLibrary;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    @InjectMocks
    private UsuarioController usuarioController;

    public UsuarioControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cadastro_DeveRetornarConfirmarEmail_QuandoUsuarioNovo() {
        when(usuarioRepository.findByUsername("novoUsuario")).thenReturn(Optional.empty());

        String view = usuarioController.cadastro("novoUsuario", "novo@email.com", "senha123", model);

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(emailService, times(1)).enviarCodigoVerificacao(eq("novo@email.com"), anyString());
        assertEquals("confirmar-email", view);
        System.out.println("✅ [UsuarioController] Cadastro de novo usuário realizado e confirmação de e-mail enviada!");
    }

    @Test
    void cadastro_DeveRetornarCadastro_QuandoUsuarioJaExiste() {
        when(usuarioRepository.findByUsername("existente")).thenReturn(Optional.of(new Usuario()));

        String view = usuarioController.cadastro("existente", "existente@email.com", "senha123", model);

        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(emailService, never()).enviarCodigoVerificacao(anyString(), anyString());
        assertEquals("cadastro", view);
        System.out.println("✅ [UsuarioController] Cadastro bloqueado para usuário já existente!");
    }

    @Test
    void cadastro_DeveCadastrarUsuarioComSucesso() {
        when(usuarioRepository.findByUsername("joao")).thenReturn(Optional.empty());

        String view = usuarioController.cadastro("joao", "joao@email.com", "senha123", model);

        verify(usuarioRepository).save(any(Usuario.class));
        System.out.println("✅ [UsuarioController] Cadastro de usuário realizado com sucesso!");
        assertEquals("confirmar-email", view);
    }

    @Test
    void cadastro_DeveTratarSqlInjectionComoTexto() {
        String injecao = "usuario' OR '1'='1";
        when(usuarioRepository.findByUsername(injecao)).thenReturn(Optional.empty());

        String view = usuarioController.cadastro(injecao, "email@teste.com", "senha123", model);

        verify(usuarioRepository).save(any(Usuario.class));
        System.out.println("✅ [UsuarioController] SQL Injection tratado como texto.");
        assertEquals("confirmar-email", view);
    }

    @Test
    void login_DeveFalharComSenhaIncorreta() {
        Usuario usuario = new Usuario();
        usuario.setUsername("joao");
        usuario.setPassword("senha123");
        when(usuarioRepository.findByUsername("joao")).thenReturn(Optional.of(usuario));

        jakarta.servlet.http.HttpSession session = mock(jakarta.servlet.http.HttpSession.class);
        String view = usuarioController.login("joao", "senhaErrada", model, session);

        System.out.println("✅ [UsuarioController] Login falhou como esperado com senha incorreta.");
        assertEquals("login", view);
    }

    @Test
    void homeDeveAdicionarLivrosPorGeneroEAtributosDeGenero() {
        Usuario usuario = new Usuario();
        jakarta.servlet.http.HttpSession session = mock(jakarta.servlet.http.HttpSession.class);
        when(session.getAttribute("usuarioLogado")).thenReturn(usuario);

        List<String> generosEN = Arrays.asList("science_fiction", "romance");
        List<String> generosPT = Arrays.asList("Ficção Científica", "Romance");

        // Simula retorno do serviço para cada gênero
        when(openLibraryService.buscarLivrosPorGenero(anyString()))
            .thenReturn(List.of(new LivroOpenLibrary()));

        // Chama o método
        String view = usuarioController.home(model, session);

        // Verifica se adicionou os atributos certos
        verify(model).addAttribute(eq("generosPopulares"), anyList());
        verify(model).addAttribute(eq("livrosPorGenero"), anyList());
        assertEquals("home", view);
    }
}