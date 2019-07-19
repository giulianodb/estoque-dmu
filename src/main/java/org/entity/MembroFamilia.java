package org.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="membroFamilia",schema="estoque")
public class MembroFamilia implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8996467323445786863L;
	@Id
	@SequenceGenerator(name = "MEMBROFAMILIA_ID", sequenceName = "id_membrofamilia_seq", schema="estoque",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MEMBROFAMILIA_ID")
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne
	private Familia familia;
	
	private String nome;
	
	private Date dataNascimento;
	
	private String rg;
	
	private String cpf;
	
	@Enumerated
	private ParentescoFamiliaEnum parentescoFamiliaEnum;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Familia getFamilia() {
		return familia;
	}

	public void setFamilia(Familia familia) {
		this.familia = familia;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public ParentescoFamiliaEnum getParentescoFamiliaEnum() {
		return parentescoFamiliaEnum;
	}

	public void setParentescoFamiliaEnum(ParentescoFamiliaEnum parentescoFamiliaEnum) {
		this.parentescoFamiliaEnum = parentescoFamiliaEnum;
	}
	
}
