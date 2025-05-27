package unpestudantes.Sistema.biblioteca.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
/*
 * @author anthony
 */
@Entity
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String autor;
    private String genero;
    private String datalancamento;
    private String ISBN;
    private boolean disponivel;
    private String capaUrl;
    private String sinopse;

    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getDatalancamento() { return datalancamento; }
    public void setDatalancamento(String datalancamento) { this.datalancamento = datalancamento; }

    public String getISBN() { return ISBN; }
    public void setISBN(String ISBN) { this.ISBN = ISBN; }

    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

    public String getCapaUrl() {return capaUrl;}
    public void setCapaUrl(String capaUrl) {this.capaUrl = capaUrl;}

    public String getSinopse() {return sinopse;}
    public void setSinopse(String sinopse) {this.sinopse = sinopse;}
}
