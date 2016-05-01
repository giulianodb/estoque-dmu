package org.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.entity.Familia;
import org.exception.ApplicationException;
import org.util.CNP;

@Stateless
public class FamiliaService {
	
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	public List<Familia> pesquisarFamilia(Familia familia, int primeiroRegistro, int tamanhoPagina) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT familia FROM Familia familia ");		
			if (familia.getNomeResponsavel() != null && !familia.getNomeResponsavel().equals("")){
				sb.append("WHERE lower (familia.nomeResponsavel) like :nome");
			}
			
			
			TypedQuery<Familia> query = em.createQuery(sb.toString(),Familia.class);
			
			if (familia.getNomeResponsavel() != null && !familia.getNomeResponsavel().equals("")){
				query.setParameter("nome", "%"+familia.getNomeResponsavel().toLowerCase()+"%");
			}
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
			
			if (familia.getNomeResponsavel() != null && !familia.getNomeResponsavel().equals("")){
				sb.append("WHERE lower (familia.nomeResponsavel) like :nome");
			}
			
			
			TypedQuery<Familia> query = em.createQuery(sb.toString(),Familia.class);
			
			if (familia.getNomeResponsavel() != null && !familia.getNomeResponsavel().equals("")){
				query.setParameter("nome", "%"+familia.getNomeResponsavel().toLowerCase()+"%");
			}
			
			
			sb.append("ORDER BY familia.nome ");
			
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	public Integer obterQtdeFamilia(Familia familia) throws ApplicationException{
			
			try {
				StringBuilder sb = new StringBuilder("SELECT COUNT(familia) FROM Familia familia ");
				
				if (familia.getNomeResponsavel() != null && !familia.getNomeResponsavel().equals("")){
					sb.append("WHERE lower (familia.nomeResponsavel) like  :nome");
				}
				
				
				Query query = em.createQuery(sb.toString());
				
				if (familia.getNomeResponsavel() != null && !familia.getNomeResponsavel().equals("")){
					query.setParameter("nome", "%"+familia.getNomeResponsavel().toLowerCase()+"%");
				}
				
				Long x = (Long) query.getSingleResult();		
				return Integer.valueOf(x.intValue());
				
			} catch(Exception e) {
				throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.obterQtdPorAluno.ERRO", e);
			}	
		}
	
	
	public Familia obterFamilia(Integer cod) throws ApplicationException{
		
		try {
			Familia f = em.find(Familia.class, cod);
			
			return f;
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.obterQtdPorAluno.ERRO", e);
		}	
	}

	
	public void incluirFamilia(Familia familia) throws ApplicationException{
		
		if (!CNP.isValidCPF(familia.getCpfResponsavel())){
			throw new ApplicationException("controller.cpf.INVALIDO");
		}
		
		em.persist(familia);
		
	}

	public void alterarFamilia(Familia familia){
		
		em.merge(familia);
	}


}