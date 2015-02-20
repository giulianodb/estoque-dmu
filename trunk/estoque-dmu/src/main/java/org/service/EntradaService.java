package org.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.entity.Entrada;
import org.entity.LoteEntrada;
import org.entity.Produto;
import org.entity.TipoParceiroEnum;
import org.exception.ApplicationException;
import org.util.DateUtil;
import org.util.NumeroUtil;

@Stateless
public class EntradaService {
	
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	@Inject
	private ProdutoService produtoService;
	
	@EJB
	private LoteEntradaService loteEntradaService;
	
	public List<Entrada> pesquisarEntrada(Entrada entrada, Integer primeiroRegistro, Integer tamanhoPagina) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT entrada FROM Entrada entrada ");	
			sb.append(" LEFT JOIN FETCH entrada.loteEntrada loteEntrada ");
			sb.append(" LEFT JOIN FETCH loteEntrada.campanha campanha  ");
			sb.append(" LEFT JOIN FETCH loteEntrada.instituicao instituicao  ");
			
			TypedQuery<Entrada> query = em.createQuery(sb.toString(),Entrada.class);
		
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
	
	
	public List<Entrada> pesquisarEntrada(Produto produto, Date dataInicial, Date dataFinal, int primeiroRegistro, int tamanhoPagina) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT entrada FROM Entrada entrada ");	
			sb.append(" LEFT JOIN FETCH entrada.produto produto ");
			sb.append(" LEFT JOIN FETCH entrada.loteEntrada loteEntrada ");
			sb.append(" LEFT JOIN FETCH loteEntrada.campanha campanha  ");
			sb.append(" LEFT JOIN FETCH loteEntrada.instituicao instituicao  ");
			
			if (produto.getId() != null){
				sb.append(" WHERE produto.id = :id ");
			}
			
			TypedQuery<Entrada> query = em.createQuery(sb.toString(),Entrada.class);
			
			if (produto.getId() != null){
				query.setParameter("id",produto.getId());
			}
			
			
			sb.append("ORDER BY entrada.nome ");
			
			//Delimita o num de registro para a pagina a ser recuperada
//	        query.setFirstResult(primeiroRegistro);
//	        query.setMaxResults(tamanhoPagina);				
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	public Integer obterQtdeEntrada(Entrada entrada) throws ApplicationException{
			
			try {
				StringBuilder sb = new StringBuilder("SELECT COUNT(entrada) FROM Entrada entrada ");
				Query query = em.createQuery(sb.toString());
				
				Long x = (Long) query.getSingleResult();		
				return Integer.valueOf(x.intValue());
				
			} catch(Exception e) {
				throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.obterQtdPorAluno.ERRO", e);
			}	
		}
	
	
	public void incluirEntrada(Entrada entrada) throws ApplicationException{
		
		//alterando valores do produto
		
		//Definindo o código do numero de entrada que será como um numero do recibo
		String numeroEntrada = DateUtil.dataToString(entrada.getLoteEntrada().getData(), "yymmdd");
		if (entrada.getLoteEntrada().getCampanha() != null && entrada.getLoteEntrada().getCampanha().getId() != null){
			numeroEntrada = numeroEntrada + entrada.getLoteEntrada().getCampanha().getId() + TipoParceiroEnum.CAMPANHA.ordinal();
		
		} else if (entrada.getLoteEntrada().getDoador() != null && entrada.getLoteEntrada().getDoador().getId() != null){
			numeroEntrada = numeroEntrada + entrada.getLoteEntrada().getDoador().getId() + TipoParceiroEnum.PESSOA.ordinal();
		
		} else if (entrada.getLoteEntrada().getInstituicao() != null && entrada.getLoteEntrada().getInstituicao().getId() != null){
			numeroEntrada = numeroEntrada + entrada.getLoteEntrada().getInstituicao().getId() + TipoParceiroEnum.INSTITUICAO.ordinal();
		}
		else {
			//Senao é um anonimo
			numeroEntrada = numeroEntrada + TipoParceiroEnum.ANONIMO.ordinal();
		}
		
		LoteEntrada loteEntrada = loteEntradaService.obterLoteEntradaById(numeroEntrada);
		
		if (loteEntrada == null){
			loteEntrada = entrada.getLoteEntrada();
			loteEntrada.setNumeroEntrada(numeroEntrada);
			
			loteEntradaService.incluirLoteEntrada(loteEntrada);
		}
		else {
			entrada.setLoteEntrada(loteEntrada);
		}
		
		//Precisa agrupar todas as entradas da mesma pessoa e da mesma data em um único "pacote" ou "lote" para gerar um relatório.
		
		//criamos um atributo ou uma classe que agrupe essas informacoes? Classe lote id baseado em data - codigo doador - codigo tipo doador e essa classe poderia conter as informacoes comuns
		
		
		Produto produto = produtoService.obterProduto(entrada.getProduto().getId());
		
		entrada.setValorMediaUltimo(produto.valorMedioProduto());
		entrada.setQuantidadeUltimo(produto.getQuantidadeEstoque());
		
		//atualizando valores para historico e média 
		produto.setQuantidadeEstoque(NumeroUtil.somarDinheiro(produto.getQuantidadeEstoque(), entrada.getQuantidade(), 3));
		produto.setQuantidadeHistoricaTotal(NumeroUtil.somarDinheiro(produto.getQuantidadeHistoricaTotal(), entrada.getQuantidade(), 3));
		produto.setValorHistoricoTotal(NumeroUtil.somarDinheiro(produto.getValorHistoricoTotal(), entrada.getValor(), 3));
		
		produtoService.alterarProduto(produto);
		
		em.persist(entrada);
	}


}