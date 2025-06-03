package unpestudantes.sistema.biblioteca.servico;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import unpestudantes.sistema.biblioteca.modelo.emprestimo.Emprestimo;
import unpestudantes.sistema.biblioteca.modelo.usuario.Usuario;
import unpestudantes.sistema.biblioteca.repositorio.EmprestimoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@SuppressWarnings("unused")
class LembreteServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private LembreteService lembreteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEnviarLembretes() {
        // Arrange
        LocalDate hoje = LocalDate.now();
        LocalDateTime dataDevolucaoPrevistaTresDiasDepois = hoje.plusDays(3).atStartOfDay();
        LocalDateTime dataDevolucaoPrevistaUmDiaDepois = hoje.plusDays(1).atStartOfDay();

        Usuario usuario = new Usuario();
        usuario.setEmail("teste@exemplo.com");

        Emprestimo empTresDias = new Emprestimo();
        empTresDias.setDataDevolucaoPrevista(dataDevolucaoPrevistaTresDiasDepois);
        empTresDias.setDataDevolucaoReal(null);
        empTresDias.setTitulo("Livro 3 Dias");
        empTresDias.setUsuario(usuario);

        Emprestimo empUmDia = new Emprestimo();
        empUmDia.setDataDevolucaoPrevista(dataDevolucaoPrevistaUmDiaDepois);
        empUmDia.setDataDevolucaoReal(null);
        empUmDia.setTitulo("Livro 1 Dia");
        empUmDia.setUsuario(usuario);

        Emprestimo empForaPrazo = new Emprestimo();
        empForaPrazo.setDataDevolucaoPrevista(hoje.plusDays(5).atStartOfDay());
        empForaPrazo.setDataDevolucaoReal(null);
        empForaPrazo.setTitulo("Livro Fora Prazo");
        empForaPrazo.setUsuario(usuario);

        List<Emprestimo> emprestimos = Arrays.asList(empTresDias, empUmDia, empForaPrazo);

        when(emprestimoRepository.findAll()).thenReturn(emprestimos);

        // Act
        lembreteService.enviarLembretes();

        // Assert
        verify(emailService, times(1)).enviarLembreteDevolucao(
                "teste@exemplo.com",
                "Livro 3 Dias",
                dataDevolucaoPrevistaTresDiasDepois.toLocalDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
        verify(emailService, times(1)).enviarLembreteDevolucao(
                "teste@exemplo.com",
                "Livro 1 Dia",
                dataDevolucaoPrevistaUmDiaDepois.toLocalDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
        verify(emailService, never()).enviarLembreteDevolucao(
                eq("teste@exemplo.com"),
                eq("Livro Fora Prazo"),
                anyString()
        );

        // Logs para depuração
        System.out.println("Testando envio de lembretes:");
        for (Emprestimo emp : emprestimos) {
            boolean deveEnviar = false;
            LocalDate dataDevolucao = emp.getDataDevolucaoPrevista().toLocalDate();
            if (dataDevolucao.minusDays(3).isEqual(hoje) || dataDevolucao.minusDays(1).isEqual(hoje)) {
                deveEnviar = true;
            }
            System.out.println("Emprestimo: " + emp.getTitulo() +
                    " | Data Prevista: " + emp.getDataDevolucaoPrevista() +
                    " | Deve Enviar: " + deveEnviar);
        }
        System.out.println("✅ [LembreteServiceTest] Lembretes enviados corretamente para os prazos de 3 e 1 dia.");
    }
}
