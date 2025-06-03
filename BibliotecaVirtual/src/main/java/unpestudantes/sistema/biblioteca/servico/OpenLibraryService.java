package unpestudantes.sistema.biblioteca.servico;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import unpestudantes.sistema.biblioteca.modelo.livro.DetalhesLivroOpenLibrary;
import unpestudantes.sistema.biblioteca.modelo.livro.LivroOpenLibrary;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
                // Filtra apenas livros em português
                boolean isPortugues = false;
                if (doc.has("language")) {
                    for (JsonNode lang : doc.get("language")) {
                        if ("por".equals(lang.asText())) {
                            isPortugues = true;
                            break;
                        }
                    }
                }
                if (!isPortugues) {
                    continue;
                }

                // Novo filtro: aceita se tiver cover_edition_key OU ISBN-10 OU ISBN-13
                boolean temEditionKey = doc.has("cover_edition_key");
                boolean temIsbn10 = false;
                boolean temIsbn13 = false;
                if (doc.has("isbn")) {
                    for (JsonNode isbnNode : doc.get("isbn")) {
                        String isbn = isbnNode.asText();
                        if (isbn.length() == 10) temIsbn10 = true;
                        if (isbn.length() == 13) temIsbn13 = true;
                    }
                }
                if (!temEditionKey && !temIsbn10 && !temIsbn13) {
                    System.out.println("Doc ignorado (sem edition_key e sem ISBN): " + doc.path("title").asText());
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
                if (temEditionKey) {
                    livro.setEditionKey(doc.get("cover_edition_key").asText());
                } else {
                    livro.setEditionKey(null);
                }

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
         * Busca livros na OpenLibrary por gênero.
         * Retorna uma lista de LivroOpenLibrary para exibição.
         * Conversa com a API da OpenLibrary.
         */
        public List<LivroOpenLibrary> buscarLivrosPorGenero(String genero) {
            System.out.println("Método buscarLivros por gênero chamado com gênero: " + genero);
            List<LivroOpenLibrary> livros = new ArrayList<>();
            try {
                String url = "https://openlibrary.org/subjects/" + URLEncoder.encode(genero, StandardCharsets.UTF_8) + ".json";
                String json = restTemplate.getForObject(url, String.class);
                JsonNode root = objectMapper.readTree(json);
    
                System.out.println("Docs retornados: " + root.get("works").size());
    
                for (JsonNode doc : root.get("works")) {
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
                    if (doc.has("cover_edition_key")) {
                        livro.setEditionKey(doc.get("cover_edition_key").asText());
                    } else {
                        livro.setEditionKey(null);
                    }
    
                    // Capa
                    if (doc.has("cover_i")) {
                        String coverId = doc.get("cover_i").asText();
                        livro.setCapaUrl("https://covers.openlibrary.org/b/id/" + coverId + "-M.jpg");
                    } else {
                        livro.setCapaUrl(null);
                    }
    
                    // ISBNs
                    if (doc.has("isbn")) {
                        List<String> isbns = new ArrayList<>();
                        for (JsonNode isbnNode : doc.get("isbn")) {
                            isbns.add(isbnNode.asText());
                        }
                        livro.setIsbns(isbns);
                    }

                    livros.add(livro);
                }
                System.out.println("Total de livros encontrados por gênero: " + livros.size());
            } catch (Exception e) {
                System.err.println("Erro ao buscar livros por gênero na OpenLibrary: " + e.getMessage());
                e.printStackTrace();
            }
            return livros;
        }

    /**
     * Busca livros na OpenLibrary por autor.
     * Retorna uma lista de LivroOpenLibrary para exibição.
     * Conversa com a API da OpenLibrary.
     */
    public List<LivroOpenLibrary> buscarLivrosPorAutor(String autor) {
        System.out.println("Método buscarLivrosPorAutor chamado com autor: " + autor);
        List<LivroOpenLibrary> livros = new ArrayList<>();
        try {
            String url = "https://openlibrary.org/search.json?author=" + URLEncoder.encode(autor, StandardCharsets.UTF_8);
            String json = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(json);

            System.out.println("Docs retornados: " + root.get("docs").size());

            for (JsonNode doc : root.get("docs")) {
                // Filtra apenas livros em português
                boolean isPortugues = false;
                if (doc.has("language")) {
                    for (JsonNode lang : doc.get("language")) {
                        if ("por".equals(lang.asText())) {
                            isPortugues = true;
                            break;
                        }
                    }
                }
                if (!isPortugues) {
                    continue;
                }

                // Novo filtro: aceita se tiver cover_edition_key OU ISBN-10 OU ISBN-13
                boolean temEditionKey = doc.has("cover_edition_key");
                boolean temIsbn10 = false;
                boolean temIsbn13 = false;
                if (doc.has("isbn")) {
                    for (JsonNode isbnNode : doc.get("isbn")) {
                        String isbn = isbnNode.asText();
                        if (isbn.length() == 10) temIsbn10 = true;
                        if (isbn.length() == 13) temIsbn13 = true;
                    }
                }
                if (!temEditionKey && !temIsbn10 && !temIsbn13) {
                    System.out.println("Doc ignorado (sem edition_key e sem ISBN): " + doc.path("title").asText());
                    continue;
                }

                LivroOpenLibrary livro = new LivroOpenLibrary();
                livro.setTitulo(doc.path("title").asText());

                // Autores
                if (doc.has("author_name")) {
                    List<String> autores = new ArrayList<>();
                    for (JsonNode autorNode : doc.get("author_name")) {
                        autores.add(autorNode.asText());
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
                if (temEditionKey) {
                    livro.setEditionKey(doc.get("cover_edition_key").asText());
                } else {
                    livro.setEditionKey(null);
                }

                // Capa
                if (doc.has("cover_i")) {
                    String coverId = doc.get("cover_i").asText();
                    livro.setCapaUrl("https://covers.openlibrary.org/b/id/" + coverId + "-M.jpg");
                } else {
                    livro.setCapaUrl(null);
                }

                // ISBNs
                if (doc.has("isbn")) {
                    List<String> isbns = new ArrayList<>();
                    for (JsonNode isbnNode : doc.get("isbn")) {
                        isbns.add(isbnNode.asText());
                    }
                    livro.setIsbns(isbns);
                }

                livros.add(livro);
            }
            System.out.println("Total de livros encontrados por autor: " + livros.size());
        } catch (Exception e) {
            System.err.println("Erro ao buscar livros por autor na OpenLibrary: " + e.getMessage());
            e.printStackTrace();
        }
        return livros;
    }

    /**
     * Busca livros na OpenLibrary por gênero e autor.
     * Retorna uma lista de LivroOpenLibrary para exibição.
     * Conversa com a API da OpenLibrary.
     */
    public List<LivroOpenLibrary> buscarLivrosPorGeneroEAutor(String genero, String autor) {
        System.out.println("Método buscarLivrosPorGeneroEAutor chamado com gênero: " + genero + " e autor: " + autor);
        List<LivroOpenLibrary> livros = new ArrayList<>();
        try {
            String url = "https://openlibrary.org/search.json?genre=" + URLEncoder.encode(genero, StandardCharsets.UTF_8) + "&author=" + URLEncoder.encode(autor, StandardCharsets.UTF_8);
            String json = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(json);

            System.out.println("Docs retornados: " + root.get("docs").size());

            for (JsonNode doc : root.get("docs")) {
                // Filtra apenas livros em português
                boolean isPortugues = false;
                if (doc.has("language")) {
                    for (JsonNode lang : doc.get("language")) {
                        if ("por".equals(lang.asText())) {
                            isPortugues = true;
                            break;
                        }
                    }
                }
                if (!isPortugues) {
                    continue;
                }

                // Novo filtro: aceita se tiver cover_edition_key OU ISBN-10 OU ISBN-13
                boolean temEditionKey = doc.has("cover_edition_key");
                boolean temIsbn10 = false;
                boolean temIsbn13 = false;
                if (doc.has("isbn")) {
                    for (JsonNode isbnNode : doc.get("isbn")) {
                        String isbn = isbnNode.asText();
                        if (isbn.length() == 10) temIsbn10 = true;
                        if (isbn.length() == 13) temIsbn13 = true;
                    }
                }
                if (!temEditionKey && !temIsbn10 && !temIsbn13) {
                    System.out.println("Doc ignorado (sem edition_key e sem ISBN): " + doc.path("title").asText());
                    continue;
                }

                LivroOpenLibrary livro = new LivroOpenLibrary();
                livro.setTitulo(doc.path("title").asText());

                // Autores
                if (doc.has("author_name")) {
                    List<String> autores = new ArrayList<>();
                    for (JsonNode autorNode : doc.get("author_name")) {
                        autores.add(autorNode.asText());
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
                if (temEditionKey) {
                    livro.setEditionKey(doc.get("cover_edition_key").asText());
                } else {
                    livro.setEditionKey(null);
                }

                // Capa
                if (doc.has("cover_i")) {
                    String coverId = doc.get("cover_i").asText();
                    livro.setCapaUrl("https://covers.openlibrary.org/b/id/" + coverId + "-M.jpg");
                } else {
                    livro.setCapaUrl(null);
                }

                // ISBNs
                if (doc.has("isbn")) {
                    List<String> isbns = new ArrayList<>();
                    for (JsonNode isbnNode : doc.get("isbn")) {
                        isbns.add(isbnNode.asText());
                    }
                    livro.setIsbns(isbns);
                }

                livros.add(livro);
            }
            System.out.println("Total de livros encontrados por gênero e autor: " + livros.size());
        } catch (Exception e) {
            System.err.println("Erro ao buscar livros por gênero e autor na OpenLibrary: " + e.getMessage());
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

            // Novos campos ISBN-10 e ISBN-13
            if (node.has("isbn_10")) {
                List<String> isbn10s = new ArrayList<>();
                for (JsonNode n : node.get("isbn_10")) {
                    isbn10s.add(n.asText());
                }
                detalhes.setIsbn10(isbn10s);
            }
            if (node.has("isbn_13")) {
                List<String> isbn13s = new ArrayList<>();
                for (JsonNode n : node.get("isbn_13")) {
                    isbn13s.add(n.asText());
                }
                detalhes.setIsbn13(isbn13s);
            }

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

    public DetalhesLivroOpenLibrary buscarDetalhesLivro(String editionKey) {
        return buscarDetalhesPorEditionKey(editionKey);
    }
}