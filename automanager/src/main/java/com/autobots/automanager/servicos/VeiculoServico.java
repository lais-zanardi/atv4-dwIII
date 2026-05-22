package com.autobots.automanager.servicos;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.modelos.VeiculoAtualizador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.VeiculoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeiculoServico {

    @Autowired
    private VeiculoRepositorio repositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private VeiculoAtualizador atualizador;

    public Veiculo salvar(Veiculo veiculo, Long idProprietario) {
        Usuario proprietario = usuarioRepositorio.findById(idProprietario).orElse(null);
        if (proprietario == null) {
            return null;
        }
        veiculo.setProprietario(proprietario);
        return repositorio.save(veiculo);
    }

    public List<Veiculo> obterTodos() {
        return repositorio.findAll();
    }

    public Veiculo obterPorId(Long id) {
        return repositorio.findById(id).orElse(null);
    }

    public Veiculo atualizar(Veiculo atualizacao) {
        Veiculo alvo = obterPorId(atualizacao.getId());
        if (alvo != null) {
            atualizador.atualizar(alvo, atualizacao);
            return repositorio.save(alvo);
        }
        return null;
    }

    public void excluir(Long id) {
        repositorio.deleteById(id);
    }
}