package org.entity;


public enum TipoProdutoEnum {
	/**
	 * 
	 */
	ALIMENTO("Alimento"),
	BRINQUEDO("Brinquedo"),
	ESCRITORIO("Escritório"),
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
