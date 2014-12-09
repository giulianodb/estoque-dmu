package org.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.entity.Entrada;
import org.entity.Produto;
import org.exception.ApplicationException;
import org.util.NumeroUtil;

@Stateless
public class EntradaService {
	
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	@Inject
	private ProdutoService produtoService;
	
	public List<Entrada> pesquisarEntrada(Entrada entrada, int primeiroRegistro, int tamanhoPagina) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT entrada FROM Entrada entrada ");	
			sb.append(" LEFT JOIN FETCH entrada.campanha campanha  ");
			sb.append(" LEFT JOIN FETCH entrada.instituicao instituicao  ");
			sb.append(" LEFT JOIN FETCH entrada.familia familia  ");
			
			TypedQuery<Entrada> query = em.createQuery(sb.toString(),Entrada.class);
		
			sb.append("ORDER BY entrada.nome ");
			
			//Delimita o num de registro para a pagina a ser recuperada
	        query.setFirstResult(primeiroRegistro);
	        query.setMaxResults(tamanhoPagina);				
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
	
	
	public void incluirEntrada(Entrada entrada){
		
		//alterando valores do produto
		
		Produto produto = produtoService.obterProduto(entrada.getProduto().getId());
		
		//atualizando valores para historico e média 
		produto.setQuantidadeEstoque(NumeroUtil.somarDinheiro(produto.getQuantidadeEstoque(), entrada.getQuantidade(), 3));
		produto.setQuantidadeHistoricaTotal(NumeroUtil.somarDinheiro(produto.getQuantidadeHistoricaTotal(), entrada.getQuantidade(), 3));
		produto.setValorHistoricoTotal(NumeroUtil.somarDinheiro(produto.getValorHistoricoTotal(), entrada.getValor(), 3));
		
		produtoService.alterarProduto(produto);
		
		em.persist(entrada);
	}


}