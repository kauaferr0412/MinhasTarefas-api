package com.dsousa.minhasFinancas.DTO;

import java.math.BigDecimal;

import com.dsousa.minhasFinancas.model.entity.Lancamento;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LancamentoDTO {

	private Long id;
	private String descricao;
	private Integer mes;
	private Integer ano;
	private BigDecimal valor;
	private Long usuario;
	private String tipo;
	private String status;
}
