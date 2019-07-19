package org.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.entity.LoteMovimentacao;
import org.entity.Movimentacao;
import org.entity.Produto;
import org.exception.ApplicationException;
import org.exception.ControllerExceptionHandler;
import org.service.ArrumarService;
import org.service.LoteService;
import org.service.MovimentacaoService;
import org.service.ProdutoService;

@Named
@ViewScoped
@ControllerExceptionHandler
public class ArrumarController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4771467384987833914L;
	@EJB
	private ArrumarService arrumarService;

	@EJB
	private LoteService loteService;

	@EJB
	private MovimentacaoService movimentacaoService;

	@EJB
	private ProdutoService produtoService;

//	public void caderno() {
//
//		arrumarService.campanhaCaderno();
//	}

//	public void ajustarSaldosUltimosProduto() {
//
//		try {
//			List<Produto> lista = produtoService.pesquisarProduto(null, null, null);
//
//			for (Produto produto : lista) {
//				try {
//					arrumarService.ajustarSaldosMovimentacoes(produto.getId());
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//			}
//		} catch (ApplicationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		System.out.println("FIM");
//	}
	
	
	
	public void ajustarItens() {
		try {
			
			List<Integer> codigos = Arrays.asList(7595 , 7593 , 7584 ,7586 ,7585 , 7587 , 7588 , 7589 , 7597 , 7608 , 7600 , 7601 , 7604 , 7603 , 7602 , 7607, 7612 , 7614 , 7619 , 7615, 7621, 7618 );
			
			for (Integer integer : codigos) {
				
				LoteMovimentacao murilo = loteService.obterLoteMovimentacaoByChave(integer);
				
				for (Movimentacao mov : murilo.getListMovimentacao()) {
					if (mov.getProduto().getId().equals(76) && mov.getQuantidade().equals(0.7F)) {
					
						Movimentacao movAlterada = new Movimentacao();
						
						movAlterada.setData(mov.getData());
						movAlterada.setDescricaoNota(mov.getDescricaoNota());
						movAlterada.setId(mov.getId());
						movAlterada.setLoteMovimentacao(mov.getLoteMovimentacao());
						movAlterada.setNumeroNF(mov.getNumeroNF());
						movAlterada.setProduto(mov.getProduto());
						movAlterada.setQuantidade(0.07F);
						movAlterada.setQuantidadeUltimo(mov.getQuantidadeUltimo());
						movAlterada.setSaldoUltimo(mov.getSaldoUltimo());
						movAlterada.setTipoMovimentacaoEnum(mov.getTipoMovimentacaoEnum());
						movAlterada.setValor(mov.getValor());
						movAlterada.setValorMediaUltimo(mov.getValorMediaUltimo());
						
//						movimentacaoService.alterarMovimentacaoLote(Arrays.asList(movAlterada), null, mov);
						
						arrumarService.ajustarCremeDental(movAlterada);
						
					}
					else if (mov.getProduto().getId().equals(76)) {
						System.out.println("MOVI: " + mov.getId());
						System.out.println("Quantidade: " + mov.getQuantidade());
					}
					
				}
				
			}
			
			
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
