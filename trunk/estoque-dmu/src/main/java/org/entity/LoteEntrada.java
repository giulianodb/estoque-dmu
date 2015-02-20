package org.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="loteEntrada",schema="estoque")
public class LoteEntrada implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8996466788745786863L;

	@Id
	private String numeroEntrada;
	
	//Esses atribtutos serão populados de acordo com o tipo de doador
	@ManyToOne
	private Campanha campanha;
	
	@ManyToOne
	private Instituicao instituicao;
	
	@ManyToOne
	private Doador doador;

	private Date data;

	public String getNumeroEntrada() {
		return numeroEntrada;
	}

	public void setNumeroEntrada(String numeroEntrada) {
		this.numeroEntrada = numeroEntrada;
	}

	public Campanha getCampanha() {
		return campanha;
	}

	public void setCampanha(Campanha campanha) {
		this.campanha = campanha;
	}

	public Instituicao getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(Instituicao instituicao) {
		this.instituicao = instituicao;
	}

	public Doador getDoador() {
		return doador;
	}

	public void setDoador(Doador doador) {
		this.doador = doador;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	
}
