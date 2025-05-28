package unpestudantes.Sistema.biblioteca.servico;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import unpestudantes.Sistema.biblioteca.modelo.DetalhesLivroOpenLibrary;
import unpestudantes.Sistema.biblioteca.modelo.LivroOpenLibrary;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OpenLibraryService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<LivroOpenLibrary> buscarLivros(String termo) {
        System.out.println("Método buscarLivros chamado com termo: " + termo);
        List<LivroOpenLibrary> livros = new ArrayList<>();
        try {
            String url = "https://openlibrary.org/search.json?q=" + URLEncoder.encode(termo, StandardCharsets.UTF_8);
            String json = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(json);

            System.out.println("Docs retornados: " + root.get("docs").size());

            for (JsonNode doc : root.get("docs")) {
                if (!doc.has("cover_edition_key")) {
                    System.out.println("Doc ignorado (sem cover_edition_key): " + doc.path("title").asText());
                    continue;
                }

                LivroOpenLibrary livro = new LivroOpenLibrary();
                livro.setTitulo(doc.path("title").asText());

                // Autores
                if (doc.has("author_name")) {
                    List<String> autores = new ArrayList<>();
                    for (JsonNode autor : doc.get("author_name")) {
                        autores.add(autor.asText());
                    }
                    livro.setAutores(autores);
                } else {
                    livro.setAutores(Collections.emptyList());
                }

                // Ano de publicação
                if (doc.has("first_publish_year")) {
                    livro.setAnoPublicacao(doc.get("first_publish_year").asInt());
                }

                // Cover Edition Key
                livro.setEditionKey(doc.get("cover_edition_key").asText());

                // Capa
                if (doc.has("cover_i")) {
                    String coverId = doc.get("cover_i").asText();
                    livro.setCapaUrl("https://covers.openlibrary.org/b/id/" + coverId + "-M.jpg");
                } else {
                    livro.setCapaUrl(null);
                }

                livros.add(livro);
            }
            System.out.println("Total de livros encontrados: " + livros.size());
        } catch (Exception e) {
            System.err.println("Erro ao buscar livros na OpenLibrary: " + e.getMessage());
            e.printStackTrace();
        }
        return livros;
    }

    public DetalhesLivroOpenLibrary buscarDetalhesPorIsbn(String isbn) {
        String url = "https://openlibrary.org/isbn/" + isbn + ".json";
        try {
            String json = restTemplate.getForObject(url, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(json);

            DetalhesLivroOpenLibrary detalhes = new DetalhesLivroOpenLibrary();
            detalhes.setTitulo(node.path("title").asText());
            detalhes.setSubtitulo(node.path("subtitle").asText(""));
            detalhes.setDataPublicacao(node.path("publish_date").asText(""));
            detalhes.setEditora(node.has("publishers") && node.get("publishers").size() > 0 ? node.get("publishers").get(0).asText() : "");
            detalhes.setNumeroPaginas(node.path("number_of_pages").asText(""));
            // Extrai editionKey se disponível
            if (node.has("key")) {
                String editionKey = node.get("key").asText().replace("/books/", "");
                detalhes.setEditionKey(editionKey);
            } else {
                detalhes.setEditionKey("");
            }

            // Sinopse/descrição
            if (node.has("description")) {
                if (node.get("description").isTextual()) {
                    detalhes.setSinopse(node.get("description").asText());
                } else if (node.get("description").has("value")) {
                    detalhes.setSinopse(node.get("description").get("value").asText());
                }
            } else {
                detalhes.setSinopse("Sinopse não disponível.");
            }

            // Capa
            if (node.has("covers") && node.get("covers").size() > 0) {
                String coverId = node.get("covers").get(0).asText();
                detalhes.setCapaUrl("https://covers.openlibrary.org/b/id/" + coverId + "-L.jpg");
            } else if (isbn != null && !isbn.isEmpty()) {
                detalhes.setCapaUrl("https://covers.openlibrary.org/b/isbn/" + isbn + "-M.jpg");
            } else {
                detalhes.setCapaUrl(null);
            }

            // Gênero
            if (node.has("subjects") && node.get("subjects").size() > 0) {
                detalhes.setGenero(node.get("subjects").get(0).asText());
            } else {
                detalhes.setGenero("");
            }

            return detalhes;
        } catch (Exception e) {
            return null;
        }
    }

    public DetalhesLivroOpenLibrary buscarDetalhesPorEditionKey(String editionKey) {
        try {
            String url = "https://openlibrary.org/books/" + editionKey + ".json";
            String json = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(json);

            DetalhesLivroOpenLibrary detalhes = new DetalhesLivroOpenLibrary();
            detalhes.setTitulo(root.path("title").asText());
            // Preencha outros campos conforme necessário

            return detalhes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}