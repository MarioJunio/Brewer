package com.mj.brewer.model;

import com.mj.brewer.repository.Usuarios;

public enum UsuarioStatus {

	ATIVAR {
		
		@Override
		public void executar(Long[] codigos, Usuarios usuarios) {
			usuarios.findByCodigoIn(codigos).forEach(u -> u.setAtivo(true));
		}
	},

	DESATIVAR {
		
		@Override
		public void executar(Long[] codigos, Usuarios usuarios) {
			usuarios.findByCodigoIn(codigos).forEach(u -> u.setAtivo(false));
		}
	};

	public abstract void executar(Long[] codigos, Usuarios usuarios);

}
