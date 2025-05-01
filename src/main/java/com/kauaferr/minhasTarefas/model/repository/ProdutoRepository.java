package com.kauaferr.minhasTarefas.model.repository;

import com.kauaferr.minhasTarefas.model.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}