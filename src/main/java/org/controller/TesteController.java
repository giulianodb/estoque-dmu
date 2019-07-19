package org.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.entity.LoteMovimentacao;
import org.entity.Movimentacao;
import org.entity.Produto;
import org.entity.TipoMedidaEnum;
import org.entity.TipoMovimentacaoEnum;
import org.entity.TipoProdutoEnum;
import org.exception.ApplicationException;
import org.exception.ControllerExceptionHandler;
import org.service.MovimentacaoService;
import org.service.ProdutoService;

@Named
@ViewScoped
@ControllerExceptionHandler
public class TesteController implements Serializable{
	
	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private MovimentacaoService movimentacaoService;
	
	public String movimentacao(){
		
		//Criar produto
		
		Produto produtoNovo = new Produto();
		
		produtoNovo.setDescricao("TESTE");
		produtoNovo.setNome("TESTE");
		produtoNovo.setTipoMedida(TipoMedidaEnum.UNIDADE);
		produtoNovo.setTipoProduto(TipoProdutoEnum.OUTROS);
		
		produtoService.incluirProduto(produtoNovo);
		
		Movimentacao dados = new Movimentacao();
		
		Movimentacao dados1 = new Movimentacao();
		Movimentacao dados2 = new Movimentacao();
		Movimentacao dados3 = new Movimentacao();
		Movimentacao dados4 = new Movimentacao();
		Movimentacao dados5 = new Movimentacao();
		Movimentacao dados6 = new Movimentacao();
		Movimentacao dados7 = new Movimentacao();
		
		
		LoteMovimentacao lote1 = new LoteMovimentacao();
		LoteMovimentacao lote2 = new LoteMovimentacao();
		LoteMovimentacao lote3 = new LoteMovimentacao();
		LoteMovimentacao lote4 = new LoteMovimentacao();
		LoteMovimentacao lote5 = new LoteMovimentacao();
		LoteMovimentacao lote6 = new LoteMovimentacao();
		LoteMovimentacao lote7 = new LoteMovimentacao();
		
		LocalDate data1 = LocalDate.of(2016,1 ,1);
		LocalDate data2 = data1.plusDays(1);
		LocalDate data3 = data1.plusDays(2);
		LocalDate data4 = data1.plusDays(3);
		LocalDate data5 = data1.plusDays(4);
		LocalDate data6 = data1.plusDays(5);
		LocalDate data7 = data1.plusDays(6);
		
		dados1.setLoteMovimentacao(lote1);
		dados2.setLoteMovimentacao(lote2);
		dados3.setLoteMovimentacao(lote3);
		dados4.setLoteMovimentacao(lote4);
		dados5.setLoteMovimentacao(lote5);
		dados6.setLoteMovimentacao(lote6);
		dados7.setLoteMovimentacao(lote7);
		
		lote1.setData(Date.from(data1.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		lote2.setData(Date.from(data2.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		lote3.setData(Date.from(data3.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		lote4.setData(Date.from(data4.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		lote5.setData(Date.from(data5.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		lote6.setData(Date.from(data6.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		lote7.setData(Date.from(data7.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		
		
		Movimentacao movimentacao1 = new Movimentacao();
		
		List<Movimentacao> lista1 = new ArrayList<Movimentacao>();
		
		movimentacao1.setProduto(produtoNovo);
		movimentacao1.setQuantidade(10F);
		movimentacao1.setValor(1F);
		movimentacao1.setTipoMovimentacaoEnum(TipoMovimentacaoEnum.ENTRADA);
		
		Movimentacao movimentacao2 = new Movimentacao();
		
		List<Movimentacao> lista2 = new ArrayList<Movimentacao>();
		
		movimentacao2.setProduto(produtoNovo);
		movimentacao2.setQuantidade(20F);
		movimentacao2.setValor(2F);
		movimentacao2.setTipoMovimentacaoEnum(TipoMovimentacaoEnum.ENTRADA);
		
		
		Movimentacao movimentacao3 = new Movimentacao();
		
		List<Movimentacao> lista3 = new ArrayList<Movimentacao>();
		
		movimentacao3.setProduto(produtoNovo);
		movimentacao3.setQuantidade(30F);
		movimentacao3.setValor(3F);
		movimentacao3.setTipoMovimentacaoEnum(TipoMovimentacaoEnum.ENTRADA);
		
		
		Movimentacao movimentacao4 = new Movimentacao();
		
		List<Movimentacao> lista4 = new ArrayList<Movimentacao>();
		
		movimentacao4.setProduto(produtoNovo);
		movimentacao4.setQuantidade(40F);
		movimentacao4.setValor(4F);
		movimentacao4.setTipoMovimentacaoEnum(TipoMovimentacaoEnum.ENTRADA);
		
		
		Movimentacao movimentacao5 = new Movimentacao();
		
		List<Movimentacao> lista5 = new ArrayList<Movimentacao>();
		
		movimentacao5.setProduto(produtoNovo);
		movimentacao5.setQuantidade(50F);
		movimentacao5.setValor(5F);
		movimentacao5.setTipoMovimentacaoEnum(TipoMovimentacaoEnum.ENTRADA);
		
		
		Movimentacao movimentacao6 = new Movimentacao();
		
		List<Movimentacao> lista6 = new ArrayList<Movimentacao>();
		
		movimentacao6.setProduto(produtoNovo);
		movimentacao6.setQuantidade(60F);
		movimentacao6.setValor(6F);
		movimentacao6.setTipoMovimentacaoEnum(TipoMovimentacaoEnum.ENTRADA);
		
		
		Movimentacao movimentacao7 = new Movimentacao();
		
		List<Movimentacao> lista7 = new ArrayList<Movimentacao>();
		
		movimentacao7.setProduto(produtoNovo);
		movimentacao7.setQuantidade(70F);
		movimentacao7.setValor(7F);
		movimentacao7.setTipoMovimentacaoEnum(TipoMovimentacaoEnum.ENTRADA);
		
		lista1.add(movimentacao1);
		lista2.add(movimentacao2);
		lista3.add(movimentacao3);
		lista4.add(movimentacao4);
		lista5.add(movimentacao5);
		lista6.add(movimentacao6);
		lista7.add(movimentacao7);
		
		try {
			movimentacaoService.incluirEntradaLote(lista1, dados1);
			movimentacaoService.incluirEntradaLote(lista2, dados2);
			movimentacaoService.incluirEntradaLote(lista3, dados3);
			movimentacaoService.incluirEntradaLote(lista4, dados4);
			movimentacaoService.incluirEntradaLote(lista5, dados5);
			movimentacaoService.incluirEntradaLote(lista6, dados6);
			movimentacaoService.incluirEntradaLote(lista7, dados7);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		return null;
	}
	
}
