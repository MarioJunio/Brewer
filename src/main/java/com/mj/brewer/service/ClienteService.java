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
import com.mj.brewer.service.exception.ImpossivelExcluirEntidade;

@Service
public class ClienteService {

	@Autowired
	private Clientes clientes;

	@Transactional
	public Cliente salvar(Cliente cliente) {
		Optional<Cliente> clienteAtual = null;

		// se estiver editando o cliente
		if (cliente.isEdicao()) {
			clienteAtual = clientes.findById(cliente.getId());

			System.out.println("Edicao: " + cliente.getCpfCnpj());
			System.out.println("Encontrado: " + clienteAtual.get().getCpfCnpj());
		}

		// se for edição verifica se o CPF/CNPJ é diferente do atual, se não verifica se
		// ele já foi cadastrado
		if ((cliente.isEdicao() && !clienteAtual.get().getCpfCnpj().equals(cliente.getCpfCnpj())
				&& cpfCnpjCadastrado(cliente.getCpfCnpjSemFormatacao()))
				|| (cliente.isNovo() && cpfCnpjCadastrado(cliente.getCpfCnpjSemFormatacao())))
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

	@Transactional(readOnly = true)
	public Cliente buscarComEndereco(Long id) {
		return clientes.buscarComEndereco(id);
	}

	@Transactional
	public void excluir(Cliente cliente) {

		try {
			clientes.delete(cliente);
			clientes.flush();
		} catch (Exception e) {
			throw new ImpossivelExcluirEntidade(cliente.getNome() + " não pode ser excluído!");
		}
	}
	
	@Transactional(readOnly = false)
	public Long quantidadeClientes() {
		return clientes.quantidadeClientes();
	}

}
