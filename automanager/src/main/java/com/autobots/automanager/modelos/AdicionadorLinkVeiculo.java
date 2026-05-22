package com.autobots.automanager.modelos;

import com.autobots.automanager.controles.VeiculoControle;
import com.autobots.automanager.dtos.resposta.VeiculoRespostaDTO;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkVeiculo implements AdicionadorLink<VeiculoRespostaDTO> {

    @Override
    public void adicionarLink(List<VeiculoRespostaDTO> lista) {
        for (VeiculoRespostaDTO dto : lista) {
            adicionarLink(dto);
        }
    }

    @Override
    public void adicionarLink(VeiculoRespostaDTO objeto) {
        objeto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculo(objeto.getId())).withSelfRel());
        objeto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculos()).withRel("veiculos"));
        objeto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).excluirVeiculo(objeto.getId())).withRel("excluir"));
    }
}