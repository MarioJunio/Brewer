package com.mj.brewer.repository.helper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mj.brewer.model.Cliente;
import com.mj.brewer.model.filter.ClienteFilter;

public interface ClientesQueries {
	
	Page<Cliente> filtrar(ClienteFilter clienteFilter, Pageable pageable);
	
	Cliente buscarComEndereco(Long id);
	
	Long quantidadeClientes();
}
