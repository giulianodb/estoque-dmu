package org.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.entity.Familia;
import org.entity.Instituicao;
import org.exception.ApplicationException;

@Stateless
public class InstituicaoService {
	
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	public List<Instituicao> pesquisarInstituicao(Instituicao instituicao, int primeiroRegistro, int tamanhoPagina) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT instituicao FROM Instituicao instituicao ");		
			TypedQuery<Instituicao> query = em.createQuery(sb.toString(),Instituicao.class);
		
			sb.append("ORDER BY instituicao.nome ");
			
			//Delimita o num de registro para a pagina a ser recuperada
	        query.setFirstResult(primeiroRegistro);
	        query.setMaxResults(tamanhoPagina);				
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	public List<Instituicao> pesquisarInstituicao(Instituicao instituicao) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT instituicao FROM Instituicao instituicao ");		
			TypedQuery<Instituicao> query = em.createQuery(sb.toString(),Instituicao.class);
		
			sb.append("ORDER BY instituicao.nome ");
			
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	public Integer obterQtdeInstituicao(Instituicao instituicao) throws ApplicationException{
			
			try {
				StringBuilder sb = new StringBuilder("SELECT COUNT(instituicao) FROM Instituicao instituicao ");
				Query query = em.createQuery(sb.toString());
				
				Long x = (Long) query.getSingleResult();		
				return Integer.valueOf(x.intValue());
				
			} catch(Exception e) {
				throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.obterQtdPorAluno.ERRO", e);
			}	
		}
	
	
	public void incluirInstituicao(Instituicao instituicao){
		
		em.persist(instituicao);
	}
	
	public Instituicao obterInstituicao(Integer cod) throws ApplicationException{
		
		try {
			Instituicao f = em.find(Instituicao.class, cod);
			
			return f;
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.obterQtdPorAluno.ERRO", e);
		}	
	}


}