package com.mj.brewer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mj.brewer.model.Cliente;
import com.mj.brewer.repository.helper.ClientesQueries;

@Repository
public interface Clientes extends JpaRepository<Cliente, Long>, ClientesQueries {

	public Optional<Cliente> findByCpfCnpj(String cpfCnpj);

	public List<Cliente> findByNomeStartingWithIgnoreCase(String nome);

}
