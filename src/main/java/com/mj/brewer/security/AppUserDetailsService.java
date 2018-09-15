package com.mj.brewer.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mj.brewer.model.Usuario;
import com.mj.brewer.model.security.UsuarioAutenticado;
import com.mj.brewer.repository.Usuarios;

@Service
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private Usuarios usuarios;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = usuarios.findByEmailAtivo(email)
				.orElseThrow(() -> new UsernameNotFoundException("O email/senha estão incorreto(s)"));

		return new UsuarioAutenticado(usuario, loadPermissoes(usuario));
	}

	public Collection<? extends GrantedAuthority> loadPermissoes(Usuario usuario) {
		Set<GrantedAuthority> permissoes = new HashSet<>();

		usuarios.permissoes(usuario).forEach(p -> permissoes.add(new SimpleGrantedAuthority(p)));

		return permissoes;
	}

}
