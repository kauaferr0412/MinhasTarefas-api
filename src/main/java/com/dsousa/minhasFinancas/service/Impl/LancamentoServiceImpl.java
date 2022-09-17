package com.dsousa.minhasFinancas.service.Impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.transaction.annotation.Transactional;

import com.dsousa.minhasFinancas.exceptions.RegraDeNegocioException;
import com.dsousa.minhasFinancas.model.entity.Lancamento;
import com.dsousa.minhasFinancas.model.enums.StatusLancamento;
import com.dsousa.minhasFinancas.model.repository.LancamentoRepository;
import com.dsousa.minhasFinancas.service.LancamentosService;

public class LancamentoServiceImpl implements LancamentosService{

	private LancamentoRepository lancamentoRepository;
	
	public LancamentoServiceImpl(LancamentoRepository lancamentoRepository) {
		this.lancamentoRepository = lancamentoRepository;
	}
	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PEDNENTE);
		return lancamentoRepository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validar(lancamento);
		return lancamentoRepository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		lancamentoRepository.delete(lancamento);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamento) {
		return lancamentoRepository.findAll(Example.of(lancamento, ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING)));
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);
		
	}

	@Override
	public void validar(Lancamento lancamento) {

		if(Objects.isNull(lancamento.getDescricao()) || lancamento.getDescricao().trim().equals("")) {
			throw new RegraDeNegocioException("Informe uma descrição válida");
		}
		
		if(Objects.isNull(lancamento.getMes()) || lancamento.getMes() < 1 || lancamento.getMes() > 12 ) {
			throw new RegraDeNegocioException("Informe um mês válido");
		}
		
		if(Objects.isNull(lancamento.getUsuario()) || Objects.isNull(lancamento.getUsuario().getId())) {
			throw new RegraDeNegocioException("Informe um usuário");
		}
		
		if(Objects.isNull(lancamento.getValor()) || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1 ) {
			throw new RegraDeNegocioException("Informe um valor válido");
		}
		
		if(Objects.isNull(lancamento.getTipo())) {
			throw new RegraDeNegocioException("Informe um tipo de lançamento");
		}
	}
	
}
