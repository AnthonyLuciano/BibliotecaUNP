package com.biblioteca.bibliotecavirtual.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.bibliotecavirtual.modelos.Usuario;
import com.biblioteca.bibliotecavirtual.servicos.UsuarioServico;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControle {

    @Autowired
    private UsuarioServico usuarioServico;

    @GetMapping
    public List<Usuario> listarTodos() {
        return usuarioServico.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return usuarioServico.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Usuario salvar(@RequestBody Usuario usuario) {
        return usuarioServico.salvar(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioServico.deletar(id);
        return ResponseEntity.noContent().build();
    }
}