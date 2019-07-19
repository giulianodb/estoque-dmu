package org.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.entity.Campanha;
import org.entity.LoteMovimentacao;
import org.entity.Movimentacao;
import org.entity.Produto;
import org.entity.TipoMovimentacaoEnum;
import org.exception.ApplicationException;
import org.util.DateUtil;
import org.util.NumeroUtil;

@Stateless
public class ArrumarService {
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	@EJB
	private MovimentacaoService movimentacaoService; 
	
	@EJB
	private LoteService loteService;
	
	@EJB
	private ProdutoService produtoService;

	@EJB
	private CampanhaService campanhaService;
	
	public void campanhaCaderno(){
//		
		Campanha c = new Campanha();
		c.setNome("CADERNO LIMPINHO");
		
		campanhaService.incluirCampanha(c);
//		
		
		Date inicio = DateUtil.retornarData("01/01/2017", "dd/MM/yyyy");
		Date fim = DateUtil.retornarData("31/03/2017", "dd/MM/yyyy");
		
		try {
			List<LoteMovimentacao> lista = loteService.pesquisarLote(null, inicio, fim, TipoMovimentacaoEnum.SAIDA, null, null, null);
			List<LoteMovimentacao> movComProdutos = new ArrayList<LoteMovimentacao>();
			
			for (LoteMovimentacao loteMovimentacao : lista) {
				List<Movimentacao> movimentacoes = loteMovimentacao.getListMovimentacao();
				for (Movimentacao m : movimentacoes) {
					if (   
							
							m.getProduto().getId().equals(206) ||
							m.getProduto().getId().equals(190) ||
							m.getProduto().getId().equals(126) ||
							m.getProduto().getId().equals(370) ||
							m.getProduto().getId().equals(195) || 
							m.getProduto().getId().equals(194) ||
							m.getProduto().getId().equals(278) ||
							m.getProduto().getId().equals(279) ||
							m.getProduto().getId().equals(167) ||
							m.getProduto().getId().equals(134) ||
							m.getProduto().getId().equals(259) ||
							m.getProduto().getId().equals(268) ||
							m.getProduto().getId().equals(141) ||
							m.getProduto().getId().equals(150) ||
							m.getProduto().getId().equals(12)  ||
							m.getProduto().getId().equals(119) 
							
							)
							{
						
						
						movComProdutos.add(loteMovimentacao);
						break;
						
					}
				}
			}
			
			for (LoteMovimentacao loteMovimentacao : movComProdutos) {
				LoteMovimentacao novoLote = new LoteMovimentacao();
				novoLote.setCampanha(loteMovimentacao.getCampanha());
				novoLote.setData(loteMovimentacao.getData());
				novoLote.setDataAcao(loteMovimentacao.getDataAcao());
				novoLote.setDoador(loteMovimentacao.getDoador());
				novoLote.setDoadorCampanha(loteMovimentacao.getDoadorCampanha());
				novoLote.setFamilia(loteMovimentacao.getFamilia());
				novoLote.setFamiliaCampanha(loteMovimentacao.getFamiliaCampanha());
				novoLote.setInstituicao(loteMovimentacao.getInstituicao());
				novoLote.setInstituicaoCampanha(loteMovimentacao.getInstituicaoCampanha());
				novoLote.setUsuarioFezCadastro(loteMovimentacao.getUsuarioFezCadastro());
				
				boolean cadastrado = false;
				
				for (Movimentacao mov : loteMovimentacao.getListMovimentacao()) {
					
					if (
							!mov.getProduto().getId().equals(206) &&
							!mov.getProduto().getId().equals(190) &&
							!mov.getProduto().getId().equals(126) &&
							!mov.getProduto().getId().equals(370) &&
							!mov.getProduto().getId().equals(195) && 
							!mov.getProduto().getId().equals(194) &&
							!mov.getProduto().getId().equals(278) &&
							!mov.getProduto().getId().equals(279) &&
							!mov.getProduto().getId().equals(167) &&
							!mov.getProduto().getId().equals(134) &&
							!mov.getProduto().getId().equals(259) &&
							!mov.getProduto().getId().equals(268) &&
							!mov.getProduto().getId().equals(141) &&
							!mov.getProduto().getId().equals(150) &&
							!mov.getProduto().getId().equals(12)  &&
							!mov.getProduto().getId().equals(119) )
							
						 {
						
						if (!cadastrado) {
							cadastrarLote(novoLote);
							em.persist(novoLote);
							cadastrado = true;
						}
						
						mov.setLoteMovimentacao(novoLote);
						em.merge(mov);
						
					}
				}
				
				loteMovimentacao.setCampanha(c);
				loteMovimentacao.setDoadorCampanha(loteMovimentacao.getDoador());
				loteMovimentacao.setDoador(null);
				
				loteMovimentacao.setFamiliaCampanha(loteMovimentacao.getFamilia());
				loteMovimentacao.setFamilia(null);
				
				loteMovimentacao.setInstituicaoCampanha(loteMovimentacao.getInstituicaoCampanha());
				loteMovimentacao.setInstituicao(null);
				
				em.merge(loteMovimentacao);
				em.flush();
				
			}
			
			System.out.println("TERMINANDO...");
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("teste");
		
		
		
				
	}
	public void cadastrarLote(LoteMovimentacao novoLote) {
		// TODO Auto-generated method stub
		
	}
	
	public void ajustarSaldosMovimentacoes(Integer codigoProduto) {
		Produto produto = produtoService.obterProduto(codigoProduto);
		produto.setId(codigoProduto);
		
		Date dataPesquisa = DateUtil.retornarData("01/01/2018", "dd/MM/yyyy");
		
		try {
			List<Movimentacao> listaMovimentacao = movimentacaoService.pesquisarMovimentacao(produto, dataPesquisa);
			
			Float quantidadeUltimo = 0f;
			Float valorUltimo = 0f;
			Float mediaUltimo = 0f;
			boolean primeiraVez = true;
			
			Float quantidadeHistorica = 0f;
			Float valorHistorico = 0f;
			
			for (Movimentacao movimentacao : listaMovimentacao) {
				if(movimentacao.getId().equals(11281)) {
					System.out.println("Atencao");
				}
				if (primeiraVez) {
					quantidadeUltimo = NumeroUtil.somarDinheiro(quantidadeUltimo, movimentacao.getQuantidadeUltimo(), 4);
					valorUltimo = NumeroUtil.somarDinheiro(valorUltimo, movimentacao.getSaldoUltimo(), 4);
					mediaUltimo = movimentacao.getValorMediaUltimo();
					
					
					if (movimentacao.getTipoMovimentacaoEnum().equals(TipoMovimentacaoEnum.SAIDA)) {
						quantidadeUltimo = NumeroUtil.diminuirDinheiro(quantidadeUltimo, movimentacao.getQuantidade(), 4);
						valorUltimo = NumeroUtil.diminuirDinheiro(valorUltimo, movimentacao.getValor(), 4);

					}
					else {
						quantidadeUltimo = NumeroUtil.somarDinheiro(quantidadeUltimo, movimentacao.getQuantidade(), 4);
						valorUltimo = NumeroUtil.somarDinheiro(valorUltimo, movimentacao.getValor(), 4);
						
						quantidadeHistorica = quantidadeUltimo;
						valorHistorico = valorUltimo;
						
					}
					if (quantidadeUltimo.equals(0f)) {
						mediaUltimo = 0f;
					}
					else {
						mediaUltimo = NumeroUtil.DividirDinheiro(valorUltimo,quantidadeUltimo, 4);
					}
					
					primeiraVez = false;
					
					continue;
				} 
				
				
				//AJUSTES DEVO TIRAR..
				
				
				if (movimentacao.getLoteMovimentacao().getCodigo().equals(6839)  && 
						movimentacao.getProduto().getId().equals(109) && 
						 	movimentacao.getQuantidade().equals(72f)) {
				
					movimentacao.setQuantidade(67f);
				}
				
				
				

				if (movimentacao.getLoteMovimentacao().getCodigo().equals(6348)  && 
						movimentacao.getProduto().getId().equals(413) && 
						 	movimentacao.getQuantidade().equals(10f)) {
				
					movimentacao.setQuantidade(8f);
				}
				
				
				if (movimentacao.getLoteMovimentacao().getCodigo().equals(6374)  && 
						movimentacao.getProduto().getId().equals(413) && 
						 	movimentacao.getQuantidade().equals(2f)) {
				
					movimentacao.setQuantidade(4f);
				}
				
				
				if (movimentacao.getLoteMovimentacao().getCodigo().equals(6467)  && 
						movimentacao.getProduto().getId().equals(142) && 
						 	movimentacao.getQuantidade().equals(5.15f)) {
				
					movimentacao.setQuantidade(1.9f);
				}
				
				
				
				
				
				
				if (movimentacao.getLoteMovimentacao().getCodigo().equals(5726)  && 
						movimentacao.getProduto().getId().equals(168) && 
						 	movimentacao.getQuantidade().equals(14.68f)) {
				
					movimentacao.setQuantidade(9.68f);
				}
				
				if (movimentacao.getLoteMovimentacao().getCodigo().equals(5725)  && 
						movimentacao.getProduto().getId().equals(168) && 
						 	movimentacao.getQuantidade().equals(14.68f)) {
				
					movimentacao.setQuantidade(9.68f);
				}
				
				
				if (movimentacao.getLoteMovimentacao().getCodigo().equals(5727)  && 
						movimentacao.getProduto().getId().equals(168) && 
						 	movimentacao.getQuantidade().equals(14.68f)) {
				
					movimentacao.setQuantidade(9.68f);
				}
				
				if (movimentacao.getLoteMovimentacao().getCodigo().equals(5719)  && 
						movimentacao.getProduto().getId().equals(168) && 
						 	movimentacao.getQuantidade().equals(14.68f)) {
				
					movimentacao.setQuantidade(9.68f);
				}
			
				
				if (movimentacao.getLoteMovimentacao().getCodigo().equals(5730)  && 
						movimentacao.getProduto().getId().equals(168) && 
						 	movimentacao.getQuantidade().equals(14.68f)) {
				
					movimentacao.setQuantidade(9.68f);
				}
				
				
				
				
				
				
				if (movimentacao.getLoteMovimentacao().getCodigo().equals(5737)  && 
						movimentacao.getProduto().getId().equals(168) && 
						 	movimentacao.getQuantidade().equals(58.72f)) {
				
					movimentacao.setQuantidade(66.22f);
				}
				
				
				if (movimentacao.getLoteMovimentacao().getCodigo().equals(5738)  && 
						movimentacao.getProduto().getId().equals(168) && 
						 	movimentacao.getQuantidade().equals(58.72f)) {
				
					movimentacao.setQuantidade(66.22f);
				}
				
				if (movimentacao.getLoteMovimentacao().getCodigo().equals(5739)  && 
						movimentacao.getProduto().getId().equals(168) && 
						 	movimentacao.getQuantidade().equals(58.72f)) {
				
					movimentacao.setQuantidade(66.22f);
				}
				
				if (movimentacao.getLoteMovimentacao().getCodigo().equals(5742)  && 
						movimentacao.getProduto().getId().equals(168) && 
						 	movimentacao.getQuantidade().equals(58.72f)) {
				
					movimentacao.setQuantidade(66.22f);
				}
				
				//TIRAR
				
				
				movimentacao.setQuantidadeUltimo(quantidadeUltimo);
				movimentacao.setSaldoUltimo(valorUltimo);
				movimentacao.setValorMediaUltimo(mediaUltimo);
				
				if (movimentacao.getTipoMovimentacaoEnum().equals(TipoMovimentacaoEnum.SAIDA)) {
					movimentacao.setValor(NumeroUtil.multiplicarDinheiro(movimentacao.getQuantidade(), mediaUltimo, 4));
					
					quantidadeUltimo = NumeroUtil.diminuirDinheiro(quantidadeUltimo, movimentacao.getQuantidade(), 4);
					valorUltimo = NumeroUtil.diminuirDinheiro(valorUltimo, movimentacao.getValor(), 4);

				}
				else {
					
					quantidadeUltimo = NumeroUtil.somarDinheiro(quantidadeUltimo, movimentacao.getQuantidade(), 4);
					valorUltimo = NumeroUtil.somarDinheiro(valorUltimo, movimentacao.getValor(), 4);
					
					mediaUltimo = NumeroUtil.DividirDinheiro(valorUltimo,quantidadeUltimo,  4);
					
					
					quantidadeHistorica = NumeroUtil.somarDinheiro(quantidadeHistorica, movimentacao.getQuantidade(), 4);
					valorHistorico = NumeroUtil.somarDinheiro(valorHistorico, movimentacao.getValor(), 4);

					
				}

				if (quantidadeUltimo < 0 || valorUltimo < 0 || mediaUltimo < 0) {
					System.out.println("PAU.. ficou negativo essa porra toda!!");
					System.out.println("Produto: " + movimentacao.getProduto().getId());
					System.out.println("Produto: " + movimentacao.getProduto().getNome());
					System.out.println("Id mov: " + movimentacao.getId());
					System.out.println("Id mov: " + movimentacao.getData());
					throw new java.lang.Exception();
				}
				
				em.merge(movimentacao);
				
			}
			
			produto.setSaldoEstoque(valorUltimo);
			produto.setQuantidadeEstoque(quantidadeUltimo);
			
			produto.setQuantidadeHistoricaTotal(quantidadeHistorica);
			produto.setValorHistoricoTotal(valorHistorico);
			
			em.merge(produto);
			
		} catch (java.lang.Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		
	}
	
	
	
	public void ajustarCremeDental(Movimentacao mov) {
		
		try {
			List<Movimentacao> movimentacoesPosterioes =  movimentacaoService.pesquisarMovimentacao(mov.getProduto(), mov.getData());
			
			
			
			/*
			movimentacao.setValorMediaUltimo(movimentacoesPosterioes.get(0).getValorMediaUltimo());
			movimentacao.setQuantidadeUltimo(movimentacoesPosterioes.get(0).getQuantidadeUltimo());
			movimentacao.setSaldoUltimo(movimentacoesPosterioes.get(0).getSaldoUltimo());
			*/
			
			
			Float quantidadeMovimentacao = mov.getQuantidade();
			Float valorMovimentacao = NumeroUtil.multiplicarDinheiro(mov.getQuantidade(), mov.getValorMediaUltimo(), 5);
			
			if (mov.getTipoMovimentacaoEnum().equals(TipoMovimentacaoEnum.SAIDA)){
				quantidadeMovimentacao = NumeroUtil.multiplicarDinheiro(quantidadeMovimentacao, -1f, 5);
//				mov.setValor(NumeroUtil.multiplicarDinheiro(movimentacoesPosterioes.get(0).getValorMediaUltimo(), movimentacao.getQuantidade(), 3));
				valorMovimentacao = NumeroUtil.multiplicarDinheiro(mov.getValor(), -1f, 5);
			}
			
			
			Float quantidadeUltimoProximo = NumeroUtil.somarDinheiro(mov.getQuantidadeUltimo(), mov.getQuantidade(), 4);
			Float saldoUltimoProximo = NumeroUtil.somarDinheiro(mov.getSaldoUltimo(), mov.getValor(), 3);
			Float valorMedioUltimoProximo = NumeroUtil.DividirDinheiro(saldoUltimoProximo, quantidadeUltimoProximo, 4);
			
			
			for (int i = 0; i < movimentacoesPosterioes.size(); i++) {
					
					movimentacoesPosterioes.get(i).setQuantidadeUltimo(quantidadeUltimoProximo);
					movimentacoesPosterioes.get(i).setSaldoUltimo(saldoUltimoProximo);
					
					if (movimentacoesPosterioes.get(i).getQuantidadeUltimo() > 0){
						movimentacoesPosterioes.get(i).setValorMediaUltimo(valorMedioUltimoProximo);
					}
					else{
						movimentacoesPosterioes.get(i).setValorMediaUltimo(0f);
					}
					
					quantidadeUltimoProximo = NumeroUtil.somarDinheiro(movimentacoesPosterioes.get(i).getQuantidadeUltimo(), movimentacoesPosterioes.get(i).getQuantidade(), 4);
					saldoUltimoProximo = NumeroUtil.somarDinheiro(movimentacoesPosterioes.get(i).getSaldoUltimo(), movimentacoesPosterioes.get(i).getValor(), 3);
					valorMedioUltimoProximo = NumeroUtil.DividirDinheiro(saldoUltimoProximo, quantidadeUltimoProximo, 4);
					
					if (movimentacoesPosterioes.get(i).getQuantidadeUltimo() < 0){
						//lançar exption..
						//estava comentado porém ACHO que precisa ficar descomentado.. assim que encontrar erro de saldo deve lançar erro!!!! Assim evita que no historico saldo fique negativo
//						throw new ApplicationException("service.movimentacao.saldoNegativo.ERRO",new String[]{DateUtil.dataToString(movimentacoesPosterioes.get(i).getData())});
						
					
						//estava comentado porém ACHO que precisa ficar descomentado.. assim que encontrar erro de saldo deve lançar erro!!!! Assim evita que no historico saldo fique negativo
//						throw new ApplicationException("service.movimentacao.saldoNegativo.ERRO",new String[]{DateUtil.dataToString(movimentacoesPosterioes.get(i).getData())});
//						sdsd
						
						System.out.println("===========================================================================================================================================");
						System.out.println("FICOU COM SALDO NEGATIVO........");
						System.out.println(mov.getQuantidade());
						System.out.println();
						System.out.println("produto: " + mov.getProduto().getNome() + "  " + mov.getProduto().getId());
						System.out.println("quantidade negativa: " + mov.getProduto().getQuantidadeEstoque());
						System.out.println("codigo lote: " + mov.getId());
						System.out.println("data: "+ DateUtil.dataToString(mov.getData()));
						System.out.println("===========================================================================================================================================");
						
						
						throw new ApplicationException("service.movimentacao.saldoNegativo.ERRO", new String[]{movimentacoesPosterioes.get(i).getQuantidadeUltimo().toString(),mov.getProduto().getNome(),DateUtil.dataToString(movimentacoesPosterioes.get(i).getData())});
					}
				
				em.merge(movimentacoesPosterioes.get(i));
			}
			
			Produto produto = new Produto();
			
			produto = produtoService.obterProduto(mov.getProduto().getId());
			produto.setQuantidadeEstoque(quantidadeUltimoProximo);
			produto.setSaldoEstoque(valorMedioUltimoProximo);
			
			em.merge(produto);
			em.merge(mov);
			
			
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}