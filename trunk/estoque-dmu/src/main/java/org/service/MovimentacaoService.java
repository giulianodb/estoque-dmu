package org.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.entity.LoteMovimentacao;
import org.entity.Movimentacao;
import org.entity.Produto;
import org.entity.TipoMovimentacaoEnum;
import org.entity.TipoParceiroEnum;
import org.exception.ApplicationException;
import org.util.DateUtil;
import org.util.NumeroUtil;

@Stateless
public class MovimentacaoService {
	
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	@Inject
	private ProdutoService produtoService;
	
	@EJB
	private LoteEntradaService loteEntradaService;
	
	public List<Movimentacao> pesquisarMovimentacao(Movimentacao movimentacao, Integer primeiroRegistro, Integer tamanhoPagina) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT movimentacao FROM Movimentacao movimentacao ");	
			sb.append(" LEFT JOIN FETCH movimentacao.loteMovimentacao lote ");
			sb.append(" LEFT JOIN FETCH lote.campanha campanha  ");
			sb.append(" LEFT JOIN FETCH lote.instituicao instituicao  ");
			
			TypedQuery<Movimentacao> query = em.createQuery(sb.toString(),Movimentacao.class);
		
			sb.append("ORDER BY entrada.nome ");
			
			//Delimita o num de registro para a pagina a ser recuperada
	        if (primeiroRegistro != null){
	        	query.setFirstResult(primeiroRegistro);
	        }
	        if (tamanhoPagina != null){
	        	query.setMaxResults(tamanhoPagina);				
	        }
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	public List<Movimentacao> pesquisarMovimentacao(Produto produto, Date dataInicial, Date dataFinal, int primeiroRegistro, int tamanhoPagina) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT movimentacao FROM Movimentacao movimentacao ");	
			sb.append(" LEFT JOIN FETCH movimentacao.produto produto ");
			sb.append(" LEFT JOIN FETCH movimentacao.loteMovimentacao lote ");
			sb.append(" LEFT JOIN FETCH lote.campanha campanha  ");
			sb.append(" LEFT JOIN FETCH lote.instituicao instituicao  ");
			
			if (produto.getId() != null){
				sb.append(" WHERE produto.id = :id ");
			}
			
			TypedQuery<Movimentacao> query = em.createQuery(sb.toString(),Movimentacao.class);
			
			if (produto.getId() != null){
				query.setParameter("id",produto.getId());
			}
			
			
			sb.append("ORDER BY movimentacao.nome ");
			
			//Delimita o num de registro para a pagina a ser recuperada
//	        query.setFirstResult(primeiroRegistro);
//	        query.setMaxResults(tamanhoPagina);				
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	public Integer obterQtdeMovimentacao(Movimentacao movimentacao) throws ApplicationException{
			
			try {
				StringBuilder sb = new StringBuilder("SELECT COUNT(movimentacao) FROM Movimentacao movimentacao ");
				Query query = em.createQuery(sb.toString());
				
				Long x = (Long) query.getSingleResult();		
				return Integer.valueOf(x.intValue());
				
			} catch(Exception e) {
				throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.obterQtdPorAluno.ERRO", e);
			}	
		}
	
	
	public void incluirEntrada(Movimentacao entrada) throws ApplicationException{
		entrada.setTipoMovimentacaoEnum(TipoMovimentacaoEnum.ENTRADA);
		String numeroEntrada = extrairNumeroLote(entrada);
		
		LoteMovimentacao loteMovimentacao = loteEntradaService.obterLoteMovimentacaoById(numeroEntrada);
		
		if (loteMovimentacao == null){
			loteMovimentacao = entrada.getLoteMovimentacao();
			loteMovimentacao.setNumeroEntrada(numeroEntrada);
			
			loteEntradaService.incluirLoteEntrada(loteMovimentacao);
		}
		else {
			entrada.setLoteMovimentacao(loteMovimentacao);
		}
		
		//Precisa agrupar todas as entradas da mesma pessoa e da mesma data em um único "pacote" ou "lote" para gerar um relatório.
		
		//criamos um atributo ou uma classe que agrupe essas informacoes? Classe lote id baseado em data - codigo doador - codigo tipo doador e essa classe poderia conter as informacoes comuns
		
		
		Produto produto = produtoService.obterProduto(entrada.getProduto().getId());
		
		entrada.setValorMediaUltimo(produto.valorMedioProduto());
		entrada.setQuantidadeUltimo(produto.getQuantidadeEstoque());
		entrada.setSaldoUltimo(NumeroUtil.multiplicarDinheiro(produto.valorMedioProduto(), produto.getQuantidadeEstoque(), 4));
		
		//atualizando valores para historico e média 
		produto.setQuantidadeEstoque(NumeroUtil.somarDinheiro(produto.getQuantidadeEstoque(), entrada.getQuantidade(), 3));
		produto.setQuantidadeHistoricaTotal(NumeroUtil.somarDinheiro(produto.getQuantidadeHistoricaTotal(), entrada.getQuantidade(), 3));
		produto.setValorHistoricoTotal(NumeroUtil.somarDinheiro(produto.getValorHistoricoTotal(), entrada.getValor(), 3));
		
		produtoService.alterarProduto(produto);
		
		em.persist(entrada);
	}


	private String extrairNumeroLote(Movimentacao movimentacao) {
		//alterando valores do produto
		
		//Definindo o código do numero de entrada que será como um numero do recibo
		String numeroEntrada = DateUtil.dataToString(movimentacao.getLoteMovimentacao().getData(), "yyMMdd");
		
		Calendar myCal = new GregorianCalendar();
		myCal.setTime(movimentacao.getLoteMovimentacao().getData());
		myCal.set(Calendar.HOUR, Calendar.getInstance().get(Calendar.HOUR));
		myCal.set(Calendar.MINUTE, Calendar.getInstance().get(Calendar.MINUTE));
		myCal.set(Calendar.SECOND, Calendar.getInstance().get(Calendar.SECOND));
		
		movimentacao.getLoteMovimentacao().setData(myCal.getTime());
		movimentacao.setData(myCal.getTime());
		
		if (movimentacao.getLoteMovimentacao().getCampanha() != null && movimentacao.getLoteMovimentacao().getCampanha().getId() != null){
			numeroEntrada = numeroEntrada + movimentacao.getLoteMovimentacao().getCampanha().getId() + TipoParceiroEnum.CAMPANHA.ordinal();
		
		} else if (movimentacao.getLoteMovimentacao().getDoador() != null && movimentacao.getLoteMovimentacao().getDoador().getId() != null){
			numeroEntrada = numeroEntrada + movimentacao.getLoteMovimentacao().getDoador().getId() + TipoParceiroEnum.PESSOA.ordinal();
		
		} else if (movimentacao.getLoteMovimentacao().getInstituicao() != null && movimentacao.getLoteMovimentacao().getInstituicao().getId() != null){
			numeroEntrada = numeroEntrada + movimentacao.getLoteMovimentacao().getInstituicao().getId() + TipoParceiroEnum.INSTITUICAO.ordinal();
		}
		else {
			//Senao é um anonimo
			numeroEntrada = numeroEntrada + TipoParceiroEnum.ANONIMO.ordinal();
		}
		
		//adicionar se entrada ou saida
		
		numeroEntrada = numeroEntrada + movimentacao.getTipoMovimentacaoEnum().ordinal();

		return numeroEntrada;
	}

	
	
	public void incluirSaida(Movimentacao saida) throws ApplicationException{
		saida.setTipoMovimentacaoEnum(TipoMovimentacaoEnum.SAIDA);
		//alterando valores do produto
		
		Produto produto = produtoService.obterProduto(saida.getProduto().getId());
		
		String numeroEntrada = extrairNumeroLote(saida);
		
		LoteMovimentacao loteMovimentacao = loteEntradaService.obterLoteMovimentacaoById(numeroEntrada);
		
		if (loteMovimentacao == null){
			loteMovimentacao = saida.getLoteMovimentacao();
			loteMovimentacao.setNumeroEntrada(numeroEntrada);
			
			loteEntradaService.incluirLoteEntrada(loteMovimentacao);
		}
		else {
			saida.setLoteMovimentacao(loteMovimentacao);
		}
		
		produtoService.alterarProduto(produto);
		
		Calendar myCal = new GregorianCalendar();
		myCal.setTime(saida.getData());
		myCal.set(Calendar.HOUR, Calendar.getInstance().get(Calendar.HOUR));
		myCal.set(Calendar.MINUTE, Calendar.getInstance().get(Calendar.MINUTE));
		myCal.set(Calendar.SECOND, Calendar.getInstance().get(Calendar.SECOND));
		
		saida.setData(myCal.getTime());
		
		saida.setValorMediaUltimo(produto.valorMedioProduto());
		//Definir o valor da saida. pega o valor médio e multiplica pela quantidade
		saida.setQuantidadeUltimo(produto.getQuantidadeEstoque());
		saida.setSaldoUltimo(NumeroUtil.multiplicarDinheiro(produto.valorMedioProduto(), produto.getQuantidadeEstoque(), 4));
		saida.setValor(NumeroUtil.multiplicarDinheiro(saida.getValorMediaUltimo(), saida.getQuantidade(), 3));
		
		//atualizando valores para historico e média 
		produto.setQuantidadeEstoque(NumeroUtil.diminuirDinheiro(produto.getQuantidadeEstoque(), saida.getQuantidade(), 3));
	
		
		em.merge(saida);
	}

	
	
	
	

}