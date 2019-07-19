package org.entity;


public enum TipoMovimentacaoEnum {
	
	ENTRADA("Entrada"),
	SAIDA("Saída");
	
	private String descricao;
	
	private TipoMovimentacaoEnum(String descricao) {
		this.descricao = descricao;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
