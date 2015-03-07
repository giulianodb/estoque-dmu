package org.entity;


public enum TipoMedidaEnum {
	
	LITROS("Litros","L"),
	QUILOS("Quilos","Kg"),
	UNIDADE("Unidade","UN"),
	MACO("Maço","Mc");	
	
	private String descricao;
	private String abreviatura;
	private TipoMedidaEnum(String descricao, String abreviatura) {
		this.descricao = descricao;
		this.abreviatura = abreviatura;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getAbreviatura() {
		return abreviatura;
	}
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}
}
