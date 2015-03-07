package org.entity;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="entrada",schema="estoque")
public class Entrada implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8996467323445786863L;
	@Id
	@SequenceGenerator(name = "ENTRADA_ID", sequenceName = "id_entrada_seq", schema="estoque",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ENTRADA_ID")
	@Column(name = "id")
	private Integer id;
	
	@Inject
	@ManyToOne
	private Produto produto;
	
	//Definir qual lote essa entrada faz parte
	@ManyToOne
	@Inject
	private LoteEntrada loteEntrada;
	
	private Float quantidade;
	
	private Float valor;
	
	private String numeroNF;
	
	private String descricaoNota;
	
	private Float valorMediaUltimo;
	
	private Float quantidadeUltimo;
	
	@Temporal(value=TemporalType.TIMESTAMP )
	@Column(name="dataEntrada")
	private Date data;
	
//	Atributo que define qual era o saldo em valor essa saída foi efetuda
	private Float saldoUltimo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public LoteEntrada getLoteEntrada() {
		return loteEntrada;
	}

	public void setLoteEntrada(LoteEntrada loteEntrada) {
		this.loteEntrada = loteEntrada;
	}

	public Float getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Float quantidade) {
		this.quantidade = quantidade;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

	public String getNumeroNF() {
		return numeroNF;
	}

	public void setNumeroNF(String numeroNF) {
		this.numeroNF = numeroNF;
	}

	public String getDescricaoNota() {
		return descricaoNota;
	}

	public void setDescricaoNota(String descricaoNota) {
		this.descricaoNota = descricaoNota;
	}

	public Float getValorMediaUltimo() {
		return valorMediaUltimo;
	}

	public void setValorMediaUltimo(Float valorMediaUltimo) {
		this.valorMediaUltimo = valorMediaUltimo;
	}

	public Float getQuantidadeUltimo() {
		return quantidadeUltimo;
	}

	public void setQuantidadeUltimo(Float quantidadeUltimo) {
		this.quantidadeUltimo = quantidadeUltimo;
	}

	public Float getSaldoUltimo() {
		return saldoUltimo;
	}

	public void setSaldoUltimo(Float saldoUltimo) {
		this.saldoUltimo = saldoUltimo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	


	
}
