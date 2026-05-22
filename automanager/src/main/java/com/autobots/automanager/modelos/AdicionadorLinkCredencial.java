package com.autobots.automanager.modelos;

import com.autobots.automanager.controles.CredencialControle;
import com.autobots.automanager.dtos.resposta.CredencialRespostaDTO;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AdicionadorLinkCredencial implements AdicionadorLink<CredencialRespostaDTO> {
    @Override
    public void adicionarLink(List<CredencialRespostaDTO> lista) {
        for (CredencialRespostaDTO dto : lista) {
            adicionarLink(dto);
        }
    }

    @Override
    public void adicionarLink(CredencialRespostaDTO objeto) {
        objeto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialControle.class).obterPorId(objeto.getId())).withSelfRel());
        objeto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialControle.class).obterTodos()).withRel("credenciais"));
        objeto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialControle.class).excluir(objeto.getId())).withRel("excluir"));
    }
}