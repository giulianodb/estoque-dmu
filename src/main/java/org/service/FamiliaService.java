package org.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.entity.Familia;
import org.entity.Status;
import org.exception.ApplicationException;
import org.util.CNP;

@Stateless
public class FamiliaService {
	
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	public List<Familia> pesquisarFamilia(Familia familia, int primeiroRegistro, int tamanhoPagina) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT familia FROM Familia familia WHERE ");
			boolean and = false;
			
			if (familia.getNomeResponsavel() != null && !familia.getNomeResponsavel().equals("")){
				sb.append(" lower (familia.nomeResponsavel) like :nome");
				and = true;
			}
			
			if (and) {
				sb.append(" AND familia.status =:status ");
			}
			else {
				sb.append(" familia.status =:status ");
			}
			
			sb.append("ORDER BY familia.nomeResponsavel ");
			
			TypedQuery<Familia> query = em.createQuery(sb.toString(),Familia.class);
			
			if (familia.getNomeResponsavel() != null && !familia.getNomeResponsavel().equals("")){
				query.setParameter("nome", "%"+familia.getNomeResponsavel().toLowerCase()+"%");
			}
			query.setParameter("status", Status.ATIVO);
			
			
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
			StringBuilder sb = new StringBuilder("SELECT familia FROM Familia familia WHERE ");		
			
			boolean and = false;
			
			if (familia.getNomeResponsavel() != null && !familia.getNomeResponsavel().equals("")){
				sb.append(" lower (familia.nomeResponsavel) like :nome");
				and = true;
			}
			
			if (and) {
				sb.append(" AND familia.status =:status ");
			}
			else {
				sb.append(" familia.status =:status ");
			}
			
			sb.append("ORDER BY familia.nomeResponsavel ");
			
			TypedQuery<Familia> query = em.createQuery(sb.toString(),Familia.class);
			
			if (familia.getNomeResponsavel() != null && !familia.getNomeResponsavel().equals("")){
				query.setParameter("nome", "%"+familia.getNomeResponsavel().toLowerCase()+"%");
			}
			query.setParameter("status", Status.ATIVO);
			
			
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("familia.pesquisar.ERRO", e);
		}
		
	}
	
	
	public Integer obterQtdeFamilia(Familia familia) throws ApplicationException{
			
			try {
				StringBuilder sb = new StringBuilder("SELECT COUNT(familia) FROM Familia familia WHERE ");
				boolean and = false;
				if (familia.getNomeResponsavel() != null && !familia.getNomeResponsavel().equals("")){
					sb.append(" lower (familia.nomeResponsavel) like  :nome");
					and = true;
				}
				
				if (and) {
					sb.append(" AND familia.status =:status ");
				}
				else {
					sb.append(" familia.status =:status ");
				}
				
				Query query = em.createQuery(sb.toString());
				
				if (familia.getNomeResponsavel() != null && !familia.getNomeResponsavel().equals("")){
					query.setParameter("nome", "%"+familia.getNomeResponsavel().toLowerCase()+"%");
				}
				query.setParameter("status", Status.ATIVO);
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
		familia.setStatus(Status.ATIVO);
		
		if (!CNP.isValidCPF(familia.getCpfResponsavel())){
			throw new ApplicationException("controller.cpf.INVALIDO");
		}
		
		em.persist(familia);
		
	}

	public void alterarFamilia(Familia familia){
		
		em.merge(familia);
	}
	
	public void excluirFamilia(Familia familia) throws ApplicationException {

		familia = obterFamilia(familia.getId());
		familia.setStatus(Status.INATIVO);
		
		
		em.merge(familia);
	}
	
	


}