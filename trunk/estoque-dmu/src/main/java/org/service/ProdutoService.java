package org.service;

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
	
	public List<Produto> pesquisarProduto(Produto produto, int primeiroRegistro, int tamanhoPagina) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT produto FROM Produto produto ");		
			TypedQuery<Produto> query = em.createQuery(sb.toString(),Produto.class);
		
			sb.append("ORDER BY produto.nome ");
			
			//Delimita o num de registro para a pagina a ser recuperada
	        query.setFirstResult(primeiroRegistro);
	        query.setMaxResults(tamanhoPagina);				
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