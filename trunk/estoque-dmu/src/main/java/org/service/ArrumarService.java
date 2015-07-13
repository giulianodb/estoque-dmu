package org.service;

import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.entity.Movimentacao;
import org.entity.Produto;
import org.entity.TipoMovimentacaoEnum;
import org.exception.ApplicationException;

@Stateless
public class ArrumarService {
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	@EJB
	private MovimentacaoService movimentacaoService; 
	
	@EJB
	private ProdutoService produtoService;
	
	public List<Movimentacao> teste(int i) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT movimentacao FROM Movimentacao movimentacao ");	
			
			sb.append(" WHERE  movimentacao.produto.id = " +i);
			sb.append(" ORDER BY movimentacao.data,movimentacao.tipoMovimentacaoEnum,movimentacao.id ");
			
			TypedQuery<Movimentacao> query = em.createQuery(sb.toString(),Movimentacao.class);
			
			
		
			
			//Delimita o num de registro para a pagina a ser recuperada
//	        query.setFirstResult(primeiroRegistro);
//	        query.setMaxResults(tamanhoPagina);				
			List<Movimentacao> lista =  query.getResultList();
			
			for (Movimentacao movimentacao : lista) {
				movimentacaoService.excluirMovimentacao(movimentacao);
			}
			
			return lista;
			
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}


	public void teste2(List<Movimentacao> lista) throws Exception {
		for (Movimentacao movimentacao : lista) {
			movimentacao.setId(null);
			movimentacao.getLoteMovimentacao().setCodigo(null);
			if (movimentacao.getTipoMovimentacaoEnum().equals(TipoMovimentacaoEnum.ENTRADA)){
				movimentacaoService.incluirEntrada(movimentacao);
			} else if(movimentacao.getTipoMovimentacaoEnum().equals(TipoMovimentacaoEnum.SAIDA)){
				movimentacaoService.incluirSaida(movimentacao);
			}
			
//				movimentacaoService.excluirMovimentacao(movimentacao);
		}
	}
	
	
	
	//Para alterar as mvimentacoes com valor 0 (jeo lancou entradas com 0 de entrada)
	// para obter o valor médio do produto..
	public List<Movimentacao> teste3() throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT movimentacao FROM Movimentacao movimentacao ");	
			
			sb.append(" WHERE  movimentacao.valor = 0");
			
			TypedQuery<Movimentacao> query = em.createQuery(sb.toString(),Movimentacao.class);
			
			//Delimita o num de registro para a pagina a ser recuperada
//	        query.setFirstResult(primeiroRegistro);
//	        query.setMaxResults(tamanhoPagina);				
			List<Movimentacao> lista =  query.getResultList();
			
			for (Movimentacao movimentacao : lista) {
//				movimentacao.setValor(movimentacao.getProduto().valorMedioProduto());
				em.merge(movimentacao);
			}
			
			return lista;
			
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	public void zerarSaldos(Integer id){
		Produto produto = produtoService.obterProduto(id);
		if (produto != null){
			produto.setValorHistoricoTotal(0f);
			produto.setSaldoEstoque(0f);
			produto.setQuantidadeEstoque(0f);
			
			em.merge(produto);
			
		}
	}
	
	public void adicionarUmSegundoMovimentacoes(){
		try {
			StringBuilder sb = new StringBuilder("SELECT movimentacao FROM Movimentacao movimentacao ");	
			
			sb.append("ORDER BY movimentacao.data DESC, movimentacao.id DESC");
			
			TypedQuery<Movimentacao> query = em.createQuery(sb.toString(),Movimentacao.class);
			
			
			
			//Delimita o num de registro para a pagina a ser recuperada
//	        query.setFirstResult(primeiroRegistro);
//	        query.setMaxResults(tamanhoPagina);	
			
			List<Movimentacao> lista =  query.getResultList();
			
			for (int i = 0; i < lista.size(); i++) {
				Movimentacao m = lista.get(i);
				Calendar calendar = Calendar.getInstance();  
			      
			    calendar.setTime(m.getData()); //colocando o objeto Date no Calendar  
			    calendar.add(Calendar.SECOND, i); 
			    
			    System.out.println("ANTES...." + m.getData());
				m.setData(calendar.getTime());
				System.out.println("DEPOIS...." + m.getData());
				
				
				em.merge(m);
				
				
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
