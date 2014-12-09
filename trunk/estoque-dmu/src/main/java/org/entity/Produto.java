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
import javax.validation.constraints.Size;

import org.util.NumeroUtil;

@Entity
@Table(name="produto",schema="estoque")
public class Produto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3795035789464662556L;
	
	@Id
	@SequenceGenerator(name = "PRODUTO_ID", sequenceName = "id_produto_seq", schema="estoque",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PRODUTO_ID")
	@Column(name = "id")
	private Integer id;
	
	@Size(max=50)
	private String nome;
	
	@Enumerated
	private TipoProdutoEnum tipoProduto;
	
	@Enumerated
	private TipoMedidaEnum tipoMedida;
	
	private Float quantidadeEstoque;
	
	//Campo para obter a média do produto quando necessário sem a necessidade de percorrer o banco sempre
	private Float quantidadeHistoricaTotal;
	
	//Campo para obter a média do produto quando necessário sem a necessidade de percorrer o banco sempre
	private Float valorHistoricoTotal;
	
	private String descricao;
	
	//mètodo responsavel em retornar o valor médio historico do produto em questão
	public Float valorMedioProduto(){
		if (quantidadeHistoricaTotal == null || quantidadeHistoricaTotal < 1){
			return 0f;
		}
		return NumeroUtil.DividirDinheiro(valorHistoricoTotal,quantidadeHistoricaTotal, 3);
	}
	
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

	public TipoProdutoEnum getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(TipoProdutoEnum tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public TipoMedidaEnum getTipoMedida() {
		return tipoMedida;
	}

	public void setTipoMedida(TipoMedidaEnum tipoMedida) {
		this.tipoMedida = tipoMedida;
	}

	public Float getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Float quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Float getQuantidadeHistoricaTotal() {
		return quantidadeHistoricaTotal;
	}

	public void setQuantidadeHistoricaTotal(Float quantidadeHistoricaTotal) {
		this.quantidadeHistoricaTotal = quantidadeHistoricaTotal;
	}

	public Float getValorHistoricoTotal() {
		return valorHistoricoTotal;
	}

	public void setValorHistoricoTotal(Float valorHistoricoTotal) {
		this.valorHistoricoTotal = valorHistoricoTotal;
	}

}
