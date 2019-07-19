package org.entity;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="movimentacao",schema="estoque")
public class Movimentacao implements Serializable{
	
	private static final long serialVersionUID = 8996467323445786863L;
	@Id
	@SequenceGenerator(name = "MOVIMENTACAO_ID", sequenceName = "id_movimentacao_seq", schema="estoque",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MOVIMENTACAO_ID")
	@Column(name = "id")
	private Integer id;
	
	private Float valorMediaUltimo;
	
//	Atributo que define qual é a quantidade quando essa saída foi efetuda
	private Float quantidadeUltimo;
	
//	Atributo que define qual era o saldo em valor essa saída foi efetuda
	private Float saldoUltimo;
	
	@Inject
	@ManyToOne(fetch=FetchType.EAGER)
	private Produto produto;
	
	//Definir qual lote essa entrada faz parte
	@ManyToOne
	@Inject
	private LoteMovimentacao loteMovimentacao;
	
	private Float quantidade;
	
	private Float valor;
	
	private String numeroNF;
	
	private String descricaoNota;
	
	@Temporal(value=TemporalType.TIMESTAMP )
	@Column(name="data")
	private Date data;

	
	@Enumerated
	private TipoMovimentacaoEnum tipoMovimentacaoEnum;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public LoteMovimentacao getLoteMovimentacao() {
		return loteMovimentacao;
	}

	public void setLoteMovimentacao(LoteMovimentacao loteMovimentacao) {
		this.loteMovimentacao = loteMovimentacao;
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

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public TipoMovimentacaoEnum getTipoMovimentacaoEnum() {
		return tipoMovimentacaoEnum;
	}

	public void setTipoMovimentacaoEnum(TipoMovimentacaoEnum tipoMovimentacaoEnum) {
		this.tipoMovimentacaoEnum = tipoMovimentacaoEnum;
	}
	
	public boolean movimentacaoIsEntrada(){
		if (tipoMovimentacaoEnum.equals(TipoMovimentacaoEnum.ENTRADA)){
			return true;
		}
		else {
			return false;
		}
		
	}
	
	@Override
	public String toString() {
		return produto.getId() +" - " + produto.getNome();
	}
	
	
}
