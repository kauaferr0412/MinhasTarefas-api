package com.kauaferr.minhasTarefas.service;

import java.util.List;
import java.util.Optional;

import com.kauaferr.minhasTarefas.model.entity.Tarefas;

public interface TarefasService {

	Tarefas salvar(Tarefas lancamento);
	
	Tarefas atualizar(Tarefas lancamento);
	
	void deletar(Tarefas lancamento);
	
	List<Tarefas> buscar(Tarefas lancamento);
		
	void validar(Tarefas lancamento);
	
	Optional<Tarefas> obterPorId(Long id);
	
	int[] obterTotalTarefasConcluidasENaoConcluidas(Long id);
}
