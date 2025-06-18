package unpestudantes.sistema.biblioteca.controlador.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unpestudantes.sistema.biblioteca.modelo.livro.LivroLocal;
import unpestudantes.sistema.biblioteca.repositorio.LivroLocalRepository;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * Controller responsável pelo CRUD de livros locais para o painel administrativo.
 */
@Controller
@RequestMapping("/admin/livros")
public class AdminLivroController {

    @Autowired
    private LivroLocalRepository livroLocalRepository;

    /**
     * Cadastra um novo livro local, incluindo upload de capa.
     * Valida o ISBN e salva o livro no banco.
     */
    @PostMapping("/cadastrar")
    public String cadastrarLivro(@RequestParam String titulo,
                                 @RequestParam String autor,
                                 @RequestParam String isbn,
                                 @RequestParam int quantidade,
                                 @RequestParam(value = "capa", required = false) MultipartFile capa,
                                 Model model) {
        LivroLocal livro = new LivroLocal();
        livro.setTitulo(titulo);
        livro.setAutor(autor);
        livro.setQuantidade(quantidade);

        String isbnLimpo = isbn.replaceAll("[^0-9Xx]", "");
        if (isbnLimpo.length() == 10) {
            livro.setIsbn10(isbnLimpo);
        } else if (isbnLimpo.length() == 13) {
            livro.setIsbn13(isbnLimpo);
        } else {
            model.addAttribute("mensagem", "ISBN inválido. Deve ter 10 ou 13 dígitos.");
            return "redirect:/admin";
        }

        try {
            String uploadDir = new File("uploads/capas").getAbsolutePath();
            Files.createDirectories(Paths.get(uploadDir));
            if (capa != null && !capa.isEmpty()) {
                String extensao = "";
                String original = capa.getOriginalFilename();
                if (original != null && original.contains(".")) {
                    extensao = original.substring(original.lastIndexOf('.'));
                }
                String nomeArquivo = UUID.randomUUID() + extensao;
                Path caminho = Paths.get(uploadDir, nomeArquivo);
                capa.transferTo(caminho.toFile());
                livro.setCapaUrl("/uploads/capas/" + nomeArquivo);
            } else {
                livro.setCapaUrl("/img/placeholder-book.png");
            }
            livroLocalRepository.save(livro);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao salvar o livro ou a capa.");
            return "admin/cadastrar-livro";
        }
        return "redirect:/admin/livros/listar";
    }

    /**
     * Lista todos os livros locais cadastrados.
     * Exibe no painel do administrador.
     */
    @GetMapping("/listar")
    public String listarLivros(Model model) {
        List<LivroLocal> livros = livroLocalRepository.findAll();
        model.addAttribute("livros", livros);
        return "Administrador"; // ou o template correto
    }
}
