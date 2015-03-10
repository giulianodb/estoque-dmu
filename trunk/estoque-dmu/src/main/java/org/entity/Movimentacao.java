package org.entity;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="movimentacao",schema="estoque")
public class Movimentacao implements Serializable{
	
	private static final long serialVersionUID = 8996467323445786863L;
	@Id
	@SequenceGenerator(name = "MOVIMENTACAO_ID", sequenceName = "id_movimentacao_seq", schema="estoque",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MOVIMENTACAO_ID")
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne
	private Campanha campanha;
	
	@ManyToOne
	private Instituicao instituicao;
	
	@ManyToOne
	private Familia familia;
	
	//Caso a saída for de campanha essa é a família que irá receber a saída de estqoue
	@ManyToOne
	private Familia familiaCampanha;
	
	//Atributo que define qual é o ultimo valor medio quando essa saída foi efetuda
	private Float valorMediaUltimo;
	
//	Atributo que define qual é a quantidade quando essa saída foi efetuda
	private Float quantidadeUltimo;
	
//	Atributo que define qual era o saldo em valor essa saída foi efetuda
	private Float saldoUltimo;
	
	@Inject
	@ManyToOne
	private Produto produto;
	
	//Definir qual lote essa entrada faz parte
	@ManyToOne
	@Inject
	private LoteMovimentacao loteMovimentacao;
	
	private Float quantidade;
	
	private Float valor;
	
	private String numeroNF;
	
	private String descricaoNota;
	
	@Temporal(value=TemporalType.TIMESTAMP )
	@Column(name="data")
	private Date data;
	
	@Enumerated
	private TipoMovimentacaoEnum tipoMovimentacaoEnum;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Familia getFamiliaCampanha() {
		return familiaCampanha;
	}

	public void setFamiliaCampanha(Familia familiaCampanha) {
		this.familiaCampanha = familiaCampanha;
	}

	public Float getValorMediaUltimo() {
		return valorMediaUltimo;
	}

	public void setValorMediaUltimo(Float valorMediaUltimo) {
		this.valorMediaUltimo = valorMediaUltimo;
	}

	public Float getQuantidadeUltimo() {
		return quantidadeUltimo;
	}

	public void setQuantidadeUltimo(Float quantidadeUltimo) {
		this.quantidadeUltimo = quantidadeUltimo;
	}

	public Float getSaldoUltimo() {
		return saldoUltimo;
	}

	public void setSaldoUltimo(Float saldoUltimo) {
		this.saldoUltimo = saldoUltimo;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public LoteMovimentacao getLoteMovimentacao() {
		return loteMovimentacao;
	}

	public void setLoteMovimentacao(LoteMovimentacao loteMovimentacao) {
		this.loteMovimentacao = loteMovimentacao;
	}

	public Float getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Float quantidade) {
		this.quantidade = quantidade;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

	public String getNumeroNF() {
		return numeroNF;
	}

	public void setNumeroNF(String numeroNF) {
		this.numeroNF = numeroNF;
	}

	public String getDescricaoNota() {
		return descricaoNota;
	}

	public void setDescricaoNota(String descricaoNota) {
		this.descricaoNota = descricaoNota;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public TipoMovimentacaoEnum getTipoMovimentacaoEnum() {
		return tipoMovimentacaoEnum;
	}

	public void setTipoMovimentacaoEnum(TipoMovimentacaoEnum tipoMovimentacaoEnum) {
		this.tipoMovimentacaoEnum = tipoMovimentacaoEnum;
	}
	
	
	
}
