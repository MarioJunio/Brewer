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

@Service
public class UsuarioService {

	@Autowired
	private Usuarios usuarios;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	public Usuario salvar(Usuario usuario) {
		Usuario usuarioEncontrado = usuarios.findByEmailIgnoreCase(usuario.getEmail());

		if (!usuarioEncontrado.equals(usuario) && usuarioEncontrado != null)
			throw new EmailJaCadastradoException("Email já está sendo usado!");

		// se for edicao use a senha atual
		if (usuario.isEdicao()) {
			usuario.setSenha(usuarioEncontrado.getSenha());
			usuario.setConfirmacaoSenha(usuarioEncontrado.getConfirmacaoSenha());
		}

		// transforma a senha em um hash
		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		usuario.setConfirmacaoSenha(usuario.getSenha());

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
}
