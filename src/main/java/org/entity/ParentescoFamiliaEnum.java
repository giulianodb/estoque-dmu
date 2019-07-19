package org.entity;


public enum ParentescoFamiliaEnum {
	
	FILHO("Filho(a)"),
	ESPOSA("Esposa"),
	MARIDO("Marido"),
	TIO("Tio"),
	TIA("Tia"),
	AVO1("Avô"),
	AVO2("Avó"),
	MAE("Mãe"),
	PAI("Pai"),
	SOBRINHO("Sobrinho(a)"),
	ENTIADO("Entiado(a)"),
	PADASTRO("Padastro"),
	MADASTRA("Madastra")
	;	
	
	private String descricao;
	private ParentescoFamiliaEnum(String descricao) {
		this.descricao = descricao;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
