package com.autobots.automanager.dtos.resposta;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = true)
public class VeiculoRespostaDTO extends RepresentationModel<VeiculoRespostaDTO> {
    private Long id;
    private String placa;
    private String modelo;
    private String marca;
    private Integer ano;
}