package com.dsousa.minhasFinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dsousa.minhasFinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
