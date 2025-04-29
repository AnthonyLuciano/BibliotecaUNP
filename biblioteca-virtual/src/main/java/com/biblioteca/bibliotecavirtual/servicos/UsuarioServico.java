package com.biblioteca.bibliotecavirtual.servicos;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.bibliotecavirtual.modelos.Usuario;
import com.biblioteca.bibliotecavirtual.repositorios.UsuarioRepositorio;

@Service
public class UsuarioServico {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public List<Usuario> listarTodos() {
        return usuarioRepositorio.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepositorio.findById(id);
    }

    public Usuario salvar(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }

    public void deletar(Long id) {
        usuarioRepositorio.deleteById(id);
    }
}