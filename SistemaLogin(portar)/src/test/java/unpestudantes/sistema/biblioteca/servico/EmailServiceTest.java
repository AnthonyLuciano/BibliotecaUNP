package unpestudantes.sistema.biblioteca.servico;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import unpestudantes.sistema.biblioteca.servico.EmailService;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unused")
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

        assertNotNull(mensagemEnviada.getTo(), "Destinatário não pode ser nulo");
        String[] destinatarios = mensagemEnviada.getTo();
        assertNotNull(destinatarios, "Destinatários não pode ser nulo");
        assertTrue(destinatarios.length > 0, "Destinatário deve ter pelo menos um e-mail");
        assertEquals(destinatario, destinatarios[0]);

        assertNotNull(mensagemEnviada.getText(), "Texto do e-mail não pode ser nulo");
        String textoEmail = mensagemEnviada.getText();
        assertNotNull(textoEmail, "Texto do e-mail não pode ser nulo");
        assertTrue(textoEmail.contains(codigo));
        System.out.println("✅ [EmailServiceTest] E-mail enviado com código correto!");
    }

    @Test
    void enviarCodigoVerificacao_DeveEnviarEmailComSucesso() {
        emailService.enviarCodigoVerificacao("teste@email.com", "123456");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage mensagemEnviada = captor.getValue();
        assertNotNull(mensagemEnviada.getTo());
        assertNotNull(mensagemEnviada.getText());
        System.out.println("✅ [EmailService] E-mail enviado com sucesso!");
    }

    @Test
    void enviarCodigoVerificacao_DeveLancarExcecaoSeMailFalhar() {
        doThrow(new RuntimeException("Falha ao enviar")).when(mailSender).send(any(SimpleMailMessage.class));

        try {
            emailService.enviarCodigoVerificacao("teste@email.com", "123456");
            fail("Deveria lançar exceção ao enviar e-mail");
        } catch (RuntimeException e) {
            System.out.println("✅ [EmailService] Erro esperado ao enviar e-mail capturado: " + e.getMessage());
        }
    }
}