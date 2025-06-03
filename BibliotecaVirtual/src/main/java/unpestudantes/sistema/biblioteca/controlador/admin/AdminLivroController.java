package unpestudantes.sistema.biblioteca.controlador.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unpestudantes.sistema.biblioteca.modelo.livro.LivroLocal;
import unpestudantes.sistema.biblioteca.repositorio.LivroLocalRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/livros")
public class AdminLivroController {

    @Autowired
    private LivroLocalRepository livroLocalRepository;

    @PostMapping("/cadastrar")
    public String cadastrarLivro(@RequestParam String titulo,
                                 @RequestParam String autor,
                                 @RequestParam String isbn,
                                 @RequestParam(value = "capa", required = false) MultipartFile capa,
                                 Model model) throws IOException {
        LivroLocal livro = new LivroLocal();
        livro.setTitulo(titulo);
        livro.setAutor(autor);

        String isbnLimpo = isbn.replaceAll("[^0-9Xx]", ""); // Remove traços e espaços
        if (isbnLimpo.length() == 10) {
            livro.setIsbn10(isbnLimpo);
        } else if (isbnLimpo.length() == 13) {
            livro.setIsbn13(isbnLimpo);
        } else {
            model.addAttribute("mensagem", "ISBN inválido. Deve ter 10 ou 13 dígitos.");
            return "redirect:/admin";
        }

        // Upload da capa (igual antes)
        if (capa != null && !capa.isEmpty()) {
            String uploadDir = "/home/anthony/GitHub/BibliotecaUNP/SistemaLogin(portar)/src/main/resources/static/capas/";
            File pasta = new File(uploadDir);
            if (!pasta.exists()) pasta.mkdirs();

            String nomeArquivo = UUID.randomUUID() + "_" + capa.getOriginalFilename();
            String caminho = uploadDir + nomeArquivo;
            capa.transferTo(new File(caminho));
            livro.setCapaUrl("/capas/" + nomeArquivo);
        } else {
            livro.setCapaUrl("/img/placeholder-book.png");
        }

        livroLocalRepository.save(livro);
        model.addAttribute("mensagem", "Livro cadastrado com sucesso!");
        return "redirect:/admin";
    }

    @GetMapping("/listar")
    public String listarLivros(Model model) {
        List<LivroLocal> livros = livroLocalRepository.findAll();
        model.addAttribute("livros", livros);
        return "admin-livros"; // crie um template se quiser listar
    }
}
