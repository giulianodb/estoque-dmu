package org.entity;


public enum TipoProdutoEnum {
	/**
	 * 
	 */
	ALIMENTO("Alimento"),
	BRINQUEDO("Brinquedo"),
	ESCRITORIO("Escritório"),
	LIMPEZA_HIGIENE("Limpeza e higiene"),
	VESTUARIO("Vestuário"),
	CAMA_MESA("Cama, mesa e banho"),
	OUTROS("Outros");
	
	
	private String descricao;
	
	private TipoProdutoEnum(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	

	
	
}
