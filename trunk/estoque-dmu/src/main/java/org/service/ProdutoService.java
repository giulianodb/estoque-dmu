package org.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.entity.Produto;
import org.exception.ApplicationException;

@Stateless
public class ProdutoService {
	
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	public List<Produto> pesquisarProduto(Produto produto, Integer primeiroRegistro, Integer tamanhoPagina) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT produto FROM Produto produto ");		
			TypedQuery<Produto> query = em.createQuery(sb.toString(),Produto.class);
		
			sb.append("ORDER BY produto.nome ");
			
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
	
	
	public List<Produto> pesquisarProdutoComMovimentacoes(Produto produto, Date dataInicio, Date dataFim) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT distinct p FROM Produto p ");		
			sb.append(" LEFT JOIN FETCH p.listaEntrada entrada ");
			sb.append(" LEFT JOIN FETCH p.listaSaida saida ");
			
			
			sb.append(" WHERE ");
			
			if (produto !=null && produto.getId() != null){
				sb.append(" p.id = :id AND ");
			}
//			
//			if (dataInicio != null) {
//				sb.append(" (entrada.loteEntrada.data >= :dataInicial AND entrada.loteEntrada.data <= :dataFim) OR ");
//			}
//			
//			if (dataInicio != null) {
////				sb.append(" saida.loteSaida.data => :dataFim AND ");
////				sb.append(" saida.data <= :dataFim AND ");
//				sb.append(" (saida.data >= :dataInicial AND saida.data <= :dataFim) AND ");
//			}
//			
			sb.append(" 1 = 1 ");
			TypedQuery<Produto> query = em.createQuery(sb.toString(),Produto.class);
			
			if (produto !=null && produto.getId() != null){
				query.setParameter( "id",produto.getId());
			}
//			if (dataInicio != null) {
//				query.setParameter( "dataInicial", dataInicio);
//			}
//			
//			if (dataInicio != null) {
////				sb.append(" saida.loteSaida.data => :dataFim AND ");
//				query.setParameter("dataFim", dataFim);
//			}
			
			sb.append("ORDER BY p.nome ,entrada.loteEntrada.data DESC , saida.data DESC ");
			
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	
	public Integer obterQtdeProduto(Produto produto) throws ApplicationException{
			
			try {
				StringBuilder sb = new StringBuilder("SELECT COUNT(produto) FROM Produto produto ");
				Query query = em.createQuery(sb.toString());
				
				Long x = (Long) query.getSingleResult();		
				return Integer.valueOf(x.intValue());
				
			} catch(Exception e) {
				throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.obterQtdPorAluno.ERRO", e);
			}	
		}
	
	
	public void incluirProduto(Produto produto){
		produto.setQuantidadeEstoque(0F);
		
		em.persist(produto);
	}
	
	public void alterarProduto(Produto produto){
		
		em.merge(produto);
	}

	
	public Produto obterProduto(Integer id){
		
		return em.find(Produto.class, id);
	}

}