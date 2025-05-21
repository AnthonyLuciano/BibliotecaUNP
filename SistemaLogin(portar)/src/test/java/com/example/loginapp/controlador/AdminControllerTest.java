package com.example.loginapp.controlador;

import com.example.loginapp.modelo.Usuario;
import com.example.loginapp.repositorio.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

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
    }

    @Test
    void cadastrar_DeveCadastrarNovoUsuarioERedirecionar() {
        when(usuarioRepository.findByUsername("novo")).thenReturn(Optional.empty());

        String view = adminController.cadastrar("novo", "senha", true, model);

        verify(usuarioRepository).save(any(Usuario.class));
        assertEquals("redirect:/admin", view);
    }

    @Test
    void cadastrar_DeveRetornarErroSeUsuarioExistir() {
        when(usuarioRepository.findByUsername("existente")).thenReturn(Optional.of(new Usuario()));

        String view = adminController.cadastrar("existente", "senha", false, model);

        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(model).addAttribute(eq("erro"), anyString());
        assertEquals("Administrador", view);
    }

    @Test
    void editar_DeveEditarUsuarioExistente() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        String view = adminController.editar(1L, "editado", true);

        verify(usuarioRepository).save(usuario);
        assertEquals("redirect:/admin", view);
    }

    @Test
    void excluir_DeveExcluirUsuarioPorId() {
        String view = adminController.excluir(2L);

        verify(usuarioRepository).deleteById(2L);
        assertEquals("redirect:/admin", view);
    }

    @Test
    void pesquisarUsuarios_DeveAdicionarUsuariosAoModelERetornarAdministrador() {
        when(usuarioRepository.findByUsernameContainingIgnoreCase("busca")).thenReturn(Arrays.asList(new Usuario()));

        String view = adminController.pesquisarUsuarios("busca", model);

        verify(model).addAttribute(eq("usuarios"), any());
        assertEquals("Administrador", view);
    }
}