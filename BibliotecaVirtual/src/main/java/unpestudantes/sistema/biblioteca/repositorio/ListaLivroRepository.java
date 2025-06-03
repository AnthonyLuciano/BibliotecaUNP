package unpestudantes.sistema.biblioteca.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import unpestudantes.sistema.biblioteca.modelo.livro.ListaLivro;
import unpestudantes.sistema.biblioteca.modelo.usuario.Usuario;

import java.util.List;

public interface ListaLivroRepository extends JpaRepository<ListaLivro, Long> {
    List<ListaLivro> findByUsuario(Usuario usuario);
    List<ListaLivro> findByUsuarioAndNomeLista(Usuario usuario, String nomeLista);
}