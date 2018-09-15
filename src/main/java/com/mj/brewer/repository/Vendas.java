package com.mj.brewer.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mj.brewer.model.Venda;
import com.mj.brewer.repository.helper.VendasQueries;

@Repository
public interface Vendas extends JpaRepository<Venda, Long>, VendasQueries {
	
}
