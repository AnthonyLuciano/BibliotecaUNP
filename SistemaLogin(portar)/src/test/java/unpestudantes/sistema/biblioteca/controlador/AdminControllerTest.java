package unpestudantes.sistema.biblioteca.controlador;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

import unpestudantes.sistema.biblioteca.controlador.AdminController;
import unpestudantes.sistema.biblioteca.modelo.Usuario;
import unpestudantes.sistema.biblioteca.repositorio.UsuarioRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Model model;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void painel_DeveAdicionarUsuariosAoModelERetornarAdministrador() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(new Usuario(), new Usuario()));

        String view = adminController.painel(model);

        verify(model).addAttribute(eq("usuarios"), any());
        assertEquals("Administrador", view);
        System.out.println("✅ [AdminController] Painel carregado e usuários adicionados ao model!");
    }

    @Test
    void cadastrar_DeveCadastrarNovoUsuarioERedirecionar() {
        when(usuarioRepository.findByUsername("novo")).thenReturn(Optional.empty());

        String view = adminController.cadastrar("novo", "senha", true, model);

        verify(usuarioRepository).save(any(Usuario.class));
        assertEquals("redirect:/admin", view);
        System.out.println("✅ [AdminController] Novo usuário cadastrado e redirecionado!");
    }

    @Test
    void cadastrar_DeveRetornarErroSeUsuarioExistir() {
        when(usuarioRepository.findByUsername("existente")).thenReturn(Optional.of(new Usuario()));

        String view = adminController.cadastrar("existente", "senha", false, model);

        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(model).addAttribute(eq("erro"), anyString());
        assertEquals("Administrador", view);
        System.out.println("✅ [AdminController] Cadastro bloqueado para usuário já existente!");
    }

    @Test
    void editar_DeveEditarUsuarioExistente() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        String view = adminController.editar(1L, "editado", true);

        verify(usuarioRepository).save(usuario);
        assertEquals("redirect:/admin", view);
        System.out.println("✅ [AdminController] Usuário editado com sucesso!");
    }

    @Test
    void excluir_DeveExcluirUsuarioPorId() {
        String view = adminController.excluir(2L);

        verify(usuarioRepository).deleteById(2L);
        assertEquals("redirect:/admin", view);
        System.out.println("✅ [AdminController] Usuário excluído com sucesso!");
    }

    @Test
    void pesquisarUsuarios_DeveAdicionarUsuariosAoModelERetornarAdministrador() {
        when(usuarioRepository.findByUsernameContainingIgnoreCase("busca")).thenReturn(Arrays.asList(new Usuario()));

        String view = adminController.pesquisarUsuarios("busca", model);

        verify(model).addAttribute(eq("usuarios"), any());
        assertEquals("Administrador", view);
        System.out.println("✅ [AdminController] Pesquisa de usuários realizada com sucesso!");
    }

    @Test
    void cadastrar_DeveCadastrarAdminComSucesso() {
        when(usuarioRepository.findByUsername("func1")).thenReturn(Optional.empty());

        String view = adminController.cadastrar("func1", "senha123", true, model);

        verify(usuarioRepository).save(any(Usuario.class));
        System.out.println("✅ [AdminController] Cadastro de admin realizado com sucesso!");
        assertEquals("redirect:/admin", view);
    }

    @Test
    void cadastrar_DeveTratarSqlInjectionComoTexto() {
        String injecao = "admin' OR '1'='1";
        when(usuarioRepository.findByUsername(injecao)).thenReturn(Optional.empty());

        String view = adminController.cadastrar(injecao, "senha", false, model);

        verify(usuarioRepository).save(any(Usuario.class));
        System.out.println("✅ [AdminController] SQL Injection tratado como texto.");
        assertEquals("redirect:/admin", view);
    }
}