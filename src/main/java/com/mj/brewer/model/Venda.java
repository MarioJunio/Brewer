package com.mj.brewer.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "venda")
public class Venda implements Serializable {

	private static final long serialVersionUID = 1L;

	// FIELD NAMES
	public static final String ID = "id";
	public static final String DATA_CRIACAO = "dataCriacao";
	public static final String VALOR_FRETE = "valorFrete";
	public static final String VALOR_DESCONTO = "valorDesconto";
	public static final String VALOR_TOTAL = "valorTotal";
	public static final String OBSERVACAO = "observacao";
	public static final String DATA_ENTREGA = "dataEntrega";
	public static final String STATUS_ENTREGA = "statusVenda";
	public static final String VENDEDOR = "vendedor";
	public static final String CLIENTE = "cliente";
	public static final String ITENS = "itens";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "data_criacao")
	private LocalDateTime dataCriacao;

	@Column(name = "valor_frete")
	private BigDecimal valorFrete;

	@Column(name = "valor_desconto")
	private BigDecimal valorDesconto;

	@Column(name = "valor_total")
	private BigDecimal valorTotal;

	@Column(name = "observacao")
	private String observacao;

	@Column(name = "data_entrega")
	private LocalDateTime dataEntrega;

	@Enumerated(EnumType.STRING)
	@Column(name = "status_venda")
	private StatusVenda statusVenda = StatusVenda.ORCAMENTO;

	@ManyToOne()
	@JoinColumn(name = "vendedor_id")
	private Usuario vendedor;

	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "venda")
	private List<ItemVenda> itens = new ArrayList<>();

	@Transient
	private UUID uuid;

	@Transient
	private LocalDate dataDaEntrega;

	@Transient
	private LocalTime horaDaEntrega;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public LocalDateTime getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(LocalDateTime dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public StatusVenda getStatusVenda() {
		return statusVenda;
	}

	public void setStatusVenda(StatusVenda statusVenda) {
		this.statusVenda = statusVenda;
	}

	public Usuario getVendedor() {
		return vendedor;
	}

	public void setVendedor(Usuario vendedor) {
		this.vendedor = vendedor;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<ItemVenda> getItens() {
		return itens;
	}

	public void setItens(List<ItemVenda> itens) {
		this.itens = itens;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public LocalDate getDataDaEntrega() {
		return dataDaEntrega;
	}

	public void setDataDaEntrega(LocalDate dataDaEntrega) {
		this.dataDaEntrega = dataDaEntrega;
	}

	public LocalTime getHoraDaEntrega() {
		return horaDaEntrega;
	}

	public void setHoraDaEntrega(LocalTime horaDaEntrega) {
		this.horaDaEntrega = horaDaEntrega;
	}

	public void adicionarItens(List<ItemVenda> itens) {
		setItens(itens);
		calcularValorTotal();
		itens.forEach(iv -> iv.setVenda(this));
	}

	public BigDecimal getSubTotal() {
		return itens.stream().map(ItemVenda::getValorTotal).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
	}

	public void calcularValorTotal() {
		setValorTotal(getSubTotal().add(Optional.ofNullable(valorFrete).orElse(BigDecimal.ZERO)
				.subtract(Optional.ofNullable(valorDesconto).orElse(BigDecimal.ZERO))));
	}

	@Override
	public String toString() {
		return "Venda [id=" + id + ", dataCriacao=" + dataCriacao + ", valorFrete=" + valorFrete + ", valorDesconto=" + valorDesconto
				+ ", valorTotal=" + valorTotal + ", observacao=" + observacao + ", dataEntrega=" + dataEntrega + ", statusVenda="
				+ statusVenda + ", vendedor=" + vendedor + ", cliente=" + cliente + ", itens=" + itens.size() + ", uuid=" + uuid
				+ ", dataDaEntrega=" + dataDaEntrega + ", horaDaEntrega=" + horaDaEntrega + "]";
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
		Venda other = (Venda) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
