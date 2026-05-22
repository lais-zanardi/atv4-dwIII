package com.autobots.automanager.dtos.resposta;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class CredencialRespostaDTO extends RepresentationModel<CredencialRespostaDTO> {
    private Long id;
    private String login;
    private LocalDateTime criacao;
    private LocalDateTime ultimoAcesso;
    private boolean inativo;
}