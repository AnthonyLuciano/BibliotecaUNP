package unpestudantes.sistema.biblioteca.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import unpestudantes.sistema.biblioteca.modelo.livro.LivroLocal;

import java.util.List;

public interface LivroLocalRepository extends JpaRepository<LivroLocal, Long> {
    List<LivroLocal> findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCaseOrIsbn10ContainingIgnoreCaseOrIsbn13ContainingIgnoreCase(
        String titulo, String autor, String isbn10, String isbn13);
}
