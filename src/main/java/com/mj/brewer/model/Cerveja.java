package com.mj.brewer.model;

import java.beans.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.mj.brewer.validation.SKU;
import com.mysql.jdbc.StringUtils;

@Entity
@Table(name = "cerveja")
public class Cerveja implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String ID = "id";
	public static final String SKU = "sku";
	public static final String NOME = "nome";
	public static final String DESCRICAO = "descricao";
	public static final String FOTO = "foto";
	public static final String VALOR = "valor";
	public static final String TEOR_ALCOOLICO = "teorAlcoolico";
	public static final String COMISSAO = "comissao";
	public static final String QUANTIDADE_ESTOQUE = "quantidadeEstoque";
	public static final String SABOR = "sabor";
	public static final String ORIGEM = "origem";
	public static final String ESTILO = "estilo";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@SKU
	@NotBlank(message = "SKU deve ser informado")
	private String sku;

	@NotBlank(message = "Nome deve ser informado")
	private String nome;

	@Size(max = 300, message = "Descrição excedeu o tamanho máximo")
	private String descricao;

	@Column
	private String foto;

	@NotNull(message = "Valor é obrigatório")
	@DecimalMin(value = "0", message = "O valor não deve ser menor que 0")
	@DecimalMax(value = "9999.99", message = "O valor deve ser inferior a 9999.99")
	@Column
	private BigDecimal valor;

	@NotNull(message = "Teor alcoólico é obrigatório")
	@DecimalMin(value = "0", message = "O teor alcoólico não deve ser menor que 0")
	@DecimalMax(value = "100", message = "O teor alcoólico não deve ser maior que 100")
	@Column(name = "teor_alcoolico")
	private BigDecimal teorAlcoolico;

	@NotNull(message = "Comissão é obrigatório")
	@DecimalMin(value = "0", message = "A comissão deve ser maior que 0")
	@DecimalMax(value = "100", message = "A comissão não deve ser maior que 100")
	@Column
	private BigDecimal comissao;

	@Min(value = 0, message = "A quantidade de estoque deve ser maior ou igual a 0")
	@Max(value = 9999, message = "A quantidade de estoque não ser maior que 9999")
	@Column(name = "quantidade_estoque")
	private int quantidadeEstoque;

	@NotNull(message = "O sabor é obrigatório")
	@Enumerated(EnumType.STRING)
	private Sabor sabor;

	@NotNull(message = "A origem é obrigatório")
	@Enumerated(EnumType.STRING)
	private Origem origem;

	@NotNull(message = "O estilo é obrigatório")
	@ManyToOne()
	@JoinColumn(name = "estilo_id")
	private Estilo estilo;

	@Column(name = "content_type")
	private String contentType;

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Transient
	public boolean isNova() {
		return id == null;
	}

	@PrePersist
	@PreUpdate
	public void prePersistAndUpdate() {
		this.sku = this.sku.toUpperCase();
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getTeorAlcoolico() {
		return teorAlcoolico;
	}

	public void setTeorAlcoolico(BigDecimal teorAlcoolico) {
		this.teorAlcoolico = teorAlcoolico;
	}

	public BigDecimal getComissao() {
		return comissao;
	}

	public void setComissao(BigDecimal comissao) {
		this.comissao = comissao;
	}

	public int getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(int quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public Sabor getSabor() {
		return sabor;
	}

	public void setSabor(Sabor sabor) {
		this.sabor = sabor;
	}

	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	public Estilo getEstilo() {
		return estilo;
	}

	public void setEstilo(Estilo estilo) {
		this.estilo = estilo;
	}

	public boolean temFoto() {
		return !StringUtils.isNullOrEmpty(foto);
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
		Cerveja other = (Cerveja) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cerveja [id=" + id + ", sku=" + sku + ", nome=" + nome + ", descricao=" + descricao + ", foto=" + foto + ", valor=" + valor
				+ ", teorAlcoolico=" + teorAlcoolico + ", comissao=" + comissao + ", quantidadeEstoque=" + quantidadeEstoque + ", sabor="
				+ sabor + ", origem=" + origem + ", estilo=" + estilo + "]";
	}

}
