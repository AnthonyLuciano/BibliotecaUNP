package com.example.loginapp.controlador;

import com.example.loginapp.modelo.Usuario;
import com.example.loginapp.repositorio.UsuarioRepository;
import com.example.loginapp.service.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private Model model;

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
    }

    @Test
    void cadastro_DeveRetornarCadastro_QuandoUsuarioJaExiste() {
        when(usuarioRepository.findByUsername("existente")).thenReturn(Optional.of(new Usuario()));

        String view = usuarioController.cadastro("existente", "existente@email.com", "senha123", model);

        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(emailService, never()).enviarCodigoVerificacao(anyString(), anyString());
        assertEquals("cadastro", view);
    }
}