package org.entity;


public enum TipoMedidaEnum {
	
	LITROS("Litros"),
	QUILOS("Quilos"),
	UNIDADE("Unidade"),
	MACO("Maço");	
	
	private String descricao;
	private TipoMedidaEnum(String descricao) {
		this.descricao = descricao;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
