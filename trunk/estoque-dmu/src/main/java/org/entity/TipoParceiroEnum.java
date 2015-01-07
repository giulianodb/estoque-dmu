package org.entity;


public enum TipoParceiroEnum {
	
	CAMPANHA("Campanha"),
	FAMILIA("Família"),
	ANONIMO("Anônimo"),
	INSTITUICAO("Instiuição"),
	PESSOA("Pessoa física");
	
	private String descricao;
	private TipoParceiroEnum(String descricao) {
		this.descricao = descricao;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
