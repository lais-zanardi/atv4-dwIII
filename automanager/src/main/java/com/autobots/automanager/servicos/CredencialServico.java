package com.autobots.automanager.servicos;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.modelos.CredencialAtualizador;
import com.autobots.automanager.repositorios.CredencialRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CredencialServico {
    @Autowired
    private CredencialRepositorio repositorio;

    @Autowired
    private CredencialAtualizador atualizador;

    public Credencial salvar(Credencial credencial) {
        return repositorio.save(credencial);
    }

    public List<Credencial> obterTodos() {
        return repositorio.findAll();
    }

    public Credencial obterPorId(Long id) {
        return repositorio.findById(id).orElse(null);
    }

    public Credencial atualizar(Long id, Credencial atualizacao) {
        Credencial alvo = obterPorId(id);
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