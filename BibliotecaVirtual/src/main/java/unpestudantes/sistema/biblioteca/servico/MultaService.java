package unpestudantes.sistema.biblioteca.servico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import unpestudantes.sistema.biblioteca.repositorio.EmprestimoRepository;
import unpestudantes.sistema.biblioteca.modelo.emprestimo.Emprestimo;


public class MultaService {
    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void atualizarMultas(){
        List<Emprestimo> emprestimos = emprestimoRepository.findAll();
        LocalDate hoje = LocalDate.now();

        for (Emprestimo emp : emprestimos){
            if (emp.getDataDevolucaoPrevista() != null && emp.getDataDevolucaoReal() == null) {
                LocalDate dataPrevista = emp.getDataDevolucaoPrevista().toLocalDate();
                if (hoje.isAfter(dataPrevista)) {
                    long diasAtraso = ChronoUnit.DAYS.between(dataPrevista, hoje);
                    BigDecimal valorMulta = BigDecimal.valueOf(diasAtraso).multiply(BigDecimal.valueOf(2.0)); // 2.0 é o valor da multa por dia de atraso
                    emp.setMulta(valorMulta);
                    emprestimoRepository.save(emp); // Salva a multa no empréstimo
                } else {
                    if (emp.getMulta().compareTo(BigDecimal.ZERO) != 0) {
                        emp.setMulta(BigDecimal.ZERO); // Se não houver atraso, zera a multa
                        emprestimoRepository.save(emp); // Salva a atualização
                        
                    }
                }
            }
        }
    }
}
