package org.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.entity.LoteMovimentacao;
import org.exception.ApplicationException;

@Stateless
public class LoteEntradaService {
	
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	

	
	public void incluirLoteEntrada(LoteMovimentacao lote){
		
		//alterando valores do produto
		
		em.persist(lote);
	}
	
	
	public LoteMovimentacao obterLoteMovimentacaoById(String numeroLote) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT movimentacao FROM LoteMovimentacao movimentacao ");	
			sb.append(" WHERE movimentacao.numeroEntrada = :numero  ");
			
			TypedQuery<LoteMovimentacao> query = em.createQuery(sb.toString(),LoteMovimentacao.class);
			query.setParameter("numero", numeroLote);
		
			//Delimita o num de registro para a pagina a ser recuperada
			return query.getSingleResult();
		} 
		catch (NoResultException e){
			return null;
		}
		catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
	}
	

}