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
import javax.persistence.Transient;
import javax.validation.constraints.Size;

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
	
	private String nomeSemAcento;
	
	@Enumerated
	private TipoProdutoEnum tipoProduto;
	
	@Enumerated
	private TipoMedidaEnum tipoMedida;
	
	private Float quantidadeEstoque;
	
	private Float saldoEstoque;
	
	//Campo para obter a média do produto quando necessário sem a necessidade de percorrer o banco sempre
	private Float quantidadeHistoricaTotal;
	
	//Campo para obter a média do produto quando necessário sem a necessidade de percorrer o banco sempre
	private Float valorHistoricoTotal;
	
	private String descricao;
	
	@OneToMany(mappedBy = "produto",fetch = FetchType.LAZY)
	@OrderBy("data asc")
	private Set<Movimentacao> listaMovimentacao;
	
	//Listas usadas para separar as movimentacoes de entrada e saida
	@Transient
	private List<Movimentacao> movimentacaoEntrada;
	@Transient
	private List<Movimentacao> movimentacaoSaida;
	
	
	@Enumerated
	@Column(name="status")
	private Status status;
	
	
	//mètodo responsavel em retornar o valor médio historico do produto em questão
	public Float valorMedioHistoricoProduto(){
		if (quantidadeHistoricaTotal == null || quantidadeHistoricaTotal < 1){
			return 0f;
		}
		return NumeroUtil.DividirDinheiro(valorHistoricoTotal,quantidadeHistoricaTotal, 3);
	}
	
	public Float valorMedioAtualProduto(){
//		if (quantidadeEstoque == null || saldoEstoque < 1){
			if (quantidadeEstoque == null){
			return 0f;
		}
		return NumeroUtil.DividirDinheiro(saldoEstoque,quantidadeEstoque, 10);
	}
	
	
	public void montarListasEntradaSaida(){
		List<Movimentacao> movimentacoes = new ArrayList<Movimentacao>(listaMovimentacao);
		if (movimentacaoEntrada == null || movimentacaoSaida == null){
			movimentacaoEntrada = new ArrayList<Movimentacao>();
			movimentacaoSaida = new ArrayList<Movimentacao>();
			for (Movimentacao movimentacao : movimentacoes) {
				if (movimentacao.getTipoMovimentacaoEnum().equals(TipoMovimentacaoEnum.ENTRADA)){
					movimentacaoEntrada.add(movimentacao);
				}
				else {
					movimentacaoSaida.add(movimentacao);
				}
			}
			
		}
		
	}
	
	/**
	 * Usado para retornoar a quantidade em estoque anterior.
	 * Normalmente é obtido uma lista de produtos e cada produto tem um lista de entradas e saidas então é obitdo
	 * a entrada/saida mais antiga e retorna qual era a quantidade anterior- USADO PARA RELATORIOS
	 * @return
	 */
	public Float quantidadeAnterior(){
		this.montarListasEntradaSaida();
		
		Movimentacao primeiraEntrada = null;
		Movimentacao primeiraSaida = null;
			
		Date dataEntrada = null;
		Date dataSaida = null;
			
		if (movimentacaoEntrada.size() > 0){
			primeiraEntrada = movimentacaoEntrada.get(0);
			
		}
			
		if (movimentacaoSaida.size() > 0){
			primeiraSaida = movimentacaoSaida.get(0);
			
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
		
		// verifica se são iguais, caso for retorna id menor
		if (dataEntrada.equals(dataSaida)){
			if (primeiraEntrada.getId() < primeiraSaida.getId()){
				return primeiraEntrada.getQuantidadeUltimo();
			}
			else {
				return primeiraSaida.getQuantidadeUltimo();
			}
				
		}
			
		//caso todos os dois vierem com valores deve retornar o maior
			
		if (dataEntrada.before(dataSaida)){
			return primeiraEntrada.getQuantidadeUltimo();
		}
		else {
			return primeiraSaida.getQuantidadeUltimo();
		}
		
	}
	
	
	/**
	 * Usado para retornoar a quantidade em estoque anterior.
	 * Normalmente é obtido uma lista de produtos e cada produto tem um lista de entradas e saidas então é obitdo
	 * a entrada/saida mais antiga e retorna qual era a quantidade anterior- USADO PARA RELATORIOS
	 * @return
	 */
	public Float valorTotalAnterior(){
		this.montarListasEntradaSaida();
		if (id.equals(279)) {
			System.out.println("OPA");
		}
		Movimentacao primeiraEntrada = null;
		Movimentacao primeiraSaida = null;
			
		if (movimentacaoEntrada.size() > 0){
			primeiraEntrada = movimentacaoEntrada.get(0);
		}
			
		if (movimentacaoSaida.size() > 0){
			primeiraSaida = movimentacaoSaida.get(0);
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
			
		//Verifica se possuem datas iguais, se tiver datas iguais retorna id menor.
		
		if (dataEntrada.equals(dataSaida)){
			if (primeiraEntrada.getId() < primeiraSaida.getId()){
				return primeiraEntrada.getSaldoUltimo();
			}
			else {
				return primeiraSaida.getSaldoUltimo();
			}
				
		}
		
		//caso todos os dois vierem com valores deve retornar o maior

		if (dataEntrada.before(dataSaida)){
			return primeiraEntrada.getSaldoUltimo();
		}
		else {
			return primeiraSaida.getSaldoUltimo();
		}
			
	}
	
	/**
	 * Usado para retornoar a quantidade em estoque anterior.
	 * Normalmente é obtido uma lista de produtos e cada produto tem um lista de entradas e saidas então é obitdo
	 * a entrada/saida mais antiga e retorna qual era a quantidade anterior- USADO PARA RELATORIOS
	 * @return
	 */
	public Float quantidadeUltimo(){
		this.montarListasEntradaSaida();
		
		Movimentacao ultimaSaida = null;
		Movimentacao ultimaEntrada = null;
		
		if (movimentacaoEntrada.size() > 0){
			ultimaEntrada = movimentacaoEntrada.get(movimentacaoEntrada.size()-1);
			
		}
		
		if (movimentacaoSaida.size() > 0){
			ultimaSaida = movimentacaoSaida.get(movimentacaoSaida.size()-1);
		}
			
		Date dataEntrada = null;
		Date dataSaida = null;
		
		if (ultimaEntrada != null){
			dataEntrada = ultimaEntrada.getData();
		} else {
			if (ultimaSaida != null){
				return NumeroUtil.diminuirDinheiro(ultimaSaida.getQuantidadeUltimo(), ultimaSaida.getQuantidade(), 6);
			}
		}
			
		if (ultimaSaida != null){
			dataSaida = ultimaSaida.getData();
		}
		else {
			if (ultimaEntrada != null){
				return NumeroUtil.somarDinheiro(ultimaEntrada.getQuantidadeUltimo(), ultimaEntrada.getQuantidade(), 6) ;
			}
		}
			
		if (dataEntrada == null && dataSaida == null){
			return 0f;
		}
			
		
	//Verifica se possuem datas iguais, se tiver datas iguais retorna id menor.
		
		if (dataEntrada.equals(dataSaida)){
			if (ultimaEntrada.getId() > ultimaSaida.getId()){
				return NumeroUtil.somarDinheiro(ultimaEntrada.getQuantidadeUltimo(), ultimaEntrada.getQuantidade(), 6) ;
			}
			else {
				return NumeroUtil.diminuirDinheiro(ultimaSaida.getQuantidadeUltimo(), ultimaSaida.getQuantidade(), 6);
			}
				
		}
		
		//caso todos os dois vierem com valores deve retornar o maior
		
		if (dataEntrada.after(dataSaida)){
			return NumeroUtil.somarDinheiro(ultimaEntrada.getQuantidadeUltimo(), ultimaEntrada.getQuantidade(), 6) ;
		}
		else {
			return NumeroUtil.diminuirDinheiro(ultimaSaida.getQuantidadeUltimo(), ultimaSaida.getQuantidade(), 6);
		}
		
	}
	
	
	/**
	 * Usado para retornoar a quantidade em estoque anterior.
	 * Normalmente é obtido uma lista de produtos e cada produto tem um lista de entradas e saidas então é obitdo
	 * a entrada/saida mais antiga e retorna qual era a quantidade anterior- USADO PARA RELATORIOS
	 * @return
	 */
	public Float valorTotalUltimoUltimo(){
		this.montarListasEntradaSaida();
		
		Movimentacao ultimaSaida = null;
		Movimentacao ultimaEntrada = null;
			
		if (movimentacaoEntrada.size() > 0){
			ultimaEntrada = movimentacaoEntrada.get(movimentacaoEntrada.size()-1);
				
		}
			
		if (movimentacaoSaida.size() > 0){
			ultimaSaida = movimentacaoSaida.get(movimentacaoSaida.size()-1);
			
		}
			
		Date dataEntrada = null;
		Date dataSaida = null;
			
		if (ultimaEntrada != null){
			dataEntrada = ultimaEntrada.getData();
		} else {
			if (ultimaSaida != null){
				
				return NumeroUtil.diminuirDinheiro(ultimaSaida.getSaldoUltimo(), ultimaSaida.getValor(), 6);
			}
		}
			
			
		if (ultimaSaida != null){
			dataSaida = ultimaSaida.getData();
		}
		else {
			if (ultimaEntrada != null){
				return NumeroUtil.somarDinheiro(ultimaEntrada.getSaldoUltimo(), ultimaEntrada.getValor(), 6);
				}
		}
			
		if (dataEntrada == null && dataSaida == null){
			return 0f;
		}
		
		
	//Verifica se possuem datas iguais, se tiver datas iguais retorna id menor.
		
		if (dataEntrada.equals(dataSaida)){
			if (ultimaEntrada.getId() > ultimaSaida.getId()){
				return NumeroUtil.somarDinheiro(ultimaEntrada.getSaldoUltimo(), ultimaEntrada.getValor(), 6) ;
			}
			else {
				Float result = NumeroUtil.diminuirDinheiro(ultimaSaida.getSaldoUltimo(), ultimaSaida.getValor(), 6);
				return NumeroUtil.deixarFloatDuasCasasSimplificado(result);
			}
				
		}
		
		
		//caso todos os dois vierem com valores deve retornar o maior
		
		if (dataEntrada.after(dataSaida)){
			return NumeroUtil.somarDinheiro(ultimaEntrada.getSaldoUltimo(), ultimaEntrada.getValor(), 6) ;
		}
		else {
			Float result = NumeroUtil.diminuirDinheiro(ultimaSaida.getSaldoUltimo(), ultimaSaida.getValor(), 6);
			return NumeroUtil.deixarFloatDuasCasasSimplificado(result);
//			return NumeroUtil.diminuirDinheiro(ultimaSaida.getSaldoUltimo(), ultimaSaida.getValor(), 6);
			
//			return ultimaSaida.getSaldoUltimo() - (NumeroUtil.multiplicarDinheiro(ultimaSaida.getValorMediaUltimo(), ultimaSaida.getQuantidade(), 3));
		}
		
	}
	
	
	/**
	 * Usado para retornoar o saldo do estoque anterior.
	 * Normalmente é obtido uma lista de produtos e cada produto tem um lista de entradas e saidas então é obitdo
	 * a entrada/saida mais antiga e retorna qual o sanldo anterior- USADO PARA RELATORIOS
	 * @return
	 */
	public Float saldoUltimo(){
		this.montarListasEntradaSaida();
		
		Movimentacao ultimaSaida = null;
		Movimentacao ultimaEntrada = null;
		
		if (movimentacaoEntrada.size() > 0){
			ultimaEntrada = movimentacaoEntrada.get(movimentacaoEntrada.size()-1);
			
		}
		
		if (movimentacaoSaida.size() > 0){
			ultimaSaida = movimentacaoSaida.get(movimentacaoSaida.size()-1);
		}
			
		Date dataEntrada = null;
		Date dataSaida = null;
		
		if (ultimaEntrada != null){
			dataEntrada = ultimaEntrada.getData();
		} else {
			if (ultimaSaida != null){
				return NumeroUtil.diminuirDinheiro(ultimaSaida.getSaldoUltimo(), ultimaSaida.getValor(), 3);
			}
		}
			
		if (ultimaSaida != null){
			dataSaida = ultimaSaida.getData();
		}
		else {
			if (ultimaEntrada != null){
				return NumeroUtil.somarDinheiro(ultimaEntrada.getSaldoUltimo(), ultimaEntrada.getValor(), 6) ;
			}
		}
			
		if (dataEntrada == null && dataSaida == null){
			return 0f;
		}
		
		
		if (dataEntrada.equals(dataSaida)){
			if (ultimaEntrada.getId() > ultimaSaida.getId()){
				return NumeroUtil.somarDinheiro(ultimaEntrada.getSaldoUltimo(), ultimaEntrada.getValor(), 6) ;
			}
			else {
				return NumeroUtil.diminuirDinheiro(ultimaSaida.getSaldoUltimo(), ultimaSaida.getValor(), 3);
			}
				
		}
			
		//caso todos os dois vierem com valores deve retornar o maior
		
		if (dataEntrada.after(dataSaida)){
			return NumeroUtil.somarDinheiro(ultimaEntrada.getSaldoUltimo(), ultimaEntrada.getValor(), 6) ;
		}
		else {
			return NumeroUtil.diminuirDinheiro(ultimaSaida.getSaldoUltimo(), ultimaSaida.getValor(), 3);
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

	public Set<Movimentacao> getListaMovimentacao() {
		return listaMovimentacao;
	}

	public void setListaMovimentacao(Set<Movimentacao> listaMovimentacao) {
		this.listaMovimentacao = listaMovimentacao;
	}

	public List<Movimentacao> getMovimentacaoEntrada() {
		return movimentacaoEntrada;
	}

	public void setMovimentacaoEntrada(List<Movimentacao> movimentacaoEntrada) {
		this.movimentacaoEntrada = movimentacaoEntrada;
	}

	public List<Movimentacao> getMovimentacaoSaida() {
		return movimentacaoSaida;
	}

	public void setMovimentacaoSaida(List<Movimentacao> movimentacaoSaida) {
		this.movimentacaoSaida = movimentacaoSaida;
	}


//	public Float getSaldoEstoque() {
//		return saldoEstoque;
//	}
//
//
//	public void setSaldoEstoque(Float saldoEstoque) {
//		this.saldoEstoque = saldoEstoque;
//	}


	public String getNomeSemAcento() {
		return nomeSemAcento;
	}


	public void setNomeSemAcento(String nomeSemAcento) {
		this.nomeSemAcento = nomeSemAcento;
	}


	public Float getSaldoEstoque() {
		return saldoEstoque;
	}


	public void setSaldoEstoque(Float saldoEstoque) {
		this.saldoEstoque = saldoEstoque;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
