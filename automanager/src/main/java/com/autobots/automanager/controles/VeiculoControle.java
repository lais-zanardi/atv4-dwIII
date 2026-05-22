package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.requisicao.VeiculoRequisicaoDTO;
import com.autobots.automanager.dtos.resposta.VeiculoRespostaDTO;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.mapeador.VeiculoMapeador;
import com.autobots.automanager.modelos.AdicionadorLinkVeiculo;
import com.autobots.automanager.servicos.VeiculoServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/veiculos")
public class VeiculoControle {

    @Autowired private VeiculoServico servico;
    @Autowired private VeiculoMapeador mapeador;
    @Autowired private AdicionadorLinkVeiculo adicionadorLink;

    @PostMapping("/cadastrar/{idProprietario}")
    public ResponseEntity<VeiculoRespostaDTO> cadastrarVeiculo(@RequestBody VeiculoRequisicaoDTO requisicao, @PathVariable Long idProprietario) {
        Veiculo entidade = mapeador.requisicaoParaEntidade(requisicao);
        Veiculo salvo = servico.salvar(entidade, idProprietario);

        if (salvo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Se o proprietário não for encontrado
        }

        VeiculoRespostaDTO resposta = mapeador.entidadeParaResposta(salvo);
        adicionadorLink.adicionarLink(resposta);
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @GetMapping("/obter")
    public ResponseEntity<List<VeiculoRespostaDTO>> obterVeiculos() {
        List<Veiculo> veiculos = servico.obterTodos();
        List<VeiculoRespostaDTO> respostas = veiculos.stream()
                .map(mapeador::entidadeParaResposta)
                .collect(Collectors.toList());
        adicionadorLink.adicionarLink(respostas);
        return new ResponseEntity<>(respostas, HttpStatus.FOUND);
    }

    @GetMapping("/obter/{id}")
    public ResponseEntity<VeiculoRespostaDTO> obterVeiculo(@PathVariable Long id) {
        Veiculo veiculo = servico.obterPorId(id);
        if (veiculo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        VeiculoRespostaDTO resposta = mapeador.entidadeParaResposta(veiculo);
        adicionadorLink.adicionarLink(resposta);
        return new ResponseEntity<>(resposta, HttpStatus.FOUND);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<VeiculoRespostaDTO> atualizarVeiculo(@PathVariable Long id, @RequestBody VeiculoRequisicaoDTO requisicao) {
        Veiculo atualizacao = mapeador.requisicaoParaEntidade(requisicao);
        atualizacao.setId(id);

        Veiculo atualizado = servico.atualizar(atualizacao);
        if (atualizado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VeiculoRespostaDTO resposta = mapeador.entidadeParaResposta(atualizado);
        adicionadorLink.adicionarLink(resposta);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluirVeiculo(@PathVariable Long id) {
        if (servico.obterPorId(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        servico.excluir(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}