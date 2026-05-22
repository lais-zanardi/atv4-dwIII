package com.autobots.automanager.mapeador;

import com.autobots.automanager.dtos.requisicao.VeiculoRequisicaoDTO;
import com.autobots.automanager.dtos.resposta.VeiculoRespostaDTO;
import com.autobots.automanager.entidades.Veiculo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VeiculoMapeador {
    VeiculoMapeador INSTANCIA = Mappers.getMapper(VeiculoMapeador.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "proprietario", ignore = true)
    Veiculo requisicaoParaEntidade(VeiculoRequisicaoDTO dto);

    VeiculoRespostaDTO entidadeParaResposta(Veiculo entidade);
}