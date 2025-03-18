package com.kauaferr.minhasTarefas.controllers;

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

import com.kauaferr.minhasTarefas.DTO.TarefasDTO;
import com.kauaferr.minhasTarefas.exceptions.RegraDeNegocioException;
import com.kauaferr.minhasTarefas.model.entity.Tarefas;
import com.kauaferr.minhasTarefas.model.entity.Usuario;
import com.kauaferr.minhasTarefas.service.TarefasService;
import com.kauaferr.minhasTarefas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tarefas")
@RequiredArgsConstructor
public class TarefaController {

	private final TarefasService tarefaService;
	private final UsuarioService userService;

	@PostMapping
	public ResponseEntity salvar(@RequestBody TarefasDTO dto ) {
		try {
			Tarefas ent = converterDTO(dto);
			tarefaService.salvar(ent);
			return new ResponseEntity(ent, HttpStatus.CREATED);
		}catch (RegraDeNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PutMapping
	public ResponseEntity atualizar(@RequestParam("id") Long id, @RequestBody TarefasDTO dto) {
		return tarefaService.obterPorId(id).map(ent -> {
			try {
				Tarefas tarefa = converterDTO(dto);
				tarefa.setId(ent.getId());
				tarefaService.atualizar(tarefa);
				return ResponseEntity.ok(tarefa);
			}catch (RegraDeNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet( () -> new ResponseEntity("tarefa não encontrado na base", HttpStatus.BAD_REQUEST));
	}
	

	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return tarefaService.obterPorId(id).map(ent -> {
				tarefaService.deletar(ent);
				return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet( () -> new ResponseEntity("tarefa não encontrado na base", HttpStatus.BAD_REQUEST));
	}
	
	@GetMapping
	public ResponseEntity buscar(
			@RequestParam(value = "descricao", required = false) String descricao, 
			@RequestParam(value = "concluido", required = false) Boolean concluido, 
			@RequestParam(value = "usuario") Long usuario) {
		Tarefas tarefaFiltro = new Tarefas();
		tarefaFiltro.setDescricao(descricao);
		tarefaFiltro.setConcluido(concluido);
		Optional<Usuario> usuarioBanco = userService.obterPorId(usuario);
		if(!usuarioBanco.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possivel realizar a consulta. Usuario não encontrado na base");
		}else {
			tarefaFiltro.setUsuario(usuarioBanco.get());
		}
		
		List<Tarefas> tarefas = tarefaService.buscar(tarefaFiltro);
		return ResponseEntity.ok(tarefas);
		
	}

	@GetMapping
	public String liveEdnpoint() {
		return "A API está em funcionamento";

	}
	
	private Tarefas converterDTO(TarefasDTO dto) {
		return Tarefas.builder()
				.descricao(dto.getDescricao())
				.usuario(userService.obterPorId(dto.getUsuario()).orElseThrow(() -> new RegraDeNegocioException("Usuário não informado para o Id informado")))
				.concluido(Objects.nonNull(dto.getConcluido()) ? dto.getConcluido(): false)
				.build();
		
		
	}
}
