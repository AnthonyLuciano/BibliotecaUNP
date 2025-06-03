package unpestudantes.sistema.biblioteca.servico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import unpestudantes.sistema.biblioteca.modelo.emprestimo.Emprestimo;
import unpestudantes.sistema.biblioteca.repositorio.EmprestimoRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class LembreteService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private EmailService emailService;

    // Executa todo dia às 8h da manhã
    @Scheduled(cron = "0 0 9 * * *")
    public void enviarLembretes() {
        LocalDate hoje = LocalDate.now();

        List<Emprestimo> emprestimos = emprestimoRepository.findAll();
        for (Emprestimo emp : emprestimos) {
            if (emp.getDataDevolucaoPrevista() != null && emp.getDataDevolucaoReal() == null) {
                LocalDate dataDevolucao = emp.getDataDevolucaoPrevista().toLocalDate();
                // Se hoje for igual a dataDevolucao - 3 dias OU dataDevolucao - 1 dia
                if (hoje.isEqual(dataDevolucao.minusDays(3)) || hoje.isEqual(dataDevolucao.minusDays(1))) {
                    String email = emp.getUsuario().getEmail();
                    String nomeLivro = emp.getTitulo();
                    String dataDevolucaoStr = dataDevolucao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    emailService.enviarLembreteDevolucao(email, nomeLivro, dataDevolucaoStr);
                }
            }
        }
    }
}
