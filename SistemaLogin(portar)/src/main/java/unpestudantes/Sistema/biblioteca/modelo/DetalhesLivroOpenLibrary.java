package unpestudantes.Sistema.biblioteca.modelo;

public class DetalhesLivroOpenLibrary {
    private String titulo;
    private String subtitulo;
    private String dataPublicacao;
    private String editora;
    private String numeroPaginas;
    private String sinopse;
    private String capaUrl; // se ainda não tiver
    private String editionKey;    // se ainda não tiver
    private String genero;

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

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
}