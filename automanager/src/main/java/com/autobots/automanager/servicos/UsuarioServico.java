package com.autobots.automanager.servicos;

import com.autobots.automanager.adaptadores.UserDetailsImpl;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.excecoes.NegocioException;
import com.autobots.automanager.modelos.UsuarioAtualizador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServico {
     private UsuarioRepositorio repositorio;
     private UsuarioAtualizador atualizador;

    public Usuario salvar(Usuario usuario) { return repositorio.save(usuario); }
    public List<Usuario> obterTodos() { return repositorio.findAll(); }
    public Usuario buscarUsuario(Long id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof UserDetailsImpl)) {
            throw new NegocioException("Usuário não autenticado.");
        }

        UserDetailsImpl usuarioLogado = (UserDetailsImpl) principal;

        boolean ehCliente = usuarioLogado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"));

        if (ehCliente && !usuarioLogado.getId().equals(id)) {
            throw new NegocioException("Acesso negado: Você só pode acessar o seu próprio cadastro.");
        }
        return repositorio.findById(id).orElseThrow(() ->
                new NegocioException("Usuário não encontrado."));
    }
    public Usuario atualizar(Usuario atualizacao) {
        Usuario alvo = obterPorId(atualizacao.getId());
        if (alvo != null) {
            atualizador.atualizar(alvo, atualizacao);
            return repositorio.save(alvo);
        }
        return null;
    }
    public void excluir(Long id) { repositorio.deleteById(id); }
}