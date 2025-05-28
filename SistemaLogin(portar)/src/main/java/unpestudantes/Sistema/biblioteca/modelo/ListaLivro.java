package unpestudantes.Sistema.biblioteca.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ListaLivro {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    private String nomeLista; // Ex: "Favoritos", "Para Ler"
    private String isbn;
    private String titulo;
    // outros campos desejados

    // getters e setters
}
