package org.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.entity.Doador;
import org.entity.Familia;
import org.exception.ApplicationException;

@Stateless
public class DoadorService {
	
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	public List<Doador> pesquisarDoador(Doador doador, int primeiroRegistro, int tamanhoPagina) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT doador FROM Doador doador ");		
			TypedQuery<Doador> query = em.createQuery(sb.toString(),Doador.class);
		
			sb.append("ORDER BY doador.nome ");
			
			//Delimita o num de registro para a pagina a ser recuperada
	        query.setFirstResult(primeiroRegistro);
	        query.setMaxResults(tamanhoPagina);				
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	public List<Doador> pesquisarDoador(Doador doador) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT doador FROM Doador doador ");		
			TypedQuery<Doador> query = em.createQuery(sb.toString(),Doador.class);
		
			sb.append("ORDER BY doador.nome ");
			
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	public Integer obterQtdeDoador(Doador doador) throws ApplicationException{
			
			try {
				StringBuilder sb = new StringBuilder("SELECT COUNT(doador) FROM Doador doador ");
				Query query = em.createQuery(sb.toString());
				
				Long x = (Long) query.getSingleResult();		
				return Integer.valueOf(x.intValue());
				
			} catch(Exception e) {
				throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.obterQtdPorAluno.ERRO", e);
			}	
		}
	
	
	public void incluirDoador(Doador doador){
		
		em.persist(doador);
	}
	
	
	public Doador obterDoador(Integer cod) throws ApplicationException{
		
		try {
			Doador f = em.find(Doador.class, cod);
			
			return f;
		} catch(Exception e) {
			throw new ApplicationException("erro.obter.doador", e);
		}	
	}

	public void alterarDoador(Doador doador){
		
		em.merge(doador);
	}
}