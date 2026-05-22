package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.requisicao.CredencialRequisicaoDTO;
import com.autobots.automanager.dtos.resposta.CredencialRespostaDTO;
import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.mapeador.CredencialMapeador;
import com.autobots.automanager.modelos.AdicionadorLinkCredencial;
import com.autobots.automanager.servicos.CredencialServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/credenciais")
public class CredencialControle {

    @Autowired
    private CredencialServico servico;

    @Autowired
    private CredencialMapeador mapeador;

    @Autowired
    private AdicionadorLinkCredencial adicionadorLink;

    @PostMapping("/cadastrar")
    public ResponseEntity<CredencialRespostaDTO> cadastrar(@RequestBody CredencialRequisicaoDTO requisicao) {
        Credencial entidade = mapeador.requisicaoParaEntidade(requisicao);
        Credencial salvo = servico.salvar(entidade);
        CredencialRespostaDTO resposta = mapeador.entidadeParaResposta(salvo);
        adicionadorLink.adicionarLink(resposta);
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @GetMapping("/obter")
    public ResponseEntity<List<CredencialRespostaDTO>> obterTodos() {
        List<Credencial> entidades = servico.obterTodos();
        List<CredencialRespostaDTO> respostas = entidades.stream()
                .map(mapeador::entidadeParaResposta)
                .collect(Collectors.toList());
        adicionadorLink.adicionarLink(respostas);
        return new ResponseEntity<>(respostas, HttpStatus.FOUND);
    }

    @GetMapping("/obter/{id}")
    public ResponseEntity<CredencialRespostaDTO> obterPorId(@PathVariable Long id) {
        Credencial entidade = servico.obterPorId(id);
        if (entidade == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        CredencialRespostaDTO resposta = mapeador.entidadeParaResposta(entidade);
        adicionadorLink.adicionarLink(resposta);
        return new ResponseEntity<>(resposta, HttpStatus.FOUND);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<CredencialRespostaDTO> atualizar(@PathVariable Long id, @RequestBody CredencialRequisicaoDTO requisicao) {
        Credencial atualizacao = mapeador.requisicaoParaEntidade(requisicao);
        Credencial atualizado = servico.atualizar(id, atualizacao);
        if (atualizado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        CredencialRespostaDTO resposta = mapeador.entidadeParaResposta(atualizado);
        adicionadorLink.adicionarLink(resposta);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        if (servico.obterPorId(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        servico.excluir(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}