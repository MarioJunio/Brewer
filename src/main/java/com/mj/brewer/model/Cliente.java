package com.mj.brewer.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.group.GroupSequenceProvider;

import com.mj.brewer.model.validation.ClienteGroupSequenceProvider;
import com.mj.brewer.model.validation.group.PessoaFisicaGroups;
import com.mj.brewer.model.validation.group.PessoaJuridicaGroups;
import com.mj.brewer.utils.Utils;

@Entity
@Table(name = "cliente")
@GroupSequenceProvider(ClienteGroupSequenceProvider.class)
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	public final static String ID = "id";
	public final static String NOME = "nome";
	public final static String TIPO_PESSOA = "tipoPessoa";
	public final static String CPF_CNPJ = "cpfCnpj";
	public final static String TELEFONE = "telefone";
	public final static String EMAIL = "email";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Nome é obrigatório")
	private String nome;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_pessoa")
	private TipoPessoa tipoPessoa;

	@NotBlank(message = "CPF/CNPJ é obrigatório")
	@CPF(groups = PessoaFisicaGroups.class)
	@CNPJ(groups = PessoaJuridicaGroups.class)
	@Column(name = "cpf_cnpj")
	private String cpfCnpj;

	@NotBlank(message = "Telefone é obrigatório")
	private String telefone;

	@Email(message = "E-mail inválido")
	private String email;

	@Embedded
	private Endereco endereco;

	@PrePersist
	@PreUpdate
	private void preInsertOrUpdate() {
		this.cpfCnpj = getCpfCnpjSemFormatacao();
	}

	@PostLoad
	private void postLoad() {
		this.cpfCnpj = tipoPessoa.formatar(this.cpfCnpj);
	}

	public boolean isNovo() {
		return id == null;
	}

	public boolean isEdicao() {
		return id != null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getCpfCnpjSemFormatacao() {
		return Utils.removerMascaraCpfCnpj(cpfCnpj);
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nome=" + nome + ", tipoPessoa=" + tipoPessoa + ", cpfCnpj=" + cpfCnpj + ", telefone=" + telefone
				+ ", email=" + email + ", endereco=" + endereco + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
