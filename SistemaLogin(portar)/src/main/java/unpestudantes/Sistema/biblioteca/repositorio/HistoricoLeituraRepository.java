package unpestudantes.Sistema.biblioteca.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import unpestudantes.Sistema.biblioteca.modelo.HistoricoLeitura;
import unpestudantes.Sistema.biblioteca.modelo.Usuario;
import java.util.List;

public interface HistoricoLeituraRepository extends JpaRepository<HistoricoLeitura, Long> {
    List<HistoricoLeitura> findByUsuarioOrderByDataAcessoDesc(Usuario usuario);
}