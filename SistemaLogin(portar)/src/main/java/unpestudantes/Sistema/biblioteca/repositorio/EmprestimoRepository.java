package unpestudantes.Sistema.biblioteca.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import unpestudantes.Sistema.biblioteca.modelo.Emprestimo;
import unpestudantes.Sistema.biblioteca.modelo.Usuario;
import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findByUsuarioAndDataDevolucaoRealIsNull(Usuario usuario);
    List<Emprestimo> findByUsuario(Usuario usuario);
    List<Emprestimo> findByIsbnAndDataDevolucaoRealIsNull(String isbn);
}