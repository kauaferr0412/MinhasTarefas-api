package com.dsousa.minhasFinancas.controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dsousa.minhasFinancas.DTO.AtualizaStatusDTO;
import com.dsousa.minhasFinancas.DTO.LancamentoDTO;
import com.dsousa.minhasFinancas.exceptions.RegraDeNegocioException;
import com.dsousa.minhasFinancas.model.entity.Lancamento;
import com.dsousa.minhasFinancas.model.entity.Usuario;
import com.dsousa.minhasFinancas.model.enums.StatusLancamento;
import com.dsousa.minhasFinancas.model.enums.TipoLancamento;
import com.dsousa.minhasFinancas.service.LancamentosService;
import com.dsousa.minhasFinancas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

	private final LancamentosService lancamentoService;
	private final UsuarioService userService;

	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto ) {
		try {
			Lancamento ent = converterDTO(dto);
			lancamentoService.salvar(ent);
			return new ResponseEntity(ent, HttpStatus.CREATED);
		}catch (RegraDeNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {
		return lancamentoService.obterPorId(id).map(ent -> {
			try {
				Lancamento lancamento = converterDTO(dto);
				lancamento.setId(ent.getId());
				lancamentoService.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
			}catch (RegraDeNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet( () -> new ResponseEntity("Lancamento não encontrado na base", HttpStatus.BAD_REQUEST));
	}
	
	@PutMapping("{id}/atualiza-status")
	public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO dto) {
		return lancamentoService.obterPorId(id).map(ent -> {
			StatusLancamento status = StatusLancamento.valueOf(dto.getStatus());
			if(Objects.isNull(status)) {
				return ResponseEntity.badRequest().body("Não foi possivel atualizar o status. Informe um status valido.");
			}
			try {
				lancamentoService.atualizarStatus(ent, status);
				return ResponseEntity.ok(ent);
			}catch (RegraDeNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet( () -> new ResponseEntity("Lancamento não encontrado na base", HttpStatus.BAD_REQUEST));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return lancamentoService.obterPorId(id).map(ent -> {
				lancamentoService.deletar(ent);
				return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet( () -> new ResponseEntity("Lancamento não encontrado na base", HttpStatus.BAD_REQUEST));
	}
	
	@GetMapping
	public ResponseEntity buscar(
			@RequestParam(value = "descricao", required = false) String descricao, 
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano,
			@RequestParam(value = "usuario") Long usuario) {
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		Optional<Usuario> usuarioBanco = userService.obterPorId(usuario);
		if(!usuarioBanco.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possivel realizar a consulta. Usuario não encontrado na base");
		}else {
			lancamentoFiltro.setUsuario(usuarioBanco.get());
		}
		
		List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos);
		
	}
	
	private Lancamento converterDTO(LancamentoDTO dto) {
		Lancamento lan = new Lancamento();
		lan.setDescricao(dto.getDescricao());
		lan.setAno(dto.getAno());
		lan.setMes(dto.getMes());
		lan.setValor(dto.getValor());
		lan.setUsuario(userService.obterPorId(dto.getUsuario()).orElseThrow(() -> new RegraDeNegocioException("Usuário não informado para o Id informado")));
		lan.setStatus((Objects.nonNull(dto.getStatus()) ? StatusLancamento.valueOf(dto.getStatus()) : null ));
		lan.setTipo((Objects.nonNull(dto.getTipo()) ? TipoLancamento.valueOf(dto.getTipo()) : null ));
		return lan;
	}
}
