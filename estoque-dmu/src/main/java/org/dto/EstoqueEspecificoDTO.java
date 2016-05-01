package org.dto;


public class EstoqueEspecificoDTO {
	private String data;
	
	private String documentoSaida = "";
	
	private String quantidadeSaida = "";
	
	private String valorMedioSaida = "";
	
	private String valorTotalSaida = "";
	
	private String nomeProduto;
	
	private String codigo;
	
	private Float quantidadeProdutoF;
	
	private Float valorTotalF;
	
	private Integer codProduto;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDocumentoSaida() {
		return documentoSaida;
	}

	public void setDocumentoSaida(String documentoSaida) {
		this.documentoSaida = documentoSaida;
	}

	public String getQuantidadeSaida() {
		return quantidadeSaida;
	}

	public void setQuantidadeSaida(String quantidadeSaida) {
		this.quantidadeSaida = quantidadeSaida;
	}

	public String getValorMedioSaida() {
		return valorMedioSaida;
	}

	public void setValorMedioSaida(String valorMedioSaida) {
		this.valorMedioSaida = valorMedioSaida;
	}

	public String getValorTotalSaida() {
		return valorTotalSaida;
	}

	public void setValorTotalSaida(String valorTotalSaida) {
		this.valorTotalSaida = valorTotalSaida;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
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

	public Integer getCodProduto() {
		return codProduto;
	}

	public void setCodProduto(Integer codProduto) {
		this.codProduto = codProduto;
	}

}
