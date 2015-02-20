package org.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.entity.Saida;
import org.entity.Produto;
import org.exception.ApplicationException;
import org.util.NumeroUtil;

@Stateless
public class SaidaService {
	
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	@Inject
	private ProdutoService produtoService;
	
	public List<Saida> pesquisarSaida(Saida saida, Integer primeiroRegistro, Integer tamanhoPagina) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT saida FROM Saida saida ");		
			TypedQuery<Saida> query = em.createQuery(sb.toString(),Saida.class);
		
			sb.append("ORDER BY saida.nome ");
			
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
	
	
	public Integer obterQtdeSaida(Saida saida) throws ApplicationException{
			
			try {
				StringBuilder sb = new StringBuilder("SELECT COUNT(saida) FROM Saida saida ");
				Query query = em.createQuery(sb.toString());
				
				Long x = (Long) query.getSingleResult();		
				return Integer.valueOf(x.intValue());
				
			} catch(Exception e) {
				throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.obterQtdPorAluno.ERRO", e);
			}	
		}
	
	
	public void incluirSaida(Saida saida){
		
		//alterando valores do produto
		
		Produto produto = produtoService.obterProduto(saida.getProduto().getId());
		
		
		produtoService.alterarProduto(produto);
		
		saida.setValorMediaUltimo(produto.valorMedioProduto());
		saida.setQuantidadeUltimo(produto.getQuantidadeEstoque());
		
		//atualizando valores para historico e média 
		produto.setQuantidadeEstoque(NumeroUtil.diminuirDinheiro(produto.getQuantidadeEstoque(), saida.getQuantidade(), 3));
	
		
		em.persist(saida);
	}


}