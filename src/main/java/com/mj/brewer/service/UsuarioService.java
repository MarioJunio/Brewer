package com.mj.brewer.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mj.brewer.controller.page.PageWrapper;
import com.mj.brewer.model.Usuario;
import com.mj.brewer.model.UsuarioStatus;
import com.mj.brewer.model.filter.UsuarioFilter;
import com.mj.brewer.repository.Usuarios;
import com.mj.brewer.service.exception.EmailJaCadastradoException;
import com.mj.brewer.service.exception.ImpossivelExcluirEntidade;

@Service
public class UsuarioService {

	@Autowired
	private Usuarios usuarios;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	public Usuario salvar(Usuario usuario) {
		Usuario usuarioEncontrado = usuarios.findByEmailIgnoreCase(usuario.getEmail());

		System.out.println("usuario editado: " + usuario.getEmail());
		System.out.println("usuario editado encontrado: " + usuarioEncontrado.getEmail());

		if (usuarioEncontrado != null && !usuarioEncontrado.equals(usuario))
			throw new EmailJaCadastradoException("Email já está sendo usado!");

		// se for edicao use a senha atual
		if (usuario.isEdicao()) {
			usuario.setSenha(usuarioEncontrado.getSenha());
			usuario.setConfirmacaoSenha(usuarioEncontrado.getConfirmacaoSenha());
		} else {
			// transforma a senha em um hash
			usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
			usuario.setConfirmacaoSenha(usuario.getSenha());
		}

		return usuarios.saveAndFlush(usuario);
	}

	public PageWrapper<Usuario> usuarios(UsuarioFilter usuarioFilter, Pageable pageable, HttpServletRequest httpServletRequest) {
		return new PageWrapper<Usuario>(usuarios.filtrar(usuarioFilter, pageable), httpServletRequest);
	}

	@Transactional
	public void alterarStatus(Long[] codigos, UsuarioStatus usuarioStatus) {
		usuarioStatus.executar(codigos, usuarios);
	}

	@Transactional(readOnly = true)
	public Usuario buscarComGrupos(Long id) {
		return usuarios.buscarComGrupos(id);
	}

	@Transactional
	public void excluir(Usuario usuario) {
		
		try {
			usuarios.delete(usuario);
			usuarios.flush();
		} catch (Exception e) {
			throw new ImpossivelExcluirEntidade(usuario.getNome() + " não pode ser excluído!");
		}
	}
}
