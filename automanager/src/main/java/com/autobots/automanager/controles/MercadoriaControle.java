package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.requisicao.MercadoriaRequisicaoDTO;
import com.autobots.automanager.dtos.resposta.MercadoriaRespostaDTO;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.mapeador.MercadoriaMapeador;
import com.autobots.automanager.modelos.AdicionadorLinkMercadoria;
import com.autobots.automanager.servicos.MercadoriaServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mercadorias")
public class MercadoriaControle {

    @Autowired private MercadoriaServico servico;
    @Autowired private MercadoriaMapeador mapeador;
    @Autowired private AdicionadorLinkMercadoria adicionadorLink;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    @PostMapping("/cadastrar")
    public ResponseEntity<MercadoriaRespostaDTO> cadastrarMercadoria(@RequestBody MercadoriaRequisicaoDTO requisicao) {
        Mercadoria entidade = mapeador.requisicaoParaEntidade(requisicao);
        Mercadoria salva = servico.salvar(entidade);
        MercadoriaRespostaDTO resposta = mapeador.entidadeParaResposta(salva);
        adicionadorLink.adicionarLink(resposta);
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    @GetMapping("/obter")
    public ResponseEntity<List<MercadoriaRespostaDTO>> obterMercadorias() {
        List<Mercadoria> mercadorias = servico.obterTodas();
        List<MercadoriaRespostaDTO> respostas = mercadorias.stream()
                .map(mapeador::entidadeParaResposta)
                .collect(Collectors.toList());
        adicionadorLink.adicionarLink(respostas);
        return new ResponseEntity<>(respostas, HttpStatus.FOUND);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    @GetMapping("/obter/{id}")
    public ResponseEntity<MercadoriaRespostaDTO> obterMercadoria(@PathVariable Long id) {
        Mercadoria mercadoria = servico.obterPorId(id);
        if (mercadoria == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        MercadoriaRespostaDTO resposta = mapeador.entidadeParaResposta(mercadoria);
        adicionadorLink.adicionarLink(resposta);
        return new ResponseEntity<>(resposta, HttpStatus.FOUND);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<MercadoriaRespostaDTO> atualizarMercadoria(@PathVariable Long id, @RequestBody MercadoriaRequisicaoDTO requisicao) {
        Mercadoria atualizacao = mapeador.requisicaoParaEntidade(requisicao);
        atualizacao.setId(id);

        Mercadoria atualizada = servico.atualizar(atualizacao);
        if (atualizada == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        MercadoriaRespostaDTO resposta = mapeador.entidadeParaResposta(atualizada);
        adicionadorLink.adicionarLink(resposta);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluirMercadoria(@PathVariable Long id) {
        if (servico.obterPorId(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        servico.excluir(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}