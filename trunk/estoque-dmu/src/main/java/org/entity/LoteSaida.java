package org.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="loteSaida",schema="estoque")
public class LoteSaida implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8996466788745786863L;

	@Id
	private String numeroSaida;
	
	//Esses atribtutos serão populados de acordo com o tipo de doador
	
//	@ManyToOne
//	private Instituicao instituicao;

	private Date data;

}
