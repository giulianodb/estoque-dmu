package org.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.entity.Produto;
import org.entity.Status;
import org.exception.ApplicationException;
import org.util.DateUtil;
import org.util.StringUtil;

@Stateless
public class ProdutoService {
	
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	public List<Produto> pesquisarProduto(Produto produto, Integer primeiroRegistro, Integer tamanhoPagina) throws ApplicationException{
		try {
			
			StringBuilder sb = new StringBuilder("SELECT produto FROM Produto produto ");		
			
			if (produto != null && produto.getNomeSemAcento() != null){
				produto.setNomeSemAcento(StringUtil.trata(produto.getNomeSemAcento()));
				sb.append(" WHERE (produto.nomeSemAcento) like :nome ");
			}
			sb.append("ORDER BY produto.nome ");
			
			TypedQuery<Produto> query = em.createQuery(sb.toString(),Produto.class);
			
			
			if (produto != null && produto.getNomeSemAcento() != null){
				query.setParameter( "nome","%"+produto.getNomeSemAcento()+"%");
			}
			
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
	
	
	public List<Produto> pesquisarProdutoComMovimentacoes(Produto produto, Date dataInicio, Date dataFim) throws ApplicationException{
		try {
			
			dataInicio = DateUtil.adicionarHoraInicio(dataInicio,0);
			dataFim = DateUtil.adicionarHoraFim(dataFim,59);
			
			StringBuilder sb = new StringBuilder("SELECT distinct p FROM Produto p ");		
			sb.append(" LEFT JOIN FETCH p.listaMovimentacao movimentacao ");
			
			
			sb.append(" WHERE ");
			
			if (produto !=null && produto.getId() != null){
				sb.append(" p.id = :id AND ");
			}
//			
			
			if (dataInicio != null) {
				sb.append(" (movimentacao.data >= :dataInicial AND movimentacao.data <= :dataFim) AND ");
			}
			
			sb.append(" 1 = 1 ");
			sb.append("ORDER BY p.nome  ");
			TypedQuery<Produto> query = em.createQuery(sb.toString(),Produto.class);
			
			if (produto !=null && produto.getId() != null){
				query.setParameter( "id",produto.getId());
			}
			if (dataInicio != null) {
				query.setParameter( "dataInicial", dataInicio);
			}
			
			if (dataFim != null) {
//				sb.append(" saida.loteSaida.data => :dataFim AND ");
				query.setParameter("dataFim", dataFim);
			}
			
			
			
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	
	public Integer obterQtdeProduto(Produto produto) throws ApplicationException{
		
			try {
				StringBuilder sb = new StringBuilder("SELECT COUNT(produto) FROM Produto produto ");
				
				if (produto != null && produto.getNomeSemAcento() != null){
					produto.setNomeSemAcento(StringUtil.trata(produto.getNomeSemAcento()));
					sb.append(" WHERE (produto.nomeSemAcento) like :nome ");
				}
				
				Query query = em.createQuery(sb.toString());
				
				if (produto != null && produto.getNomeSemAcento() != null){
					query.setParameter( "nome","%"+produto.getNomeSemAcento()+"%");
				}
				
				
				Long x = (Long) query.getSingleResult();		
				return Integer.valueOf(x.intValue());
				
			} catch(Exception e) {
				throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.obterQtdPorAluno.ERRO", e);
			}	
		}
	
	
	public void incluirProduto(Produto produto){
		produto.setStatus(Status.ATIVO);
		produto.setQuantidadeEstoque(0F);
		produto.setQuantidadeHistoricaTotal(0f);
//		produto.setSaldoEstoque(0f);
		produto.setValorHistoricoTotal(0f);
		produto.setNomeSemAcento(StringUtil.trata(produto.getNome()));
		
		em.persist(produto);
	}
	
	public void alterarProduto(Produto produto){
		
		produto.setNomeSemAcento(StringUtil.trata(produto.getNome()));
		em.merge(produto);
	}
	
	
	public Produto obterProduto(Integer id){
		
		return em.find(Produto.class, id);
	}
	
	/**
	 * Obtem a lista de produtos em formato especifico para importar no sistema de contabilidade
	 * @return
	 */
	public ByteArrayOutputStream produtosTXT() {
		List<Produto> produtos = new ArrayList<Produto>();
		StringBuilder sb = new StringBuilder();
		
		try {
			produtos = this.pesquisarProduto(null, null, null);
			
			for (Produto produto : produtos) {
				sb.append("\"");
				sb.append(produto.getId());
				sb.append("\"");
				sb.append(",");
				
				sb.append("\"");
				sb.append(produto.getNome().trim());
				sb.append("\"");
				sb.append(",");
				
				
				String descricao = "";
				if (produto.getDescricao() == null || produto.getDescricao().trim().equals("")) {
					descricao = produto.getNome().trim();
				}
				else {
					descricao = produto.getDescricao().trim();
				}
				
				sb.append("\"");
				sb.append(descricao);
				sb.append("\"");
				sb.append(",");
				
				sb.append(",");
				
				sb.append("\"");
				sb.append(produto.getTipoMedida().getAbreviatura());
				sb.append("\"");
				sb.append(",");
				
				sb.append(",");
				
				sb.append("\"");
				sb.append("\"");
				sb.append(",");
				
				sb.append(",");
				
				sb.append("\"");
				sb.append("N");
				sb.append("\"");
				sb.append(",");
				
				sb.append(",");
				
				sb.append(",");
				
				sb.append("\"");
				sb.append("\"");
				sb.append(",");
				
				sb.append("\"");
				sb.append("\"");
				sb.append(",");
				
				sb.append("99");
				sb.append(",");
				
				sb.append(",");
				
				sb.append(",");
				
				sb.append(",");
				
				sb.append(",");
				
				sb.append(",");
				
				sb.append(",");
				
				sb.append("\"");
				sb.append("N");
				sb.append("\"");
				sb.append(",");
				
				sb.append("\"");
				sb.append("N");
				sb.append("\"");
				sb.append(",");
				
				sb.append("\"");
				sb.append("N");
				sb.append("\"");
				sb.append(",");
				
				sb.append("\"");
				sb.append("\"");
				sb.append(",");
				
				sb.append("\"");
				sb.append("\"");
				sb.append(",");
				
				sb.append("\"");
				sb.append("\"");
				sb.append(",");
				
				sb.append("\"");
				sb.append("\"");
				sb.append(",");
				
				sb.append("\"");
				sb.append("N");
				sb.append("\"");
				sb.append(",");
				
				sb.append(",");
				
				sb.append(",");
				
				sb.append(",");
				
				sb.append(",");
				
				sb.append(",");
				
				sb.append(",");
				
				sb.append(",");
				
				sb.append(",");
				
				sb.append("\"");
				sb.append("\"");
				sb.append(",");
				
				sb.append("\"");
				sb.append("\"");
				sb.append(",");
				
				sb.append(",");
				
				sb.append(",");
				
				sb.append("\n");
				
			}
			
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			try {
				byteArrayOutputStream.write(sb.toString().getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
			return byteArrayOutputStream;
			
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	

}