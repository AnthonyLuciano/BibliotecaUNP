package unpestudantes.sistema.biblioteca.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import unpestudantes.sistema.biblioteca.modelo.HistoricoLeitura;
import unpestudantes.sistema.biblioteca.modelo.Usuario;

import java.util.List;

public interface HistoricoLeituraRepository extends JpaRepository<HistoricoLeitura, Long> {
    List<HistoricoLeitura> findByUsuarioOrderByDataAcessoDesc(Usuario usuario);
}