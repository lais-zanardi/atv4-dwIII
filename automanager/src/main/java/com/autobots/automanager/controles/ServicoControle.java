package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.requisicao.ServicoRequisicaoDTO;
import com.autobots.automanager.dtos.resposta.ServicoRespostaDTO;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.mapeador.ServicoMapeador;
import com.autobots.automanager.modelos.AdicionadorLinkServico;
import com.autobots.automanager.servicos.ServicoServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/servicos")
public class ServicoControle {

    @Autowired private ServicoServico servicoServico;
    @Autowired private ServicoMapeador mapeador;
    @Autowired private AdicionadorLinkServico adicionadorLink;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    @PostMapping("/cadastrar")
    public ResponseEntity<ServicoRespostaDTO> cadastrarServico(@RequestBody ServicoRequisicaoDTO requisicao) {
        Servico entidade = mapeador.requisicaoParaEntidade(requisicao);
        Servico salvo = servicoServico.salvar(entidade);
        ServicoRespostaDTO resposta = mapeador.entidadeParaResposta(salvo);
        adicionadorLink.adicionarLink(resposta);
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    @GetMapping("/obter")
    public ResponseEntity<List<ServicoRespostaDTO>> obterServicos() {
        List<Servico> servicos = servicoServico.obterTodos();
        List<ServicoRespostaDTO> respostas = servicos.stream()
                .map(mapeador::entidadeParaResposta)
                .collect(Collectors.toList());
        adicionadorLink.adicionarLink(respostas);
        return new ResponseEntity<>(respostas, HttpStatus.FOUND);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    @GetMapping("/obter/{id}")
    public ResponseEntity<ServicoRespostaDTO> obterServico(@PathVariable Long id) {
        Servico servico = servicoServico.obterPorId(id);
        if (servico == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ServicoRespostaDTO resposta = mapeador.entidadeParaResposta(servico);
        adicionadorLink.adicionarLink(resposta);
        return new ResponseEntity<>(resposta, HttpStatus.FOUND);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<ServicoRespostaDTO> atualizarServico(@PathVariable Long id, @RequestBody ServicoRequisicaoDTO requisicao) {
        Servico atualizacao = mapeador.requisicaoParaEntidade(requisicao);
        atualizacao.setId(id);

        Servico atualizado = servicoServico.atualizar(atualizacao);
        if (atualizado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ServicoRespostaDTO resposta = mapeador.entidadeParaResposta(atualizado);
        adicionadorLink.adicionarLink(resposta);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluirServico(@PathVariable Long id) {
        if (servicoServico.obterPorId(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        servicoServico.excluir(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}