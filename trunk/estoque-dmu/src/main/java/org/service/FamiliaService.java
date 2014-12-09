package org.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.entity.Familia;
import org.exception.ApplicationException;

@Stateless
public class FamiliaService {
	
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	public List<Familia> pesquisarFamilia(Familia familia, int primeiroRegistro, int tamanhoPagina) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT familia FROM Familia familia ");		
			TypedQuery<Familia> query = em.createQuery(sb.toString(),Familia.class);
		
			sb.append("ORDER BY familia.nome ");
			
			//Delimita o num de registro para a pagina a ser recuperada
	        query.setFirstResult(primeiroRegistro);
	        query.setMaxResults(tamanhoPagina);				
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	public List<Familia> pesquisarFamilia(Familia familia) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT familia FROM Familia familia ");		
			TypedQuery<Familia> query = em.createQuery(sb.toString(),Familia.class);
		
			sb.append("ORDER BY familia.nome ");
			
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	public Integer obterQtdeFamilia(Familia familia) throws ApplicationException{
			
			try {
				StringBuilder sb = new StringBuilder("SELECT COUNT(familia) FROM Familia familia ");
				Query query = em.createQuery(sb.toString());
				
				Long x = (Long) query.getSingleResult();		
				return Integer.valueOf(x.intValue());
				
			} catch(Exception e) {
				throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.obterQtdPorAluno.ERRO", e);
			}	
		}
	
	
	public void incluirFamilia(Familia familia){
		
		em.persist(familia);
	}


}