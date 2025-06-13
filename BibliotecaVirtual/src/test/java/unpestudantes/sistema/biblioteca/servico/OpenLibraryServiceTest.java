package unpestudantes.sistema.biblioteca.servico;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import unpestudantes.sistema.biblioteca.modelo.livro.LivroOpenLibrary;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenLibraryServiceTest {

    @InjectMocks
    private OpenLibraryService openLibraryService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void buscarLivrosPorGeneroDevePreencherIsbns() throws Exception {
        String genero = "science_fiction";
        String json = "{\"works\":[{\"title\":\"Livro Teste\",\"isbn\":[\"1234567890\",\"9876543210\"]}]}";
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(json);

        List<LivroOpenLibrary> livros = openLibraryService.buscarLivrosPorGenero(genero);

        assertFalse(livros.isEmpty());
        assertEquals("Livro Teste", livros.get(0).getTitulo());
        assertEquals("1234567890", livros.get(0).getIsbns().get(0));
        assertEquals("9876543210", livros.get(0).getIsbns().get(1));
        System.out.println("✅ Busca por gênero retornou livros corretamente.");
    }
}