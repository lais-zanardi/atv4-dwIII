package com.autobots.automanager.modelos;

import com.autobots.automanager.entidades.Credencial;
import org.springframework.stereotype.Component;

@Component
public class CredencialAtualizador {
    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    public void atualizar(Credencial alvo, Credencial atualizacao) {
        if (atualizacao != null) {
            if (!verificador.verificar(atualizacao.getLogin())) {
                alvo.setLogin(atualizacao.getLogin());
            }
            if (!verificador.verificar(atualizacao.getSenha())) {
                alvo.setSenha(atualizacao.getSenha());
            }
        }
    }
}