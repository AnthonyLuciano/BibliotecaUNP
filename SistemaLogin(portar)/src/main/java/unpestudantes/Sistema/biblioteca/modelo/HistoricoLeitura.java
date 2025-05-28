package unpestudantes.Sistema.biblioteca.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class HistoricoLeitura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    private String isbn;
    private String titulo;
    private String genero;
    private LocalDateTime dataAcesso;

    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public LocalDateTime getDataAcesso() { return dataAcesso; }
    public void setDataAcesso(LocalDateTime dataAcesso) { this.dataAcesso = dataAcesso; }
}