package org.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.entity.Entrada;
import org.entity.LoteEntrada;
import org.exception.ApplicationException;

@Stateless
public class LoteEntradaService {
	
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	public List<Entrada> pesquisarLoteEntrada(LoteEntrada lote, int primeiroRegistro, int tamanhoPagina) throws ApplicationException{
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
	
	
	public void incluirLoteEntrada(LoteEntrada lote){
		
		//alterando valores do produto
		
		em.persist(lote);
	}
	
	
	public LoteEntrada obterLoteEntradaById(String numeroLote) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT loteEntrada FROM LoteEntrada loteEntrada ");	
			sb.append(" WHERE loteEntrada.numeroEntrada = :numero  ");
			
			TypedQuery<LoteEntrada> query = em.createQuery(sb.toString(),LoteEntrada.class);
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