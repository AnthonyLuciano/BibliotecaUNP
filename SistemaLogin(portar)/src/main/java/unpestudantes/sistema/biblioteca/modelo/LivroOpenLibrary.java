package unpestudantes.sistema.biblioteca.modelo;

import java.util.List;

public class LivroOpenLibrary {
    private String titulo;
    private List<String> autores;
    private Integer anoPublicacao;
    private String capaUrl;
    private String editionKey; // Usando editionKey do OpenLibrary

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public List<String> getAutores() { return autores; }
    public void setAutores(List<String> autores) { this.autores = autores; }

    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }

    public String getCapaUrl() { return capaUrl; }
    public void setCapaUrl(String capaUrl) { this.capaUrl = capaUrl; }

    public String getEditionKey() { return editionKey; }
    public void setEditionKey(String editionKey) { this.editionKey = editionKey; }
}