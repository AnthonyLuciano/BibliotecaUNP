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

    /**
     * Busca livros na OpenLibrary por termo de pesquisa.
     * Retorna uma lista de LivroOpenLibrary para exibição.
     * Conversa com a API da OpenLibrary.
     */
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

    /**
     * Busca detalhes de um livro na OpenLibrary usando o ISBN.
     * Retorna um objeto DetalhesLivroOpenLibrary.
     * Conversa com a API da OpenLibrary.
     */
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

            // Autores (authors é uma lista de objetos com chave "key")
            List<String> autores = new ArrayList<>();
            if (node.has("authors")) {
                for (JsonNode autorNode : node.get("authors")) {
                    String authorKey = autorNode.path("key").asText();
                    try {
                        String autorUrl = "https://openlibrary.org" + authorKey + ".json";
                        String autorJson = restTemplate.getForObject(autorUrl, String.class);
                        JsonNode autorRoot = objectMapper.readTree(autorJson);
                        autores.add(autorRoot.path("name").asText());
                    } catch (Exception e) {
                        // Se falhar, ignora este autor
                    }
                }
            }
            detalhes.setAutores(autores);

            // Gêneros (subjects)
            List<String> generos = new ArrayList<>();
            if (node.has("subjects")) {
                for (JsonNode generoNode : node.get("subjects")) {
                    generos.add(generoNode.asText());
                }
            }
            detalhes.setGeneros(generos);

            return detalhes;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Busca detalhes de um livro na OpenLibrary usando a editionKey.
     * Retorna um objeto DetalhesLivroOpenLibrary.
     * Conversa com a API da OpenLibrary.
     */
    public DetalhesLivroOpenLibrary buscarDetalhesPorEditionKey(String editionKey) {
        try {
            String url = "https://openlibrary.org/books/" + editionKey + ".json";
            String json = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(json);

            DetalhesLivroOpenLibrary detalhes = new DetalhesLivroOpenLibrary();
            detalhes.setTitulo(root.path("title").asText());
            detalhes.setSubtitulo(root.path("subtitle").asText(""));
            detalhes.setDataPublicacao(root.path("publish_date").asText(""));
            detalhes.setEditora(root.has("publishers") && root.get("publishers").size() > 0 ? root.get("publishers").get(0).asText() : "");
            detalhes.setNumeroPaginas(root.has("number_of_pages") ? root.get("number_of_pages").asText() : "");
            detalhes.setEditionKey(editionKey);

            // Sinopse/descrição
            if (root.has("description")) {
                if (root.get("description").isTextual()) {
                    detalhes.setSinopse(root.get("description").asText());
                } else if (root.get("description").has("value")) {
                    detalhes.setSinopse(root.get("description").get("value").asText());
                }
            } else {
                detalhes.setSinopse("Sinopse não disponível.");
            }

            // Capa
            if (root.has("covers") && root.get("covers").size() > 0) {
                String coverId = root.get("covers").get(0).asText();
                detalhes.setCapaUrl("https://covers.openlibrary.org/b/id/" + coverId + "-L.jpg");
            } else {
                detalhes.setCapaUrl(null);
            }

            // Autores (authors é uma lista de objetos com chave "key")
            List<String> autores = new ArrayList<>();
            if (root.has("authors")) {
                for (JsonNode autorNode : root.get("authors")) {
                    String authorKey = autorNode.path("key").asText();
                    // Busca nome do autor em outra requisição
                    try {
                        String autorUrl = "https://openlibrary.org" + authorKey + ".json";
                        String autorJson = restTemplate.getForObject(autorUrl, String.class);
                        JsonNode autorRoot = objectMapper.readTree(autorJson);
                        autores.add(autorRoot.path("name").asText());
                    } catch (Exception e) {
                        // Se falhar, ignora este autor
                    }
                }
            }
            detalhes.setAutores(autores);

            // Gêneros (subjects)
            List<String> generos = new ArrayList<>();
            if (root.has("subjects")) {
                for (JsonNode generoNode : root.get("subjects")) {
                    generos.add(generoNode.asText());
                }
            }
            detalhes.setGeneros(generos);

            return detalhes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}