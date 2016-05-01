package org.dto;

import java.io.Serializable;

public class RelatorioCampanhaDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer codigoProduto;
	private String nomeProduto;
	private String quantidadeProduto;
	private String valorTotal;
	private String tipoMovimentacao;
	
	private Float quantidadeProdutoF;
	
	private Float valorTotalF;
	public Integer getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(Integer codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public String getQuantidadeProduto() {
		return quantidadeProduto;
	}
	public void setQuantidadeProduto(String quantidadeProduto) {
		this.quantidadeProduto = quantidadeProduto;
	}
	public String getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}
	public String getTipoMovimentacao() {
		return tipoMovimentacao;
	}
	public void setTipoMovimentacao(String tipoMovimentacao) {
		this.tipoMovimentacao = tipoMovimentacao;
	}
	public Float getQuantidadeProdutoF() {
		return quantidadeProdutoF;
	}
	public void setQuantidadeProdutoF(Float quantidadeProdutoF) {
		this.quantidadeProdutoF = quantidadeProdutoF;
	}
	public Float getValorTotalF() {
		return valorTotalF;
	}
	public void setValorTotalF(Float valorTotalF) {
		this.valorTotalF = valorTotalF;
	}

	
	
}