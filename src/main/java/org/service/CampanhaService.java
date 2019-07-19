package org.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.entity.Campanha;
import org.entity.Familia;
import org.entity.Status;
import org.exception.ApplicationException;

@Stateless
public class CampanhaService {
	
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	public List<Campanha> pesquisarCampanha(Campanha campanha) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT campanha FROM Campanha campanha WHERE campanha.status =:status ");		
			sb.append(" ORDER BY campanha.nome ");
			TypedQuery<Campanha> query = em.createQuery(sb.toString(),Campanha.class);
			query.setParameter("status", Status.ATIVO);
			
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	public List<Campanha> pesquisarCampanha(Campanha campanha, int primeiroRegistro, int tamanhoPagina) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT campanha FROM Campanha campanha WHERE campanha.status =:status ");	
			
			sb.append(" ORDER BY campanha.nome ");
			TypedQuery<Campanha> query = em.createQuery(sb.toString(),Campanha.class);
			query.setParameter("status", Status.ATIVO);
			
			
			
			//Delimita o num de registro para a pagina a ser recuperada
	        query.setFirstResult(primeiroRegistro);
	        query.setMaxResults(tamanhoPagina);				
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	public Integer obterQtdeCampanha(Campanha campanha) throws ApplicationException{
			
			try {
				StringBuilder sb = new StringBuilder("SELECT COUNT(campanha) FROM Campanha campanha WHERE campanha.status =:status ");
				Query query = em.createQuery(sb.toString());
				query.setParameter("status", Status.ATIVO);
				Long x = (Long) query.getSingleResult();		
				return Integer.valueOf(x.intValue());
				
			} catch(Exception e) {
				throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.obterQtdPorAluno.ERRO", e);
			}	
		}
	
	
	public void incluirCampanha(Campanha campanha){
		campanha.setStatus(Status.ATIVO);
		
		em.persist(campanha);
	}
	
	public void excluirCampanha(Campanha campanha){
		campanha = em.find(campanha.getClass(), campanha.getId());
		campanha.setStatus(Status.INATIVO);
		
		
		em.merge(campanha);
	}
	
	public Campanha obterCampanha(Integer cod) throws ApplicationException{
		
		try {
			Campanha f = em.find(Campanha.class, cod);
			
			return f;
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.obterQtdPorAluno.ERRO", e);
		}	
	}


	public void alterarCampanha(Campanha campanha){
		
		em.merge(campanha);
	}
}