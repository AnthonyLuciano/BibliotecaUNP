package unpestudantes.sistema.biblioteca.repositorio;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import unpestudantes.sistema.biblioteca.modelo.Usuario;

// Interface de repositório para acessar dados dos usuários no banco
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Busca um usuário pelo nome de usuário
    Optional<Usuario> findByUsername(String username);
    List<Usuario> findByUsernameContainingIgnoreCase(String username);
}