package com.mj.brewer.model;

import com.mj.brewer.model.validation.group.PessoaJuridicaGroups;
import com.mj.brewer.model.validation.group.PessoaFisicaGroups;

public enum TipoPessoa {
	FISICA(PessoaFisicaGroups.class, "Física", "CPF", "000.000.000-00") {
		@Override
		public String formatar(String cpf) {
			return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
		}
	},

	JURIDICA(PessoaJuridicaGroups.class, "Jurídica", "CNPJ", "00.000.000/0000-00") {
		@Override
		public String formatar(String cnpj) {
			return cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})", "$1.$2.$3/$4-");
		}
	};

	TipoPessoa(Class<?> group, String descricao, String documento, String mascara) {
		this.group = group;
		this.descricao = descricao;
		this.documento = documento;
		this.mascara = mascara;
	}

	private Class<?> group;
	private String descricao;
	private String documento;
	private String mascara;

	public abstract String formatar(String cpfCnpj);

	public String getDescricao() {
		return descricao;
	}

	public String getDocumento() {
		return documento;
	}

	public String getMascara() {
		return mascara;
	}

	public Class<?> getGroup() {
		return group;
	}

}
