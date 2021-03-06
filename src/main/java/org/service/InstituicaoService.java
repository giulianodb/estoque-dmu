package org.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.entity.Instituicao;
import org.entity.Status;
import org.exception.ApplicationException;
import org.util.CNP;

@Stateless
public class InstituicaoService {
	
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	public List<Instituicao> pesquisarInstituicao(Instituicao instituicao, int primeiroRegistro, int tamanhoPagina) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT instituicao FROM Instituicao instituicao WHERE instituicao.status = :status ");		
			sb.append("ORDER BY instituicao.nome ");
			
			TypedQuery<Instituicao> query = em.createQuery(sb.toString(),Instituicao.class);
			query.setParameter("status", Status.ATIVO);
			
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
			StringBuilder sb = new StringBuilder("SELECT instituicao FROM Instituicao instituicao  WHERE instituicao.status = :status ");		
			sb.append("ORDER BY instituicao.nome ");
			TypedQuery<Instituicao> query = em.createQuery(sb.toString(),Instituicao.class);
			query.setParameter("status", Status.ATIVO);
			
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	public Integer obterQtdeInstituicao(Instituicao instituicao) throws ApplicationException{
			
			try {
				StringBuilder sb = new StringBuilder("SELECT COUNT(instituicao) FROM Instituicao instituicao  WHERE instituicao.status = :status ");
				Query query = em.createQuery(sb.toString());
				query.setParameter("status", Status.ATIVO);
				Long x = (Long) query.getSingleResult();		
				return Integer.valueOf(x.intValue());
				
			} catch(Exception e) {
				throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.obterQtdPorAluno.ERRO", e);
			}	
		}
	
	
	public void incluirInstituicao(Instituicao instituicao) throws ApplicationException{
		instituicao.setStatus(Status.ATIVO);
		
		if (!CNP.isValidCNPJ(instituicao.getCnpj())){
			throw new ApplicationException("controller.cpf.INVALIDO");
		}
		
		em.persist(instituicao);
	}
	
	public void alterarInstituicao(Instituicao instituicao) throws ApplicationException{
		
		
		if (!CNP.isValidCNPJ(instituicao.getCnpj())){
			throw new ApplicationException("controller.cnpj.INVALIDO");
		}
		
		em.merge(instituicao);
	}
	
	public Instituicao obterInstituicao(Integer cod) throws ApplicationException{
		
		try {
			Instituicao f = em.find(Instituicao.class, cod);
			
			return f;
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.obterQtdPorAluno.ERRO", e);
		}	
	}

	public void excluirInstituicao(Instituicao instituicao) {
		// TODO Auto-generated method stub
		
		instituicao = em.find(instituicao.getClass(), instituicao.getId());
		instituicao.setStatus(Status.INATIVO);
		
		em.merge(instituicao);
	}


}