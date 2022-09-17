package com.dsousa.minhasFinancas.service;

import java.util.List;

import com.dsousa.minhasFinancas.model.entity.Lancamento;
import com.dsousa.minhasFinancas.model.enums.StatusLancamento;

public interface LancamentosService {

	Lancamento salvar(Lancamento lancamento);
	
	Lancamento atualizar(Lancamento lancamento);
	
	void deletar(Lancamento lancamento);
	
	List<Lancamento> buscar(Lancamento lancamento);
	
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	void validar(Lancamento lancamento);
}
