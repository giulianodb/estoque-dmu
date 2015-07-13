package org.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.entity.Campanha;
import org.entity.LoteMovimentacao;
import org.entity.Movimentacao;
import org.entity.Produto;
import org.exception.ApplicationException;
import org.util.DateUtil;

@Stateless
public class LoteService {
	
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	@Inject
	private ProdutoService produtoService;
	
	@EJB
	private LoteService loteService;
	
	public List<LoteMovimentacao> pesquisarLote(LoteMovimentacao lote, Integer primeiroRegistro, Integer tamanhoPagina) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT lote FROM LoteMovimentacao lote ");	
			sb.append(" LEFT JOIN FETCH lote.instituicao instituicao  ");
			
			TypedQuery<LoteMovimentacao> query = em.createQuery(sb.toString(),LoteMovimentacao.class);
		
			sb.append("ORDER BY lote.data ");
			
			//Delimita o num de registro para a pagina a ser recuperada
	        if (primeiroRegistro != null){
	        	query.setFirstResult(primeiroRegistro);
	        }
	        if (tamanhoPagina != null){
	        	query.setMaxResults(tamanhoPagina);				
	        }
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	public List<LoteMovimentacao> pesquisarLoteMovimentacao(Produto produto, Date dataInicial, Date dataFinal, int primeiroRegistro, int tamanhoPagina) throws ApplicationException{
		try {
			
			dataInicial = DateUtil.adicionarHoraInicio(dataInicial);
			dataFinal = DateUtil.adicionarHoraFim(dataFinal);
			StringBuilder sb = new StringBuilder("SELECT lote FROM LoteMovimentacao lote ");	
			
			TypedQuery<LoteMovimentacao> query = em.createQuery(sb.toString(),LoteMovimentacao.class);
			
			
			sb.append("ORDER BY lote.data ");
			
			//Delimita o num de registro para a pagina a ser recuperada
//	        query.setFirstResult(primeiroRegistro);
//	        query.setMaxResults(tamanhoPagina);				
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	
	public Integer obterQtdeLote(LoteMovimentacao loteMovimentacao) throws ApplicationException{
			
			try {
				StringBuilder sb = new StringBuilder("SELECT COUNT(lote) FROM LoteMovimentacao lote ");
				Query query = em.createQuery(sb.toString());
				
				Long x = (Long) query.getSingleResult();		
				return Integer.valueOf(x.intValue());
				
			} catch(Exception e) {
				throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.obterQtdPorAluno.ERRO", e);
			}	
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
	
	/**
	 * Campanha só saida mesmo
	 * @param campanha
	 * @param dataInicial
	 * @param dataFinal
	 * @return
	 * @throws ApplicationException
	 */
	public List<LoteMovimentacao> pesquisarLotePorCampanha(Campanha campanha, Date dataInicial, Date dataFinal) throws ApplicationException{
		try {
			
			dataInicial = DateUtil.adicionarHoraInicio(dataInicial);
			dataFinal = DateUtil.adicionarHoraFim(dataFinal);
			
			StringBuilder sb = new StringBuilder("SELECT lote FROM LoteMovimentacao lote ");	
			sb.append(" LEFT JOIN FETCH lote.campanha campanha  ");
			
			if (campanha != null && campanha.getId() != null && campanha.getId() != 0){
				sb.append(" WHERE campanha.id = :id AND ");
			} else {
				sb.append(" WHERE campanha.id <> NULL  AND ");
			}
			
			
			if(dataInicial != null){
				sb.append(" lote.data >= :dataInicial AND ");
			}
			
			if(dataFinal != null){
				sb.append(" lote.data <= :dataFinal AND ");
			}
			
			sb.append(" 1 = 1 ");
			
			TypedQuery<LoteMovimentacao> query = em.createQuery(sb.toString(),LoteMovimentacao.class);
			
			if (campanha != null && campanha.getId() != null && campanha.getId() != 0){
				query.setParameter("id",campanha.getId());
			}
			if(dataInicial != null){
				query.setParameter("dataInicial",dataInicial);
			}
			
			if(dataFinal != null){
				query.setParameter("dataFinal",dataFinal);
			}
			
//			
//			sb.append("ORDER BY lote.nome ");
			
			//Delimita o num de registro para a pagina a ser recuperada
//	        query.setFirstResult(primeiroRegistro);
//	        query.setMaxResults(tamanhoPagina);				
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	

	public void incluirLote(LoteMovimentacao lote){
		
		//alterando valores do produto
		
		em.persist(lote);
	}
	
	public void excluirLote(LoteMovimentacao lote){
		
		 Query query = em.createQuery("DELETE FROM LoteMovimentacao lote WHERE lote.codigo = :id");
			  int deletedCount = query.setParameter("id", lote.getCodigo()).executeUpdate();
	}
	
	
	public LoteMovimentacao obterLoteMovimentacaoByChave(Integer id) throws ApplicationException{
		return em.find(LoteMovimentacao.class, id);
	}

}