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

import org.controller.UsuarioLogadoControl;
import org.entity.Campanha;
import org.entity.LoteMovimentacao;
import org.entity.Movimentacao;
import org.entity.TipoMovimentacaoEnum;
import org.exception.ApplicationException;
import org.util.DateUtil;

@Stateless
public class LoteService {
	
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private MovimentacaoService movimentacaoService;
	
	@EJB
	private LoteService loteService;
	
	@Inject
	private UsuarioLogadoControl usuarioLogado;
	
	public List<LoteMovimentacao> pesquisarLote(LoteMovimentacao lote, Date dataInicioPesquisa, Date dataFimPesquisa, TipoMovimentacaoEnum tipoMovimentacaoPesquisa, Integer numeroReciboPesquisa, Integer primeiroRegistro, Integer tamanhoPagina) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT distinct lote FROM LoteMovimentacao lote ");	
//			if (dataFimPesquisa != null || dataInicioPesquisa != null){
//				sb.append(" LEFT JOIN FETCH lote.instituicao instituicao  ");
//			}
			if (tipoMovimentacaoPesquisa != null){
				sb.append(" JOIN lote.listMovimentacao l  ");
			} else {
				sb.append(" LEFT JOIN FETCH  lote.listMovimentacao l  ");
			}
			
			
			
			sb.append(" WHERE ");
			if (dataInicioPesquisa != null){
				sb.append(" lote.data >= :dataInicio AND ");
			}
			if (dataFimPesquisa != null){
				sb.append(" lote.data <= :dataFim AND");
			}
			if (tipoMovimentacaoPesquisa != null){
				sb.append(" l.tipoMovimentacaoEnum = :tipoMovimentacao AND ");
			}
			if (numeroReciboPesquisa != null){
				sb.append(" lote.codigo = :codigo AND ");
			}
			
			sb.append(" 1 = 1 ");
			
			sb.append("ORDER BY lote.data DESC");
			
			TypedQuery<LoteMovimentacao> query = em.createQuery(sb.toString(),LoteMovimentacao.class);
			
			if (dataInicioPesquisa != null){
				query.setParameter("dataInicio", dataInicioPesquisa);
			}
			if (dataFimPesquisa != null){
				query.setParameter("dataFim", dataFimPesquisa);
			}
			
			//Delimita o num de registro para a pagina a ser recuperada
	        if (primeiroRegistro != null){
	        	query.setFirstResult(primeiroRegistro);
	        }
	        if (tamanhoPagina != null){
	        	query.setMaxResults(tamanhoPagina);				
	        }
	        
	    	if (tipoMovimentacaoPesquisa != null){
	    		query.setParameter("tipoMovimentacao", tipoMovimentacaoPesquisa); 
			}
	    	
	    	if (numeroReciboPesquisa != null){
	    		query.setParameter("codigo", numeroReciboPesquisa); 
			}
	        
	        List<LoteMovimentacao> listaMovimentacao = query.getResultList();
//	        List<LoteMovimentacao> listaRemover = new ArrayList<LoteMovimentacao>();
//	        
//	        if (tipoMovimentacaoPesquisa != null){
//	        	for (LoteMovimentacao loteMovimentacao : listaMovimentacao) {
//	        		if (!loteMovimentacao.obterTipoMovimentacao().equals(tipoMovimentacaoPesquisa.getDescricao())) {
//	        			listaRemover.add(loteMovimentacao);
//	        		}
//	        	}
//	        	listaMovimentacao.removeAll(listaRemover);
//	        	
//	        }
	        
			return listaMovimentacao;
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	public List<LoteMovimentacao> pesquisarLoteMovimentacao() throws ApplicationException{
		try {
			
//			dataInicial = DateUtil.adicionarHoraInicio(dataInicial);
//			dataFinal = DateUtil.adicionarHoraFim(dataFinal);
			StringBuilder sb = new StringBuilder("SELECT lote FROM LoteMovimentacao lote ");	
			sb.append("ORDER BY lote.data ");
			
			TypedQuery<LoteMovimentacao> query = em.createQuery(sb.toString(),LoteMovimentacao.class);
			
			//Delimita o num de registro para a pagina a ser recuperada
//	        query.setFirstResult(primeiroRegistro);
//	        query.setMaxResults(tamanhoPagina);				
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	
	public Integer obterQtdeLote(LoteMovimentacao loteMovimentacao, Date dataInicioPesquisa, Date dataFimPesquisa, TipoMovimentacaoEnum tipoMovimentacaoPesquisa, Integer numeroReciboPesquisa) throws ApplicationException{
			
			try {
				StringBuilder sb = new StringBuilder("SELECT COUNT(DISTINCT lote) FROM LoteMovimentacao lote ");
				
//				if (dataFimPesquisa != null || dataInicioPesquisa != null){
//					sb.append(" LEFT JOIN FETCH lote.instituicao instituicao  ");
//				}
				if (tipoMovimentacaoPesquisa != null){
					sb.append(" JOIN lote.listMovimentacao l  ");
				}
				
				sb.append(" WHERE ");
				if (dataInicioPesquisa != null){
					sb.append(" lote.data >= :dataInicio AND ");
				}
				if (dataFimPesquisa != null){
					sb.append(" lote.data <= :dataFim AND");
				}
				if (tipoMovimentacaoPesquisa != null){
					sb.append(" l.tipoMovimentacaoEnum = :tipoMovimentacao AND ");
				}
				
				if (numeroReciboPesquisa != null){
					sb.append(" lote.codigo = :codigo AND ");
				}
				
				sb.append(" 1 = 1 ");
				
				
				Query query = em.createQuery(sb.toString());
				
				if (dataInicioPesquisa != null){
					query.setParameter("dataInicio", dataInicioPesquisa);
				}
				if (dataFimPesquisa != null){
					query.setParameter("dataFim", dataFimPesquisa);
				}
				
		        
		    	if (tipoMovimentacaoPesquisa != null){
		    		query.setParameter("tipoMovimentacao", tipoMovimentacaoPesquisa); 
				}
		    	
		    	if (numeroReciboPesquisa != null){
		    		query.setParameter("codigo", numeroReciboPesquisa); 
				}
				
				Long x = (Long) query.getSingleResult();		
				return Integer.valueOf(x.intValue());
				
			} catch(Exception e) {
				throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.obterQtdPorAluno.ERRO", e);
			}	
		}
	
	//@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
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
			throw new ApplicationException("ERRO", e);
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
			
			dataInicial = DateUtil.adicionarHoraInicio(dataInicial,0);
			dataFinal = DateUtil.adicionarHoraFim(dataFinal,59);
			
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
	

	public void incluirLote(LoteMovimentacao lote, TipoMovimentacaoEnum tipo){
		
		//alterando valores do produto
		if (tipo.equals(TipoMovimentacaoEnum.ENTRADA)){
			lote.setDataAcao(DateUtil.adicionarHoraInicio(new Date(),0));
		}
		else {
			lote.setDataAcao(DateUtil.adicionarHoraFim(new Date(),59));
		}
		
		lote.setUsuarioFezCadastro(usuarioLogado.getUsuario());
		lote.setTipoMovimentacaoEnum(tipo);
		
		em.persist(lote);
	}
	
	public void alterarLote(LoteMovimentacao lote){
		
		//alterando valores do produto
		
		em.merge(lote);
	}
	
	public void excluirLoteComMovimentacoes(Integer codLote) throws ApplicationException{
		
		LoteMovimentacao lote = loteService.obterLoteMovimentacaoByChave(codLote);
		
		for (Movimentacao mov : lote.getListMovimentacao()) {
			movimentacaoService.excluirMovimentacao(mov);
		}
		
		 Query query = em.createQuery("DELETE FROM LoteMovimentacao lote WHERE lote.codigo = :id");
		 int deletedCount = query.setParameter("id", lote.getCodigo()).executeUpdate();
	}
	
	
	public void excluirLote(LoteMovimentacao lote) throws ApplicationException{
		
		 Query query = em.createQuery("DELETE FROM LoteMovimentacao lote WHERE lote.codigo = :id");
		 int deletedCount = query.setParameter("id", lote.getCodigo()).executeUpdate();
	}
	
	
	
	
	public LoteMovimentacao obterLoteMovimentacaoByChave(Integer id) throws ApplicationException{
		return em.find(LoteMovimentacao.class, id);
	}

}