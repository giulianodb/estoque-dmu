package org.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="usuario",schema="estoque")
public class Usuario implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3795035789464662511L;
	
	@Id
	@SequenceGenerator(name = "USUARIO_ID", sequenceName = "id_usuario_seq", schema="controle",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "USUARIO_ID")
	@Column(name = "id_usuario")
	private Integer idUsuario;
	
	@Size(max=50)
	private String nome;
	
	@ManyToOne 
	private Papel papel;
	
	@Column(unique=true)
	private String loginUsuario;
	
	private String senha;
	
	private String email;
	
	private Boolean participaExtra;
	
	private Float participacao;

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Papel getPapel() {
		return papel;
	}

	public void setPapel(Papel papel) {
		this.papel = papel;
	}

	public String getLoginUsuario() {
		return loginUsuario;
	}

	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getParticipaExtra() {
		return participaExtra;
	}

	public void setParticipaExtra(Boolean participaExtra) {
		this.participaExtra = participaExtra;
	}

	public Float getParticipacao() {
		return participacao;
	}

	public void setParticipacao(Float participacao) {
		this.participacao = participacao;
	}
	

	
}
