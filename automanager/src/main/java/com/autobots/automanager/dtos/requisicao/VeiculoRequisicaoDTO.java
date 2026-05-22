package com.autobots.automanager.dtos.requisicao;

import lombok.Data;

@Data
public class VeiculoRequisicaoDTO {
    private String placa;
    private String modelo;
    private String marca;
    private Integer ano;
}