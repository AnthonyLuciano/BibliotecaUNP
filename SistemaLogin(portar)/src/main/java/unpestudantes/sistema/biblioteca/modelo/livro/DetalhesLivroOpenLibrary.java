package unpestudantes.sistema.biblioteca.modelo.livro;

import java.util.List;

public class DetalhesLivroOpenLibrary {
    private String titulo;
    private String subtitulo;
    private String dataPublicacao;
    private String editora;
    private String numeroPaginas;
    private String sinopse;
    private String capaUrl; // se ainda não tiver
    private String editionKey;    // se ainda não tiver
    private List<String> autores;
    private List<String> generos;
    private List<String> isbn10;
    private List<String> isbn13;

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getSubtitulo() { return subtitulo; }
    public void setSubtitulo(String subtitulo) { this.subtitulo = subtitulo; }

    public String getDataPublicacao() { return dataPublicacao; }
    public void setDataPublicacao(String dataPublicacao) { this.dataPublicacao = dataPublicacao; }

    public String getEditora() { return editora; }
    public void setEditora(String editora) { this.editora = editora; }

    public String getNumeroPaginas() { return numeroPaginas; }
    public void setNumeroPaginas(String numeroPaginas) { this.numeroPaginas = numeroPaginas; }

    public String getSinopse() { return sinopse; }
    public void setSinopse(String sinopse) { this.sinopse = sinopse; }

    public String getCapaUrl() { return capaUrl; }
    public void setCapaUrl(String capaUrl) { this.capaUrl = capaUrl; }

    public String getEditionKey() { return editionKey; }
    public void setEditionKey(String editionKey) { this.editionKey = editionKey; }

    public List<String> getAutores() { return autores; }
    public void setAutores(List<String> autores) { this.autores = autores; }

    public List<String> getGeneros() { return generos; }
    public void setGeneros(List<String> generos) { this.generos = generos; }

    public List<String> getIsbn10() { return isbn10; }
    public void setIsbn10(List<String> isbn10) { this.isbn10 = isbn10; }

    public List<String> getIsbn13() { return isbn13; }
    public void setIsbn13(List<String> isbn13) { this.isbn13 = isbn13; }
}