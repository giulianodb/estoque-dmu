package org.dto;

import java.util.List;

public class RelatorioEntradaProdutoDTO {
	
	private String nomeProduto;
	
	private List<EntradaDTO> entradas;

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public List<EntradaDTO> getEntradas() {
		return entradas;
	}

	public void setEntradas(List<EntradaDTO> entradas) {
		this.entradas = entradas;
	}
}
