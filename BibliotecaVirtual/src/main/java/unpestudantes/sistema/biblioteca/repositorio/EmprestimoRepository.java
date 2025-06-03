package unpestudantes.sistema.biblioteca.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import unpestudantes.sistema.biblioteca.modelo.emprestimo.Emprestimo;
import unpestudantes.sistema.biblioteca.modelo.usuario.Usuario;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findByUsuarioAndDataDevolucaoRealIsNull(Usuario usuario);
    List<Emprestimo> findByUsuario(Usuario usuario);
    List<Emprestimo> findByIsbnAndDataDevolucaoRealIsNull(String isbn);
}