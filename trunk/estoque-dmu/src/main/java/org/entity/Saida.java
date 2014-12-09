package org.entity;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="saida",schema="estoque")
public class Saida implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8996467323444586863L;
	@Id
	@SequenceGenerator(name = "SAIDA_ID", sequenceName = "id_saida_seq", schema="estoque",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SAIDA_ID")
	@Column(name = "id")
	private Integer id;
	
	@Inject
	@ManyToOne
	private Produto produto;
	
	private Float quantidade;
	
	private Date data;
	
	@ManyToOne
	private Campanha campanha;
	
	@ManyToOne
	private Instituicao instituicao;
	
	@ManyToOne
	private Familia familia;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Float getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Float quantidade) {
		this.quantidade = quantidade;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
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

	public Familia getFamilia() {
		return familia;
	}

	public void setFamilia(Familia familia) {
		this.familia = familia;
	}

	
}
