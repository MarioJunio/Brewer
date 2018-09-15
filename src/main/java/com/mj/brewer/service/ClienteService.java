package com.mj.brewer.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mj.brewer.controller.page.PageWrapper;
import com.mj.brewer.model.Cliente;
import com.mj.brewer.model.filter.ClienteFilter;
import com.mj.brewer.repository.Clientes;
import com.mj.brewer.service.exception.ClienteJaCadastradoException;

@Service
public class ClienteService {

	@Autowired
	private Clientes clientes;

	@Transactional
	public Cliente salvar(Cliente cliente) {

		if (cpfCnpjCadastrado(cliente.getCpfCnpjSemFormatacao()))
			throw new ClienteJaCadastradoException("CPF/CNPJ já cadastrado!");

		return clientes.saveAndFlush(cliente);
	}

	public boolean cpfCnpjCadastrado(String cpfCnpj) {
		Optional<Cliente> optionalCliente = clientes.findByCpfCnpj(cpfCnpj);
		return optionalCliente.isPresent();
	}

	public PageWrapper<Cliente> clientes(ClienteFilter clienteFilter, Pageable pageable, HttpServletRequest httpRequest) {
		return new PageWrapper<Cliente>(clientes.filtrar(clienteFilter, pageable), httpRequest);
	}
	
	public List<Cliente> pesquisarPorNome(String nome) {
		return clientes.findByNomeStartingWithIgnoreCase(nome);
	}

}
