package com.mj.brewer.repository.helper;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.mj.brewer.model.Usuario;
import com.mj.brewer.model.filter.UsuarioFilter;

public interface UsuariosQueries {

	public Optional<Usuario> findByEmailAtivo(String email);
	
	public List<String> permissoes(Usuario usuario);
	
	public PageImpl<Usuario> filtrar(UsuarioFilter usuarioFilter, Pageable pageable);
	
	public Usuario buscarComGrupos(Long id);

}
