package org.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.entity.Campanha;
import org.exception.ApplicationException;

@Stateless
public class CampanhaService {
	
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	public List<Campanha> pesquisarCampanha(Campanha campanha) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT campanha FROM Campanha campanha ");		
			TypedQuery<Campanha> query = em.createQuery(sb.toString(),Campanha.class);
		
			sb.append("ORDER BY campanha.nome ");
			
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	public Integer obterQtdeCampanha(Campanha campanha) throws ApplicationException{
			
			try {
				StringBuilder sb = new StringBuilder("SELECT COUNT(campanha) FROM Campanha campanha ");
				Query query = em.createQuery(sb.toString());
				
				Long x = (Long) query.getSingleResult();		
				return Integer.valueOf(x.intValue());
				
			} catch(Exception e) {
				throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.obterQtdPorAluno.ERRO", e);
			}	
		}
	
	
	public void incluirCampanha(Campanha campanha){
		
		em.persist(campanha);
	}


}