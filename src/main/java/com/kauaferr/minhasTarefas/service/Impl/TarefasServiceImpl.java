package com.kauaferr.minhasTarefas.service.Impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kauaferr.minhasTarefas.exceptions.RegraDeNegocioException;
import com.kauaferr.minhasTarefas.model.entity.Tarefas;
import com.kauaferr.minhasTarefas.model.repository.TarefasRepository;
import com.kauaferr.minhasTarefas.service.TarefasService;

@Service
public class TarefasServiceImpl implements TarefasService{

	private TarefasRepository tarefasRepository;
	
	public TarefasServiceImpl(TarefasRepository tarefaRepository) {
		this.tarefasRepository = tarefaRepository;
	}
	@Override
	@Transactional
	public Tarefas salvar(Tarefas tarefa) {
		validar(tarefa);
		return tarefasRepository.save(tarefa);
	}

	@Override
	@Transactional
	public Tarefas atualizar(Tarefas tarefa) {
		Objects.requireNonNull(tarefa.getId());
		validar(tarefa);
		return tarefasRepository.save(tarefa);
	}

	@Override
	@Transactional
	public void deletar(Tarefas tarefa) {
		Objects.requireNonNull(tarefa.getId());
		tarefasRepository.delete(tarefa);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Tarefas> buscar(Tarefas tarefas) {
		return tarefasRepository.
				findAll(Example.of(tarefas, 
						ExampleMatcher.matching()
						.withIgnoreCase()
						.withStringMatcher(StringMatcher.CONTAINING)));
	}

//	@Override
//	public void atualizarStatus(Tarefas tarefa, StatusLancamento status) {
////		lancamento.setStatus(status);
////		atualizar(lancamento);
//		
//	}

	@Override
	public void validar(Tarefas tarefa) {
		if(Objects.isNull(tarefa.getDescricao()) || tarefa.getDescricao().trim().equals("")) {
			throw new RegraDeNegocioException("Informe uma descrição válida");
		}
					
		if(Objects.isNull(tarefa.getUsuario()) || Objects.isNull(tarefa.getUsuario().getId())) {
			throw new RegraDeNegocioException("Informe um usuário");
		}
			
	}
	
	@Override
	public Optional<Tarefas> obterPorId(Long id) {
		System.out.println(id);

		return tarefasRepository.findById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public int[] obterTotalTarefasConcluidasENaoConcluidas(Long id) {
		int[] somasTarefas = new int[2];

		List<Tarefas> somaConcluidos = tarefasRepository.obterTotalTarefasConcluidas(id);
		List<Tarefas> somaNaoConcluidos = tarefasRepository.obterTotalTarefasNaoConcluidas(id);

		if(Objects.isNull(somaConcluidos)) {
			somasTarefas[0] = 0;
		}
		if(Objects.isNull(somaNaoConcluidos)) {
			somasTarefas[1] = 0;
		}
		
		somasTarefas[0] = somaConcluidos.size();
		somasTarefas[1] = somaNaoConcluidos.size();
		return somasTarefas;
	}
	
}
