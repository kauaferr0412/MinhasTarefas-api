package com.kauaferr.minhasTarefas.controllers;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kauaferr.minhasTarefas.DTO.UsuarioDTO;
import com.kauaferr.minhasTarefas.exceptions.RegraDeNegocioException;
import com.kauaferr.minhasTarefas.model.entity.Usuario;
import com.kauaferr.minhasTarefas.service.TarefasService;
import com.kauaferr.minhasTarefas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
	
	private final UsuarioService userService;
	private final TarefasService tarefaService;
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {
		try {
			Usuario userAutenticado = userService.autenticar(dto.getEmail(), dto.getSenha());
			return new ResponseEntity(userAutenticado, HttpStatus.OK);
		}catch(RegraDeNegocioException erro) {
			return ResponseEntity.badRequest().body(erro.getMessage());
		}
	}
	
	@PostMapping("/salvar")
	public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
		try {
			Usuario userSalvo = userService.salvarUsuario(Usuario.builder()
					.nome(dto.getNome())
					.email(dto.getEmail())
					.senha(dto.getSenha())
					.build());
			return new ResponseEntity(userSalvo, HttpStatus.CREATED);
		}catch(RegraDeNegocioException erro) {
			return ResponseEntity.badRequest().body(erro.getMessage());
		}
	}
	
	
	@GetMapping("/total")
	public ResponseEntity obterTotalTarefasConcluidasENaoConcluidas(@RequestParam(value = "usuario") Long id) {
		Optional<Usuario> user = userService.obterPorId(id);
		
		if(!user.isPresent()) {
			return ResponseEntity.badRequest().body("Usuario não encontrado na base de dados");
		}
		
		int[] saldo = tarefaService.obterTotalTarefasConcluidasENaoConcluidas(id);
		return  ResponseEntity.ok(saldo);

	}
}
