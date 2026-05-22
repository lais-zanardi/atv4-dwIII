package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.requisicao.UsuarioRequisicaoDTO;
import com.autobots.automanager.dtos.resposta.UsuarioRespostaDTO;
import com.autobots.automanager.enums.Perfil;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.mapeador.UsuarioMapeador;
import com.autobots.automanager.modelos.AdicionadorLinkUsuario;
import com.autobots.automanager.servicos.UsuarioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControle {

	@Autowired private UsuarioServico servico;
	@Autowired private UsuarioMapeador mapeador;
	@Autowired private AdicionadorLinkUsuario adicionadorLink;

	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@PostMapping("/cadastrar")
	public ResponseEntity<UsuarioRespostaDTO> cadastrarUsuario(@RequestBody UsuarioRequisicaoDTO requisicao) {
		Usuario entidade = mapeador.requisicaoParaEntidade(requisicao);
		Usuario salvo = servico.salvar(entidade);
		UsuarioRespostaDTO resposta = mapeador.entidadeParaResposta(salvo);
		adicionadorLink.adicionarLink(resposta);
		return new ResponseEntity<>(resposta, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENDEDOR')")
	@GetMapping("/obter")
	public ResponseEntity<List<UsuarioRespostaDTO>> obterUsuarios() {
		List<Usuario> usuarios = servico.obterTodos();
		List<UsuarioRespostaDTO> respostas = usuarios.stream()
				.map(mapeador::entidadeParaResposta)
				.collect(Collectors.toList());
		adicionadorLink.adicionarLink(respostas);
		return new ResponseEntity<>(respostas, HttpStatus.FOUND);
	}

	@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
	@GetMapping("/obter/{id}")
	public ResponseEntity<UsuarioRespostaDTO> obterUsuario(@PathVariable Long id) {
		Usuario usuario = servico.buscarUsuario(id);
		if (usuario == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		UsuarioRespostaDTO resposta = mapeador.entidadeParaResposta(usuario);
		adicionadorLink.adicionarLink(resposta);
		return new ResponseEntity<>(resposta, HttpStatus.FOUND);
	}

	@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENDEDOR')")
	@PutMapping("/atualizar/{id}")
	public ResponseEntity<UsuarioRespostaDTO> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequisicaoDTO requisicao) {
		Usuario atualizacao = mapeador.requisicaoParaEntidade(requisicao);
		atualizacao.setId(id);
		Usuario atualizado = servico.atualizar(atualizacao);
		if (atualizado == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		UsuarioRespostaDTO resposta = mapeador.entidadeParaResposta(atualizado);
		adicionadorLink.adicionarLink(resposta);
		return new ResponseEntity<>(resposta, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirUsuario(@PathVariable Long id) {
		if (servico.buscarUsuario(id) == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		servico.excluir(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}