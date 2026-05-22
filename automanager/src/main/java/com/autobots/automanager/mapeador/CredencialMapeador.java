package com.autobots.automanager.mapeador;

import com.autobots.automanager.dtos.requisicao.CredencialRequisicaoDTO;
import com.autobots.automanager.dtos.resposta.CredencialRespostaDTO;
import com.autobots.automanager.entidades.Credencial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CredencialMapeador {
    CredencialMapeador INSTANCIA = Mappers.getMapper(CredencialMapeador.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criacao", ignore = true)
    @Mapping(target = "ultimoAcesso", ignore = true)
    @Mapping(target = "inativo", ignore = true)
    Credencial requisicaoParaEntidade(CredencialRequisicaoDTO dto);

    CredencialRespostaDTO entidadeParaResposta(Credencial entidade);
}