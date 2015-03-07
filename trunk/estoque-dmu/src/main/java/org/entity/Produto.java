package org.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.util.NumeroUtil;

@Entity
@Table(name="produto",schema="estoque")
//@NamedQueries({ @NamedQuery(name = "pesquisarProduto", query = "SELECT c FROM Caso c") })
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
	
	@OneToMany(mappedBy = "produto",fetch = FetchType.LAZY)
//	@IndexColumn(name="produto")
//	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("data asc")
	private Set<Saida> listaSaida;
	
	@OrderBy("data asc")
	@OneToMany(mappedBy = "produto", fetch = FetchType.LAZY)
//	@LazyCollection(LazyCollectionOption.FALSE)
//	@IndexColumn(name="produto")
	private Set<Entrada> listaEntrada;
	
	//mètodo responsavel em retornar o valor médio historico do produto em questão
	public Float valorMedioProduto(){
		if (quantidadeHistoricaTotal == null || quantidadeHistoricaTotal < 1){
			return 0f;
		}
		return NumeroUtil.DividirDinheiro(valorHistoricoTotal,quantidadeHistoricaTotal, 3);
	}
	
	/**
	 * Usado para retornoar a quantidade em estoque anterior.
	 * Normalmente é obtido uma lista de produtos e cada produto tem um lista de entradas e saidas então é obitdo
	 * a entrada/saida mais antiga e retorna qual era a quantidade anterior- USADO PARA RELATORIOS
	 * @return
	 */
	public Float quantidadeAnterior(){
		if (listaEntrada != null && listaSaida != null){
			List<Entrada> entradas = new ArrayList<Entrada>(listaEntrada);
			List<Saida> saidas = new ArrayList<Saida>(listaSaida);
			
			Entrada primeiraEntrada = null;
			Saida primeiraSaida = null;
			
			Date dataEntrada = null;
			Date dataSaida = null;
			
			if (entradas.size() > 0){
				primeiraEntrada = entradas.get(0);
				
			}
			
			if (saidas.size() > 0){
				primeiraSaida = saidas.get(0);
				
			}
			
			if (primeiraEntrada != null){
				dataEntrada = primeiraEntrada.getData();
			} else {
				if (primeiraSaida != null){
					return primeiraSaida.getQuantidadeUltimo();
				}
			}
			
			
			if (primeiraSaida != null){
				dataSaida = primeiraSaida.getData();
			}
			else {
					if (primeiraEntrada != null){
						return primeiraEntrada.getQuantidadeUltimo();
					}
			}
			
			
			if (dataEntrada == null && dataSaida == null){
				return 0f;
			}
			
			//caso todos os dois vierem com valores deve retornar o maior
			
			if (dataEntrada.before(dataSaida)){
				return primeiraEntrada.getQuantidadeUltimo();
			}
			else {
				return primeiraSaida.getQuantidadeUltimo();
			}
			
		} else {
			System.out.println("LISTA VAZIAS..");
			return 0F;
		}
		
	}
	
	
	/**
	 * Usado para retornoar a quantidade em estoque anterior.
	 * Normalmente é obtido uma lista de produtos e cada produto tem um lista de entradas e saidas então é obitdo
	 * a entrada/saida mais antiga e retorna qual era a quantidade anterior- USADO PARA RELATORIOS
	 * @return
	 */
	public Float valorTotalAnterior(){
		if (listaEntrada != null && listaSaida != null){

			List<Entrada> entradas = new ArrayList<Entrada>(listaEntrada);
			List<Saida> saidas = new ArrayList<Saida>(listaSaida);
			
			
			Entrada primeiraEntrada = null;
			Saida primeiraSaida = null;
			
			if (entradas.size() > 0){
				primeiraEntrada = entradas.get(0);
				
			}
			
			if (saidas.size() > 0){
				primeiraSaida = saidas.get(0);
				
			}
			
			Date dataEntrada = null;
			Date dataSaida = null;
			
			if (primeiraEntrada != null){
				dataEntrada = primeiraEntrada.getData();
			} else {
				if (primeiraSaida != null){
					return primeiraSaida.getSaldoUltimo();
				}
			}
			
			
			if (primeiraSaida != null){
				dataSaida = primeiraSaida.getData();
			}
			else {
					if (primeiraEntrada != null){
						return primeiraEntrada.getSaldoUltimo();
					}
			}
			
			if (dataEntrada == null && dataSaida == null){
				return 0f;
			}
			
			//caso todos os dois vierem com valores deve retornar o maior
		
			if (dataEntrada.before(dataSaida)){
				return primeiraEntrada.getSaldoUltimo();
			}
			else {
				return primeiraSaida.getSaldoUltimo();
			}
			
		} else {
			System.out.println("LISTA VAZIAS..");
			return 0F;
		}
		
	}
	
	/**
	 * Usado para retornoar a quantidade em estoque anterior.
	 * Normalmente é obtido uma lista de produtos e cada produto tem um lista de entradas e saidas então é obitdo
	 * a entrada/saida mais antiga e retorna qual era a quantidade anterior- USADO PARA RELATORIOS
	 * @return
	 */
	public Float quantidadeUltimo(){
		if (listaEntrada != null && listaSaida != null){
			List<Entrada> entradas = new ArrayList<Entrada>(listaEntrada);
			List<Saida> saidas = new ArrayList<Saida>(listaSaida);
			Saida ultimaSaida = null;
			Entrada ultimaEntrada = null;
			
			if (entradas.size() > 0){
				ultimaEntrada = entradas.get(entradas.size()-1);
				
			}
			
			if (saidas.size() > 0){
				ultimaSaida = saidas.get(saidas.size()-1);
				
			}
		
			
			Date dataEntrada = null;
			Date dataSaida = null;
			
			if (ultimaEntrada != null){
				dataEntrada = ultimaEntrada.getData();
			} else {
				if (ultimaSaida != null){
					
					return NumeroUtil.diminuirDinheiro(ultimaSaida.getQuantidadeUltimo(), ultimaSaida.getQuantidade(), 3);
				}
			}
			
			
			if (ultimaSaida != null){
				dataSaida = ultimaSaida.getData();
			}
			else {
					if (ultimaEntrada != null){
						return ultimaEntrada.getQuantidadeUltimo() + ultimaEntrada.getQuantidade();
					}
			}
			
			if (dataEntrada == null && dataSaida == null){
				return 0f;
			}
			
			//caso todos os dois vierem com valores deve retornar o maior
		
			if (dataEntrada.after(dataSaida)){
				return ultimaEntrada.getQuantidadeUltimo() + ultimaEntrada.getQuantidade();
			}
			else {
				return NumeroUtil.diminuirDinheiro(ultimaSaida.getQuantidadeUltimo(), ultimaSaida.getQuantidade(), 3);
			}
			
		} else {
			System.out.println("LISTA VAZIAS..");
			return 0F;
		}
		
	}
	
	
	/**
	 * Usado para retornoar a quantidade em estoque anterior.
	 * Normalmente é obtido uma lista de produtos e cada produto tem um lista de entradas e saidas então é obitdo
	 * a entrada/saida mais antiga e retorna qual era a quantidade anterior- USADO PARA RELATORIOS
	 * @return
	 */
	public Float valorTotalUltimoUltimo(){
		if (listaEntrada != null && listaSaida != null){
			List<Entrada> entradas = new ArrayList<Entrada>(listaEntrada);
			List<Saida> saidas = new ArrayList<Saida>(listaSaida);
			Saida ultimaSaida = null;
			Entrada ultimaEntrada = null;
			
			if (entradas.size() > 0){
				ultimaEntrada = entradas.get(entradas.size()-1);
				
			}
			
			if (saidas.size() > 0){
				ultimaSaida = saidas.get(saidas.size()-1);
				
			}
		
			
			Date dataEntrada = null;
			Date dataSaida = null;
			
			if (ultimaEntrada != null){
				dataEntrada = ultimaEntrada.getData();
			} else {
				if (ultimaSaida != null){
					
					return ultimaSaida.getSaldoUltimo()+ultimaSaida.getValorMediaUltimo();
				}
			}
			
			
			if (ultimaSaida != null){
				dataSaida = ultimaSaida.getData();
			}
			else {
					if (ultimaEntrada != null){
						return ultimaEntrada.getSaldoUltimo() + ultimaEntrada.getValor();
					}
			}
			
			if (dataEntrada == null && dataSaida == null){
				return 0f;
			}
			//caso todos os dois vierem com valores deve retornar o maior
		
			if (dataEntrada.after(dataSaida)){
				return ultimaEntrada.getSaldoUltimo() + ultimaEntrada.getValor();
			}
			else {
				return ultimaSaida.getSaldoUltimo() - (NumeroUtil.multiplicarDinheiro(ultimaSaida.getValorMediaUltimo(), ultimaSaida.getQuantidade(), 3));
			}
			
		} else {
			System.out.println("LISTA VAZIAS..");
			return 0F;
		}
		
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

	public Set<Saida> getListaSaida() {
		return listaSaida;
	}

	public void setListaSaida(Set<Saida> listaSaida) {
		this.listaSaida = listaSaida;
	}

	public Set<Entrada> getListaEntrada() {
		return listaEntrada;
	}

	public void setListaEntrada(Set<Entrada> listaEntrada) {
		this.listaEntrada = listaEntrada;
	}



}
