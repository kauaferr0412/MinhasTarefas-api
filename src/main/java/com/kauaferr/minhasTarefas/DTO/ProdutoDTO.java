package com.kauaferr.minhasTarefas.DTO;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoDTO {
    private Long id;
    private String title;
    private String description;
    private String categoryId;
    private Integer quantity;
    private String currencyId;
    private BigDecimal unitPrice;
    private String pictureUrl;
}