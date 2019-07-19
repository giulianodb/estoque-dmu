package org.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="configuracao",schema="estoque")
public class Configuracao implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8996467323447003284L;

	/**
	 * Define qual último ano já foi realizado os cadastros
	 */
	@Id
	private Integer id;
	
	private Integer mes;
	
	private Date dataUltimaMovimentacao;

	@Column(name = "ano_resolvido")
	private Integer ano;

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Date getDataUltimaMovimentacao() {
		return dataUltimaMovimentacao;
	}

	public void setDataUltimaMovimentacao(Date dataUltimaMovimentacao) {
		this.dataUltimaMovimentacao = dataUltimaMovimentacao;
	}
	
	
}
