package com.autobots.automanager.servicos;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.modelos.UsuarioAtualizador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServico {

    @Autowired
    private UsuarioRepositorio repositorio;

    public Usuario salvar(Usuario entidade) {
        return repositorio.save(entidade);
    }

    public List<Usuario> obterTodos() {
        return repositorio.findAll();
    }

    @PostAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENDEDOR') or " +
            "(hasRole('CLIENTE') and returnObject != null and returnObject.id == principal.id)")
    public Usuario buscarUsuario(Long id) {
        return repositorio.findById(id).orElse(null);
    }

    public Usuario atualizar(Usuario atualizacao) {
        Usuario usuario = repositorio.findById(atualizacao.getId()).orElse(null);
        if (usuario != null) {
            UsuarioAtualizador atualizador = new UsuarioAtualizador();
            atualizador.atualizar(usuario, atualizacao);
            return repositorio.save(usuario);
        }
        return null;
    }

    public void excluir(Long id) {
        repositorio.deleteById(id);
    }
}