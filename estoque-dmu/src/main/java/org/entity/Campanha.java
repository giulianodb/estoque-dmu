package org.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.util.DateUtil.MesesEnum;

@Entity
@Table(name="campanha",schema="estoque")
public class Campanha implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8996467323447003863L;
	@Id
	@SequenceGenerator(name = "CAMPANHA_ID", sequenceName = "id_campanha_seq", schema="estoque",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CAMPANHA_ID")
	@Column(name = "id")
	private Integer id;
	
	private String nome;
	
	private String descricao;
	
	@Enumerated
	private MesesEnum mesInicio;
	
	@Enumerated
	private MesesEnum mesFim;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public MesesEnum getMesInicio() {
		return mesInicio;
	}

	public void setMesInicio(MesesEnum mesInicio) {
		this.mesInicio = mesInicio;
	}

	public MesesEnum getMesFim() {
		return mesFim;
	}

	public void setMesFim(MesesEnum mesFim) {
		this.mesFim = mesFim;
	}




	
	
}
