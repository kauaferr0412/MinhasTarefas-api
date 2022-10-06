package com.kauaferr.minhasTarefas.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kauaferr.minhasTarefas.model.entity.Tarefas;

public interface TarefasRepository extends JpaRepository<Tarefas, Long>{

	@Query(value = "select t from Tarefas t join t.usuario u where u.id = :idUsuario and t.concluido = true")
	List<Tarefas> obterTotalTarefasConcluidas(@Param("idUsuario") Long idUsuario);
	
	@Query(value = "select t from Tarefas t join t.usuario u where u.id = :idUsuario and t.concluido = false")
	List<Tarefas> obterTotalTarefasNaoConcluidas(@Param("idUsuario") Long idUsuario);
}
