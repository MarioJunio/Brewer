package com.mj.brewer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mj.brewer.model.Usuario;
import com.mj.brewer.repository.helper.UsuariosQueries;

@Repository
public interface Usuarios extends JpaRepository<Usuario, Long>, UsuariosQueries {

	public Usuario findByEmailIgnoreCase(String email);
	
	public List<Usuario> findByCodigoIn(Long[] codigos);
}
