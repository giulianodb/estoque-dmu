package org.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="loteMovimentacao",schema="estoque")
public class LoteMovimentacao implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8996466788745786863L;
	
	@SequenceGenerator(name = "LOTEMOVIMENTACAO_ID", sequenceName = "id_lotemovimentacao_seq", schema="estoque",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "LOTEMOVIMENTACAO_ID")
	@Id
	private Integer codigo;
	
	private String numeroEntrada;
	
	//Esses atribtutos serão populados de acordo com o tipo de doador
	@ManyToOne
	private Campanha campanha;
	
	@ManyToOne
	private Instituicao instituicao;
	
	@ManyToOne
	private Doador doador;
	
	@Temporal(value=TemporalType.TIMESTAMP )
	@Column(name="dataEntrada")
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

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	
}
