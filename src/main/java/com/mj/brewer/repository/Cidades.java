package com.mj.brewer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mj.brewer.model.Cidade;
import com.mj.brewer.model.Estado;
import com.mj.brewer.repository.helper.CidadesQueries;

@Repository
public interface Cidades extends JpaRepository<Cidade, Long>, CidadesQueries {

	@Query("select a from #{#entityName} a where a.estado = ?1 order by a.nome asc")
	public List<Cidade> findByEstado(Estado estado);
	
	public List<Cidade> findByNomeIgnoreCaseAndEstado(String cidade, Estado estado);
}
