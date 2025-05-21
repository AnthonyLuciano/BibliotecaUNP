package com.example.loginapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class EmailServiceTest {

    private JavaMailSender mailSender;
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        mailSender = mock(JavaMailSender.class);
        emailService = new EmailService();
        // Usando reflexão para injetar o mock, já que o campo é private
        java.lang.reflect.Field field;
        try {
            field = EmailService.class.getDeclaredField("mailSender");
            field.setAccessible(true);
            field.set(emailService, mailSender);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void enviarCodigoVerificacao_DeveEnviarEmailComCodigoCorreto() {
        String destinatario = "teste@email.com";
        String codigo = "123456";

        emailService.enviarCodigoVerificacao(destinatario, codigo);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage mensagemEnviada = captor.getValue();
        assertEquals(destinatario, mensagemEnviada.getTo()[0]);
        assertEquals("Código de verificação - Biblioteca UnP", mensagemEnviada.getSubject());
        assertTrue(mensagemEnviada.getText().contains(codigo));
    }
}