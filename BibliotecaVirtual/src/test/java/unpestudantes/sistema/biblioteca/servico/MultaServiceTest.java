package unpestudantes.sistema.biblioteca.servico;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import unpestudantes.sistema.biblioteca.modelo.emprestimo.Emprestimo;
import unpestudantes.sistema.biblioteca.repositorio.EmprestimoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MultaServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @InjectMocks
    private MultaService multaService;

    @Test
    void atualizarMultas_DeveCalcularMultaCorretamente() {
        Emprestimo emAtraso = new Emprestimo();
        emAtraso.setId(1L);
        emAtraso.setDataDevolucaoPrevista(LocalDate.now().minusDays(3).atStartOfDay());
        emAtraso.setDataDevolucaoReal(null);
        emAtraso.setMulta(BigDecimal.ZERO);

        Emprestimo emDia = new Emprestimo();
        emDia.setId(2L);
        emDia.setDataDevolucaoPrevista(LocalDate.now().plusDays(2).atStartOfDay());
        emDia.setDataDevolucaoReal(null);
        emDia.setMulta(BigDecimal.ZERO);

        List<Emprestimo> lista = Arrays.asList(emAtraso, emDia);

        when(emprestimoRepository.findAll()).thenReturn(lista);

        multaService.atualizarMultas();

        // 3 dias de atraso x R$2,00 = R$6,00
        assertEquals(BigDecimal.valueOf(6.0), emAtraso.getMulta());
        // Em dia, multa deve ser zero
        assertEquals(BigDecimal.ZERO, emDia.getMulta());

        // Só salva o empréstimo em atraso
        verify(emprestimoRepository).save(emAtraso);
        verify(emprestimoRepository, never()).save(emDia);

        System.out.println("✅ [MultaServiceTest] Multa calculada corretamente para empréstimos em atraso e em dia.");
    }

    @Test
    void atualizarMultas_DeveCalcularMultaParaVariosDiasDeAtraso() {
        for (int diasAtraso = 1; diasAtraso <= 5; diasAtraso++) {
            Emprestimo emAtraso = new Emprestimo();
            emAtraso.setId((long) diasAtraso);
            emAtraso.setDataDevolucaoPrevista(LocalDate.now().minusDays(diasAtraso).atStartOfDay());
            emAtraso.setDataDevolucaoReal(null);
            emAtraso.setMulta(BigDecimal.ZERO);

            List<Emprestimo> lista = Arrays.asList(emAtraso);

            when(emprestimoRepository.findAll()).thenReturn(lista);

            multaService.atualizarMultas();

            BigDecimal esperado = BigDecimal.valueOf(diasAtraso * 2.0);
            assertEquals(esperado, emAtraso.getMulta(),
                "Multa para " + diasAtraso + " dias de atraso deveria ser R$" + esperado);

            verify(emprestimoRepository).save(emAtraso);

            System.out.println("✅ [MultaServiceTest] Multa correta para " + diasAtraso + " dias de atraso: R$" + esperado);

            // Limpa as interações do mock para o próximo loop
            clearInvocations(emprestimoRepository);
        }
    }
}
