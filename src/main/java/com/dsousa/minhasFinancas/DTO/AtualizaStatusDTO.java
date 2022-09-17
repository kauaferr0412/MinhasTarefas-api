package com.dsousa.minhasFinancas.DTO;

import com.dsousa.minhasFinancas.model.enums.StatusLancamento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizaStatusDTO {
	private String status;
}
