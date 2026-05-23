package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.requisicao.VendaRequisicaoDTO;
import com.autobots.automanager.dtos.resposta.VendaRespostaDTO;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.mapeador.VendaMapeador;
import com.autobots.automanager.modelos.AdicionadorLinkVenda;
import com.autobots.automanager.servicos.VendaServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vendas")
public class VendaControle {

    @Autowired private VendaServico servico;
    @Autowired private VendaMapeador mapeador;
    @Autowired private AdicionadorLinkVenda adicionadorLink;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENDEDOR')")
    @PostMapping("/cadastrar")
    public ResponseEntity<VendaRespostaDTO> cadastrarVenda(@RequestBody VendaRequisicaoDTO requisicao) {
        Venda entidade = mapeador.requisicaoParaEntidade(requisicao);
        Venda salva = servico.salvar(entidade);
        VendaRespostaDTO resposta = mapeador.entidadeParaResposta(salva);
        adicionadorLink.adicionarLink(resposta);
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    @GetMapping("/obter")
    public ResponseEntity<List<VendaRespostaDTO>> obterVendas() {
        List<Venda> vendas = servico.obterTodas();
        List<VendaRespostaDTO> respostas = vendas.stream()
                .map(mapeador::entidadeParaResposta)
                .collect(Collectors.toList());
        adicionadorLink.adicionarLink(respostas);
        return new ResponseEntity<>(respostas, HttpStatus.FOUND);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    @GetMapping("/obter/{id}")
    public ResponseEntity<VendaRespostaDTO> obterVenda(@PathVariable Long id) {
        Venda venda = servico.obterPorId(id);
        if (venda == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        VendaRespostaDTO resposta = mapeador.entidadeParaResposta(venda);
        adicionadorLink.adicionarLink(resposta);
        return new ResponseEntity<>(resposta, HttpStatus.FOUND);
    }
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<VendaRespostaDTO> atualizarVenda(@PathVariable Long id, @RequestBody VendaRequisicaoDTO requisicao) {
        Venda atualizacao = mapeador.requisicaoParaEntidade(requisicao);
        atualizacao.setId(id);

        Venda atualizada = servico.atualizar(atualizacao);
        if (atualizada == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VendaRespostaDTO resposta = mapeador.entidadeParaResposta(atualizada);
        adicionadorLink.adicionarLink(resposta);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluirVenda(@PathVariable Long id) {
        if (servico.obterPorId(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        servico.excluir(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}