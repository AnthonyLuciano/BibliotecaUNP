package unpestudantes.sistema.biblioteca.modelo.livro;

import java.util.List;

public class ResultadoBuscaOpenLibrary {
    private List<LivroOpenLibrary> livros;

    public List<LivroOpenLibrary> getLivros() {
        return livros;
    }

    public void setLivros(List<LivroOpenLibrary> livros) {
        this.livros = livros;
    }
}