package org.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.dto.EntradaDTO;
import org.dto.EstoqueAnaliticoDTO;
import org.dto.EstoqueEspecificoDTO;
import org.dto.EstoqueSinteticoDTO;
import org.dto.ReciboEntradaDTO;
import org.dto.RelatorioCampanhaDTO;
import org.dto.RelatorioEntradaProdutoDTO;
import org.entity.Campanha;
import org.entity.Familia;
import org.entity.Instituicao;
import org.entity.LoteMovimentacao;
import org.entity.Movimentacao;
import org.entity.Produto;
import org.entity.TipoMovimentacaoEnum;
import org.exception.ApplicationException;
import org.util.DateUtil;
import org.util.NumeroUtil;
import org.util.PropertiesLoaderImpl;

import converter.MoedaConverter;


@Stateless
public class RelatorioService {
	
	@EJB
	private ProdutoService produtoService;
	
	@EJB
	private MovimentacaoService movimentacaoService;
	
	@EJB
	private CampanhaService campanhaService;
	
	@EJB
	private LoteService loteService;
	
	@EJB
	private InstituicaoService instituicaoService;
	
	@EJB
	private FamiliaService familiaService;
	
	
	public ByteArrayOutputStream relatorioEntrada(Date dataInicio, Date dataFim, Produto produto) throws ApplicationException{
		
		List<Movimentacao> listaMovimentacao = movimentacaoService.pesquisarMovimentacao(produto, null,null, 1, 50);
		
		RelatorioEntradaProdutoDTO jasper = new RelatorioEntradaProdutoDTO();
		jasper.setNomeProduto("Carrinho");
		
		List<EntradaDTO> lista = new ArrayList<EntradaDTO>();
		
		for (Movimentacao movimentacao : listaMovimentacao) {
			EntradaDTO movimentacao1 = new EntradaDTO();
			movimentacao1.setData(movimentacao.getData());
			movimentacao1.setDescricaoNota(movimentacao.getDescricaoNota());
			movimentacao1.setNomeDoador(movimentacao.getLoteMovimentacao().getInstituicao().getNome());
			movimentacao1.setQuantidade(movimentacao.getQuantidade());
			movimentacao1.setNotaFiscal(movimentacao.getNumeroNF());
			movimentacao1.setNomeProduto(movimentacao.getProduto().getNome());
			
			lista.add(movimentacao1);
			
		}
		
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(lista);
		
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("produto", jasper.getNomeProduto());
		mapa.put("zabumba", beanColDataSource);
		
		
		jasper.setEntradas(lista);
		
		JasperReport report;
		try {
			report = JasperCompileManager
					.compileReport(PropertiesLoaderImpl.getValor("entrada_produto"));
			// // preenchimento do relatorio, note que o metodo
			// recebe 3 parametros: // 1 - o relatorio // // 2 - um
			// Map, com parametros que sao passados ao relatorio //
			// no momento do preenchimento. No nosso caso eh null,
			// pois nao // estamos usando nenhum parametro // // 3 -
			// o data source. Note que nao devemos passar a lista
			// diretamente, // e sim "transformar" em um data source
			// utilizando a classe //
			// JRBeanCollectionDataSource
			JasperPrint print = JasperFillManager.fillReport(report, mapa,
					new JRBeanCollectionDataSource(jasper.getEntradas())); // exportacao do
			// relatorio para outro formato, no caso PDF]]

			// JasperExportManager.exportReportToPdfFile(print,
			// "/home/giuliano/RelatorioClientes.pdf");
			System.out.println("Relatório gerado.");

			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byteArrayOutputStream.write(JasperExportManager
					.exportReportToPdf(print));

			return byteArrayOutputStream;

		
		
		}
		catch (Exception e){
			return null;
		}
	}
	
	
	
	public ByteArrayOutputStream relatorioEstoqueSintetico(Date dataInicio, Date dataFim) throws ApplicationException{
		 
		List<Produto> listaProdutos = produtoService.pesquisarProdutoComMovimentacoes(null, dataInicio, dataFim);
		
		//
		List<Produto> todosProdutos = produtoService.pesquisarProduto(null, null, null);
		List<Produto> produtosTratamentoDiferente = new ArrayList<Produto>();
		
		
		for (Produto produto : todosProdutos) {
			if(!listaProdutos.contains(produto)){
				
				Movimentacao movimentacaoFake = new Movimentacao();
				Movimentacao ultimaMov = movimentacaoService.pesquisarUltimaMovimentacao(produto, dataInicio);
					if (ultimaMov != null){
						
						System.out.println("===============================");
						System.out.println("Codigo produto: " + produto.getId());
						System.out.println("Codigo ultimov: " + ultimaMov.getId());
						System.out.println("===============================");
						movimentacaoFake.setValor(0f);
						movimentacaoFake.setTipoMovimentacaoEnum(TipoMovimentacaoEnum.ENTRADA);
						movimentacaoFake.setQuantidade(0f);
						
						if (ultimaMov.getTipoMovimentacaoEnum().equals(TipoMovimentacaoEnum.ENTRADA)){
							movimentacaoFake.setQuantidadeUltimo(NumeroUtil.somarDinheiro(ultimaMov.getQuantidadeUltimo(), ultimaMov.getQuantidade(), 6));
							movimentacaoFake.setSaldoUltimo(NumeroUtil.somarDinheiro(ultimaMov.getSaldoUltimo(), ultimaMov.getValor(), 6));
						}
						else {
							movimentacaoFake.setQuantidadeUltimo(NumeroUtil.diminuirDinheiro(ultimaMov.getQuantidadeUltimo(), ultimaMov.getQuantidade(), 6));
							movimentacaoFake.setSaldoUltimo(NumeroUtil.diminuirDinheiro(ultimaMov.getSaldoUltimo(), ultimaMov.getValor(), 6));
						}
						
						
						List<Movimentacao> listaTemp = new ArrayList<Movimentacao>();
						listaTemp.add(movimentacaoFake);
						produto.setMovimentacaoEntrada(listaTemp);
						produto.setMovimentacaoSaida(new ArrayList<Movimentacao>());
						
						produto.setListaMovimentacao(new HashSet<Movimentacao>());
						produtosTratamentoDiferente.add(produto);
					}
					
			}
		}
		listaProdutos.addAll(produtosTratamentoDiferente);
		
		List<EstoqueSinteticoDTO> estoqueSintetico = new ArrayList<EstoqueSinteticoDTO>();
		
		//variaveis para o somatorio final
		Float quantidadeEntrada = 0f;
		Float quantidadeSaida = 0f;
		Float valorEntrada = 0f;
		Float valorSaida = 0f;
		
		Float quantidadeFinal = 0f;
		Float valorFinal = 0f;
		
		Float quantidadeAnterior = 0f;
		Float valorAnterior = 0f;
		
		
		for (Produto produto : listaProdutos) {
			EstoqueSinteticoDTO dto = new EstoqueSinteticoDTO();
			if (produto.getId().equals(18)){
				System.out.println("ATENCAO");
			}
			Float quantidadeSaldoAnterior = produto.quantidadeAnterior();
			Float valorTotalAnterior = produto.valorTotalAnterior();
			
			quantidadeAnterior = NumeroUtil.somarDinheiro(quantidadeAnterior, quantidadeSaldoAnterior, 6);
			valorAnterior = NumeroUtil.somarDinheiro(valorAnterior, valorTotalAnterior, 6);
			
			Float quantidadeSaldoAtual = produto.quantidadeUltimo();
			Float valorTotalAtual = produto.valorTotalUltimoUltimo();
			Float valorMedioAtual = 0f;
			
			if (quantidadeSaldoAtual > 0){
				valorMedioAtual = NumeroUtil.DividirDinheiro(valorTotalAtual, quantidadeSaldoAtual, 4);
			} 
			
			Float quantidadeTotalEntrada = 0f;
			Float quantidadeTotalSaida = 0f;
			Float valorTotalEntrada = 0f;
			Float valorTotalSaida = 0f;
			
			dto.setIdProduto(produto.getId());
			dto.setNomeProduto(produto.getNome());
			dto.setLocal("Núcleo");
			dto.setUnidadeMedida(produto.getTipoMedida().name());
			
			for (Movimentacao entrada : produto.getMovimentacaoEntrada()) {
				quantidadeTotalEntrada = NumeroUtil.somarDinheiro(quantidadeTotalEntrada, entrada.getQuantidade(), 8);
				valorTotalEntrada = NumeroUtil.somarDinheiro(valorTotalEntrada, entrada.getValor(), 8);
			}
			
			for (Movimentacao saida : produto.getMovimentacaoSaida()) {
				quantidadeTotalSaida = NumeroUtil.somarDinheiro(quantidadeTotalSaida, saida.getQuantidade(), 8);
				valorTotalSaida = NumeroUtil.somarDinheiro(valorTotalSaida, (NumeroUtil.multiplicarDinheiro(saida.getValorMediaUltimo(), saida.getQuantidade(), 8)), 8);
			}
			MoedaConverter mc = new MoedaConverter();
			
			dto.setQuantidadeSaldoAnterior(mc.getAsString(null, null, quantidadeSaldoAnterior));
			dto.setValorSaldoAnterior( mc.getAsString(null, null, valorTotalAnterior) .toString());
			
			dto.setQuantidadeEntrada(mc.getAsString(null, null, quantidadeTotalEntrada) .toString());
			dto.setValorEntrada(mc.getAsString(null, null, valorTotalEntrada) .toString());
			
			dto.setQuantidadeSaida(mc.getAsString(null, null, quantidadeTotalSaida));
			dto.setValorSaida(mc.getAsString(null, null, valorTotalSaida));
			
			dto.setQuantidadeSaldoAtual(mc.getAsString(null, null, quantidadeSaldoAtual) );
			if (valorTotalAtual < 0){
				valorTotalAtual = 0f;
			}
			
			dto.setValorSaldoAtual(mc.getAsString(null, null, valorTotalAtual) );
			dto.setValorMedioAtual(mc.getAsString(null, null, valorMedioAtual));
			
			dto.setUnidadeMedida(produto.getTipoMedida().getAbreviatura());
			
			quantidadeEntrada = NumeroUtil.somarDinheiro(quantidadeEntrada, quantidadeTotalEntrada, 8);
			quantidadeSaida = NumeroUtil.somarDinheiro(quantidadeSaida,quantidadeTotalSaida,3);
			valorEntrada = NumeroUtil.somarDinheiro(valorEntrada,valorTotalEntrada,3);
			valorSaida = NumeroUtil.somarDinheiro(valorSaida,valorTotalSaida,3);
			
			
			estoqueSintetico.add(dto);
			
		}
		
		
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(estoqueSintetico);
		
		MoedaConverter mc = new MoedaConverter();
		
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("produto", "TIRAR ESSE NOME DO PRODUTO AQUI...");
		//ESSE Atributo está indo para a tabela...
		mapa.put("zabumba", beanColDataSource);
		
		mapa.put("dataEmissao", DateUtil.dataToString(new Date()));
		mapa.put("horarioEmissao", DateUtil.getHoraAtual());
		mapa.put("data", DateUtil.dataToString(dataInicio) + " - " + DateUtil.dataToString(dataFim));
		
		quantidadeFinal = NumeroUtil.diminuirDinheiro(quantidadeEntrada, quantidadeSaida, 3);
		valorFinal = NumeroUtil.diminuirDinheiro(valorEntrada, valorSaida, 3);
				
		mapa.put("quantidadeEntrada", mc.getAsString(null, null, quantidadeEntrada));
		mapa.put("quantidadeSaida", mc.getAsString(null, null, quantidadeSaida));
		mapa.put("valorEntrada", mc.getAsString(null, null, valorEntrada));
		mapa.put("valorSaida", mc.getAsString(null, null,valorSaida ));
		
		quantidadeFinal = NumeroUtil.somarDinheiro(quantidadeAnterior, quantidadeFinal, 6);
		valorFinal = NumeroUtil.somarDinheiro(valorAnterior, valorFinal, 6);
		
		mapa.put("quantidadeFinal", mc.getAsString(null, null, quantidadeFinal));
		mapa.put("valorFinal", mc.getAsString(null, null, valorFinal));
		
		mapa.put("quantidadeAnterior", mc.getAsString(null, null, quantidadeAnterior));
		mapa.put("valorAnterior", mc.getAsString(null, null, valorAnterior));
		
		
		JasperReport report;
		try {
			report = JasperCompileManager
					.compileReport(PropertiesLoaderImpl.getValor("sintetico"));
			// // preenchimento do relatorio, note que o metodo
			// recebe 3 parametros: // 1 - o relatorio // // 2 - um
			// Map, com parametros que sao passados ao relatorio //
			// no momento do preenchimento. No nosso caso eh null,
			// pois nao // estamos usando nenhum parametro // // 3 -
			// o data source. Note que nao devemos passar a lista
			// diretamente, // e sim "transformar" em um data source
			// utilizando a classe //
			// JRBeanCollectionDataSource
			JasperPrint print = JasperFillManager.fillReport(report, mapa,
					new JRBeanCollectionDataSource(estoqueSintetico)); // exportacao do
			// relatorio para outro formato, no caso PDF]]

			// JasperExportManager.exportReportToPdfFile(print,
			// "/home/giuliano/RelatorioClientes.pdf");
			System.out.println("Relatório gerado.");

			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byteArrayOutputStream.write(JasperExportManager
					.exportReportToPdf(print));

			return byteArrayOutputStream;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	public ByteArrayOutputStream relatorioEstoqueAnalitico(Date dataInicio, Date dataFim, Produto produtoRelatorio) throws ApplicationException{
		
		List<Produto> listaProdutos = produtoService.pesquisarProdutoComMovimentacoes(produtoRelatorio, dataInicio, dataFim);
		produtoRelatorio = listaProdutos.get(0);
		
		MoedaConverter mc = new MoedaConverter();
		List<EstoqueAnaliticoDTO> estoqueAnalitico = new ArrayList<EstoqueAnaliticoDTO>();
		
		
		Float totalQuantidadeEntradas = 0f;
		Float totalQuantidadeSaidas = 0f;
		Float totalQuantidadeSaldoAtual  = 0f;
		Float totalSaldoEntradas = 0f;
//		Float totalSaldoSaidas = 0f;
		Float totalSaldoAtual  = 0f;
		
		for (Movimentacao movimentacao : produtoRelatorio.getListaMovimentacao()) {
			Float quantidadeAtual = movimentacao.getQuantidade();
			Float valorAtual = movimentacao.getValor();
			EstoqueAnaliticoDTO estoqueAnaliticoDTO = new EstoqueAnaliticoDTO();
			
			if (movimentacao.getTipoMovimentacaoEnum().equals(TipoMovimentacaoEnum.SAIDA)){
				quantidadeAtual = NumeroUtil.multiplicarDinheiro(movimentacao.getQuantidade(), -1f, 3);
				valorAtual = NumeroUtil.multiplicarDinheiro(valorAtual, -1f,3);
				
				estoqueAnaliticoDTO.setDocumentoSaida(movimentacao.getLoteMovimentacao().getCodigo().toString());
				estoqueAnaliticoDTO.setValorTotalSaida(mc.getAsString(null,null,movimentacao.getValor()));
				estoqueAnaliticoDTO.setQuantidadeSaida(mc.getAsString(null,null,movimentacao.getQuantidade()));
				estoqueAnaliticoDTO.setValorMedioSaida(mc.getAsString(null,null,NumeroUtil.DividirDinheiro(valorAtual, quantidadeAtual, 3)));
				
				totalQuantidadeSaidas = NumeroUtil.somarDinheiro(totalQuantidadeSaidas, movimentacao.getQuantidade(), 3);
//				totalSaldoSaidas = NumeroUtil.somarDinheiro(totalSaldoSaidas,movimentacao.getValor() , 3);
				
			}
			else {
				estoqueAnaliticoDTO.setDocumentoEntrada(movimentacao.getLoteMovimentacao().getCodigo().toString());
				estoqueAnaliticoDTO.setValorTotalEntrada(mc.getAsString(null,null,valorAtual));
				estoqueAnaliticoDTO.setQuantidadeEntrada(mc.getAsString(null,null,quantidadeAtual));
				estoqueAnaliticoDTO.setValorMedioEntrada(mc.getAsString(null,null,NumeroUtil.DividirDinheiro(valorAtual, quantidadeAtual, 3)));
				
				totalQuantidadeEntradas = NumeroUtil.somarDinheiro(totalQuantidadeEntradas, movimentacao.getQuantidade(), 3);
				totalSaldoEntradas = NumeroUtil.somarDinheiro(totalSaldoEntradas,movimentacao.getValor() , 3);
				
			}
			
			totalSaldoAtual = NumeroUtil.somarDinheiro(totalSaldoAtual, valorAtual, 3);
			totalQuantidadeSaldoAtual = NumeroUtil.somarDinheiro(totalQuantidadeSaldoAtual, quantidadeAtual, 3);
		
			estoqueAnaliticoDTO.setData(DateUtil.dataToString(movimentacao.getData()));
			Float quantidadeSaldo = NumeroUtil.somarDinheiro(movimentacao.getQuantidadeUltimo(), quantidadeAtual, 3);
			Float valorTotalSaldo = NumeroUtil.somarDinheiro(movimentacao.getSaldoUltimo(), valorAtual, 3);
			
			estoqueAnaliticoDTO.setQuantidadeSaldo(mc.getAsString(null,null,quantidadeSaldo));
			estoqueAnaliticoDTO.setValorTotalSaldo(mc.getAsString(null,null,valorTotalSaldo));
			if (quantidadeSaldo > 0){
				estoqueAnaliticoDTO.setValorMedioSaldo(mc.getAsString(null, null, NumeroUtil.DividirDinheiro(valorTotalSaldo, quantidadeSaldo, 3)));
			}
			else {
				estoqueAnaliticoDTO.setValorMedioSaldo("0,00");
			}
			
			estoqueAnalitico.add(estoqueAnaliticoDTO);
		}
		
		
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(estoqueAnalitico);
		
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("produto", produtoRelatorio.getNome());
		mapa.put("idproduto", produtoRelatorio.getId());
		mapa.put("medidaproduto", produtoRelatorio.getTipoMedida().getAbreviatura());
		
		mapa.put("totalQuantidadeEntradas", mc.getAsString(null,null,totalQuantidadeEntradas));
		mapa.put("totalQuantidadeSaidas", mc.getAsString(null,null,totalQuantidadeSaidas));
		mapa.put("totalQuantidadeSaldoAtual", mc.getAsString(null,null,totalQuantidadeSaldoAtual));
		mapa.put("totalSaldoEntradas", mc.getAsString(null,null,totalSaldoEntradas));
		
		//SÓ LEVA EM CONTA AS ENTRADAS PARA MÉDIA.
		mapa.put("totalSaldoSaidas", mc.getAsString(null,null,NumeroUtil.multiplicarDinheiro(totalQuantidadeSaidas, NumeroUtil.DividirDinheiro(totalSaldoEntradas , totalQuantidadeEntradas, 10), 10)));
		mapa.put("totalSaldoAtual", mc.getAsString(null,null,NumeroUtil.multiplicarDinheiro(totalQuantidadeSaldoAtual, NumeroUtil.DividirDinheiro(totalSaldoEntradas , totalQuantidadeEntradas, 10), 10)));
		
//		mapa.put("totalSaldoSaidas", mc.getAsString(null,null,totalSaldoSaidas));
//		mapa.put("totalSaldoAtual", mc.getAsString(null,null,totalSaldoAtual));
		
		mapa.put("dataFinalPeriodo", DateUtil.dataToString(dataFim));
		
		//ESSE Atributo está indo para a tabela...
		mapa.put("zabumba", beanColDataSource);
		
		mapa.put("dataEmissao", DateUtil.dataToString(new Date()));
		mapa.put("horarioEmissao", DateUtil.getHoraAtual());
		mapa.put("data", DateUtil.dataToString(dataInicio) + " - " + DateUtil.dataToString(dataFim));
		
		JasperReport report;
		try {
			report = JasperCompileManager
					.compileReport(PropertiesLoaderImpl.getValor("analitico"));
			// // preenchimento do relatorio, note que o metodo
			// recebe 3 parametros: // 1 - o relatorio // // 2 - um
			// Map, com parametros que sao passados ao relatorio //
			// no momento do preenchimento. No nosso caso eh null,
			// pois nao // estamos usando nenhum parametro // // 3 -
			// o data source. Note que nao devemos passar a lista
			// diretamente, // e sim "transformar" em um data source
			// utilizando a classe //
			// JRBeanCollectionDataSource
			JasperPrint print = JasperFillManager.fillReport(report, mapa,
					new JRBeanCollectionDataSource(estoqueAnalitico)); // exportacao do
			// relatorio para outro formato, no caso PDF]]

			// JasperExportManager.exportReportToPdfFile(print,
			// "/home/giuliano/RelatorioClientes.pdf");
			System.out.println("Relatório gerado.");

			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byteArrayOutputStream.write(JasperExportManager
					.exportReportToPdf(print));

			return byteArrayOutputStream;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	public ByteArrayOutputStream reciboEntrada (LoteMovimentacao lote) throws ApplicationException{
		
		List<ReciboEntradaDTO> listaReciboEntradaDTO = new ArrayList<ReciboEntradaDTO>();
		
		for (Movimentacao movimentacao : lote.getListMovimentacao()) {
			ReciboEntradaDTO dto = new ReciboEntradaDTO();
			
			if (movimentacao.getDescricaoNota() != null && !"".equals(movimentacao.getDescricaoNota())){
				dto.setNomeProduto(movimentacao.getDescricaoNota());
			}
			else {
				dto.setNomeProduto(movimentacao.getProduto().getNome());
			}
			
			dto.setQuantidade(movimentacao.getQuantidade());
			dto.setTipoUnidade(movimentacao.getProduto().getTipoMedida().getDescricao());
			dto.setValorTotal(movimentacao.getValor());
			dto.setValorUnitario(NumeroUtil.DividirDinheiro(movimentacao.getValor(), movimentacao.getQuantidade(), 3));
			
			listaReciboEntradaDTO.add(dto);
			
		}
		
		MoedaConverter mc = new MoedaConverter();
		
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(listaReciboEntradaDTO);
		
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("doador", lote.obterNomeDoador());
		mapa.put("endereco", "INSERIR A RUA COMPLETA...");
		mapa.put("documento", "INSERIR DOCUMENTO...");
		mapa.put("data", DateUtil.dataToString(lote.getData()));
		mapa.put("numeroRecibo", lote.getNumeroEntrada());
		
		mapa.put("valorTotal", mc.getAsString(null,null,lote.valorTotalMovimentacao()));
		
		
		//ESSE Atributo está indo para a tabela...
		mapa.put("zabumba", beanColDataSource);
		
		JasperReport report;
		try {
			report = JasperCompileManager
					.compileReport(PropertiesLoaderImpl.getValor("recibo_entrada"));
			// // preenchimento do relatorio, note que o metodo
			// recebe 3 parametros: // 1 - o relatorio // // 2 - um
			// Map, com parametros que sao passados ao relatorio //
			// no momento do preenchimento. No nosso caso eh null,
			// pois nao // estamos usando nenhum parametro // // 3 -
			// o data source. Note que nao devemos passar a lista
			// diretamente, // e sim "transformar" em um data source
			// utilizando a classe //
			// JRBeanCollectionDataSource
			JasperPrint print = JasperFillManager.fillReport(report, mapa,
					new JRBeanCollectionDataSource(listaReciboEntradaDTO)); // exportacao do
			// relatorio para outro formato, no caso PDF]]

			// JasperExportManager.exportReportToPdfFile(print,
			// "/home/giuliano/RelatorioClientes.pdf");
			System.out.println("Relatório gerado.");

			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byteArrayOutputStream.write(JasperExportManager
					.exportReportToPdf(print));

			return byteArrayOutputStream;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	

	public ByteArrayOutputStream relatorioCampanha (Date dataInicial, Date dataFinam, Campanha campanha) throws ApplicationException{
		campanha = campanhaService.obterCampanha(campanha.getId());
		List<RelatorioCampanhaDTO> listaRelatorioCampanhaDTO = new ArrayList<RelatorioCampanhaDTO>();
		
		List<LoteMovimentacao> listaLote = loteService.pesquisarLotePorCampanha(campanha, dataInicial, dataFinam);
		List<Movimentacao> listaMovimentacao = new ArrayList<Movimentacao>();
		MoedaConverter mc = new MoedaConverter();
		
		Float valorTotal = 0f;
		Float quantidadeTotal = 0f;
		
		for (LoteMovimentacao loteMovimentacao : listaLote) {
			listaMovimentacao.addAll(loteMovimentacao.getListMovimentacao());
		}
		
		for (Movimentacao movimentacao : listaMovimentacao) {
			RelatorioCampanhaDTO dto = new RelatorioCampanhaDTO();
			
			dto.setCodigoProduto(movimentacao.getProduto().getId());
			dto.setNomeProduto(movimentacao.getProduto().getNome());
			dto.setQuantidadeProduto(mc.getAsString(null,null,movimentacao.getQuantidade()));
			dto.setValorTotal(mc.getAsString(null, null, movimentacao.getValor()));
			
			dto.setQuantidadeProdutoF(movimentacao.getQuantidade());
			dto.setValorTotalF(movimentacao.getValor());
			
			//não há necessidade desse cara pois sempre será de saída.
			dto.setTipoMovimentacao(movimentacao.getTipoMovimentacaoEnum().getDescricao());
			
			valorTotal = NumeroUtil.somarDinheiro(valorTotal, movimentacao.getValor() ,3);
			quantidadeTotal = NumeroUtil.somarDinheiro(quantidadeTotal, movimentacao.getQuantidade(), 3);
			
			listaRelatorioCampanhaDTO.add(dto);
			
		}
		
		
		//uni os produtos e movimentacoes
		
		List<RelatorioCampanhaDTO> listaUnificada = new ArrayList<RelatorioCampanhaDTO>();
		Map<Integer , RelatorioCampanhaDTO> mapaTemp = new HashMap<Integer, RelatorioCampanhaDTO>();
		
		
		for (RelatorioCampanhaDTO relatorioCampanhaDTO : listaRelatorioCampanhaDTO) {
			if (mapaTemp.containsKey(relatorioCampanhaDTO.getCodigoProduto() )){
				
				RelatorioCampanhaDTO dto =	mapaTemp.get(relatorioCampanhaDTO.getCodigoProduto());
				dto.setQuantidadeProdutoF(NumeroUtil.somarDinheiro(dto.getQuantidadeProdutoF(), relatorioCampanhaDTO.getQuantidadeProdutoF(), 6));
				dto.setValorTotalF(NumeroUtil.somarDinheiro(dto.getValorTotalF(), relatorioCampanhaDTO.getValorTotalF(), 6));
				
				dto.setQuantidadeProduto(mc.getAsString(null,null,dto.getQuantidadeProdutoF()));
				dto.setValorTotal(mc.getAsString(null, null, dto.getValorTotalF()));
				
			} else {
				mapaTemp.put(relatorioCampanhaDTO.getCodigoProduto(), relatorioCampanhaDTO);
				
			}
			
		}
		listaUnificada.addAll(mapaTemp.values());
		
		
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(listaUnificada);
		
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("data", DateUtil.dataToString(dataInicial) + " - "+ DateUtil.dataToString(dataFinam));
		
		mapa.put("valorTotal", mc.getAsString(null,null,valorTotal));
		mapa.put("quantidadeTotal", mc.getAsString(null,null,quantidadeTotal));
		
		if (campanha == null || campanha.getNome() == null){
			mapa.put("campanha", "Todas campanhas");
		}
		else {
			mapa.put("campanha", campanha.getNome());
		}
		
		
		//ESSE Atributo está indo para a tabela...
		mapa.put("zabumba", beanColDataSource);
		
		JasperReport report;
		try {
			report = JasperCompileManager
					.compileReport(PropertiesLoaderImpl.getValor("relatorio_campanha"));
			// // preenchimento do relatorio, note que o metodo
			// recebe 3 parametros: // 1 - o relatorio // // 2 - um
			// Map, com parametros que sao passados ao relatorio //
			// no momento do preenchimento. No nosso caso eh null,
			// pois nao // estamos usando nenhum parametro // // 3 -
			// o data source. Note que nao devemos passar a lista
			// diretamente, // e sim "transformar" em um data source
			// utilizando a classe //
			// JRBeanCollectionDataSource
			JasperPrint print = JasperFillManager.fillReport(report, mapa,
					new JRBeanCollectionDataSource(listaRelatorioCampanhaDTO)); // exportacao do
			// relatorio para outro formato, no caso PDF]]
	
			// JasperExportManager.exportReportToPdfFile(print,
			// "/home/giuliano/RelatorioClientes.pdf");
			System.out.println("Relatório gerado.");
	
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byteArrayOutputStream.write(JasperExportManager
					.exportReportToPdf(print));
	
			return byteArrayOutputStream;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
	
	
public ByteArrayOutputStream relatorioEstoqueAnalitico(Produto produto, Date dataInicio, Date dataFim) throws ApplicationException{
		
		List<Produto> listaProdutos = produtoService.pesquisarProdutoComMovimentacoes(produto, dataInicio, dataFim);
		List<EstoqueSinteticoDTO> estoqueSintetico = new ArrayList<EstoqueSinteticoDTO>();
		
		produto = listaProdutos.get(0);
		
			EstoqueSinteticoDTO dto = new EstoqueSinteticoDTO();
			
			Float quantidadeSaldoAnterior = produto.quantidadeAnterior();
			Float valorTotalAnterior = produto.valorTotalAnterior();
			Float quantidadeSaldoAtual = produto.quantidadeUltimo();
			
			Float valorTotalAtual = produto.valorTotalUltimoUltimo();
			Float valorMedioAtual = 0f;
			
			if (quantidadeSaldoAtual > 0){
				valorMedioAtual = NumeroUtil.DividirDinheiro(valorTotalAtual, quantidadeSaldoAtual, 4);
			} 
			
			Float quantidadeTotalEntrada = 0f;
			Float quantidadeTotalSaida = 0f;
			Float valorTotalEntrada = 0f;
			Float valorTotalSaida = 0f;
			
			dto.setIdProduto(produto.getId());
			dto.setNomeProduto(produto.getNome());
			dto.setLocal("Núcleo");
			dto.setUnidadeMedida(produto.getTipoMedida().name());
			
			for (Movimentacao entrada : produto.getMovimentacaoEntrada()) {
				quantidadeTotalEntrada = NumeroUtil.somarDinheiro(quantidadeTotalEntrada, entrada.getQuantidade(), 3);
				valorTotalEntrada = NumeroUtil.somarDinheiro(valorTotalEntrada, entrada.getValor(), 3);
			}
			
			for (Movimentacao saida : produto.getMovimentacaoSaida()) {
				quantidadeTotalSaida = NumeroUtil.somarDinheiro(quantidadeTotalSaida, saida.getQuantidade(), 3);
				valorTotalSaida = NumeroUtil.somarDinheiro(valorTotalSaida, saida.getValorMediaUltimo(), 3);
			}
			
			
			dto.setQuantidadeSaldoAnterior(quantidadeSaldoAnterior.toString());
			dto.setValorSaldoAnterior(valorTotalAnterior.toString());
			
			dto.setQuantidadeEntrada(quantidadeTotalEntrada.toString());
			dto.setValorEntrada(valorTotalEntrada.toString());
			
			dto.setQuantidadeSaida(quantidadeTotalSaida.toString());
			dto.setValorSaida(valorTotalSaida.toString());
			
			dto.setQuantidadeSaldoAtual(quantidadeSaldoAtual.toString());
			dto.setValorSaldoAtual(valorTotalAtual.toString());
			dto.setValorMedioAtual(valorMedioAtual.toString());
			
			estoqueSintetico.add(dto);
			
		
		return null;
	}

	public ByteArrayOutputStream relatorioSaidaFamilia(Familia familia, Date dataInicial, Date dataFinal) throws ApplicationException{
		System.out.println("zaaaa");
		
 		List<EstoqueEspecificoDTO> lista = new ArrayList<EstoqueEspecificoDTO>();
 		
		
		List<Movimentacao> listaMovimentacaos = movimentacaoService.pesquisarMovimentacaoPorFamilia(familia, dataInicial, dataFinal,TipoMovimentacaoEnum.SAIDA);
		
		Float saldo = 0f;
		Float quantidade = 0f;
		
		MoedaConverter mc = new MoedaConverter();
		
		for (Movimentacao movimentacao : listaMovimentacaos) {
			EstoqueEspecificoDTO dto = new EstoqueEspecificoDTO();
		
			
			if (movimentacao.getLoteMovimentacao() != null && movimentacao.getLoteMovimentacao().getFamilia() != null && movimentacao.getLoteMovimentacao().getFamilia().getId() != null){
				dto.setCodigo(movimentacao.getLoteMovimentacao().getFamilia().getId().toString());
			}
			else if (movimentacao.getLoteMovimentacao() != null && movimentacao.getLoteMovimentacao().getFamiliaCampanha() != null && movimentacao.getLoteMovimentacao().getFamiliaCampanha().getId() != null){
				dto.setCodigo(movimentacao.getLoteMovimentacao().getFamiliaCampanha().getId().toString());
			}
			
			dto.setData(DateUtil.dataToString(movimentacao.getData()));
			dto.setDocumentoSaida(movimentacao.getId().toString());
			dto.setNomeProduto(movimentacao.getProduto().getNome());
			dto.setQuantidadeSaida(mc.getAsString(null, null, movimentacao.getQuantidade()));
			dto.setValorMedioSaida("");
			dto.setValorTotalSaida(mc.getAsString(null, null, movimentacao.getValor()));
			dto.setCodProduto(movimentacao.getProduto().getId());
			lista.add(dto);
			
			saldo = NumeroUtil.somarDinheiro(saldo, movimentacao.getValor(), 6);
			quantidade = NumeroUtil.somarDinheiro(quantidade, movimentacao.getQuantidade(), 6);
			
		}
		
		
		
		//uni os produtos e movimentacoes
		
		List<EstoqueEspecificoDTO> listaUnificada = new ArrayList<EstoqueEspecificoDTO>();
		Map<Integer , EstoqueEspecificoDTO> mapaTemp = new HashMap<Integer, EstoqueEspecificoDTO>();
		
		
		for (EstoqueEspecificoDTO relatorioFamiliaDTO : lista) {
			if (mapaTemp.containsKey(relatorioFamiliaDTO.getCodigo() )){
				
				EstoqueEspecificoDTO dto =	mapaTemp.get(relatorioFamiliaDTO.getCodigo());
				dto.setQuantidadeProdutoF(NumeroUtil.somarDinheiro(dto.getQuantidadeProdutoF(), relatorioFamiliaDTO.getQuantidadeProdutoF(), 6));
				dto.setValorTotalF(NumeroUtil.somarDinheiro(dto.getValorTotalF(), relatorioFamiliaDTO.getValorTotalF(), 6));
				
				dto.setQuantidadeSaida(mc.getAsString(null,null,dto.getQuantidadeProdutoF()));
				dto.setValorTotalSaida(mc.getAsString(null, null, dto.getValorTotalF()));
				
			} else {
				mapaTemp.put(relatorioFamiliaDTO.getCodProduto(), relatorioFamiliaDTO);
				
			}
			
		}
		listaUnificada.addAll(mapaTemp.values());
		
		
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(listaUnificada);
		
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("titulo", "Relatório doações para famílias");
		
		mapa.put("dataEmissao", DateUtil.dataToString(new Date()));
		mapa.put("horarioEmissao", DateUtil.getHoraAtual());
		mapa.put("data", DateUtil.dataToString(dataInicial) + " - " + DateUtil.dataToString(dataFinal));
		mapa.put("totalQuantidadeSaidas", mc.getAsString(null, null, quantidade));
		mapa.put("totalSaldoSaidas", mc.getAsString(null, null, saldo));
		
		String descricao = ""; 
		
		if (familia == null || familia.getId() == null || familia.getId().equals(0)){
			descricao = "Família: Todas";
		} else{
			familia = familiaService.obterFamilia(familia.getId());
			descricao = "Responsável Família: "+ familia.getNomeResponsavel();
		}
		
		
		mapa.put("descricao", descricao);
		//ESSE Atributo está indo para a tabela...
		mapa.put("zabumba", beanColDataSource);
		
		JasperReport report;
		try {
			report = JasperCompileManager
					.compileReport(PropertiesLoaderImpl.getValor("especifico"));
			// // preenchimento do relatorio, note que o metodo
			// recebe 3 parametros: // 1 - o relatorio // // 2 - um
			// Map, com parametros que sao passados ao relatorio //
			// no momento do preenchimento. No nosso caso eh null,
			// pois nao // estamos usando nenhum parametro // // 3 -
			// o data source. Note que nao devemos passar a lista
			// diretamente, // e sim "transformar" em um data source
			// utilizando a classe //
			// JRBeanCollectionDataSource
			JasperPrint print = JasperFillManager.fillReport(report, mapa,
					new JRBeanCollectionDataSource(lista)); // exportacao do
			// relatorio para outro formato, no caso PDF]]

			// JasperExportManager.exportReportToPdfFile(print,
			// "/home/giuliano/RelatorioClientes.pdf");
			System.out.println("Relatório gerado.");

			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byteArrayOutputStream.write(JasperExportManager
					.exportReportToPdf(print));

			return byteArrayOutputStream;
		
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
		
	
	
	}
	
	public ByteArrayOutputStream relatorioSaidaInstituicao(Instituicao instituicao, Date dataInicial, Date dataFinal) throws ApplicationException{
		System.out.println("zaaaa");
		
		List<EstoqueEspecificoDTO> lista = new ArrayList<EstoqueEspecificoDTO>();
		List<Movimentacao> listaMovimentacaos = movimentacaoService.pesquisarMovimentacaoPorInstituicao(instituicao, dataInicial, dataFinal,TipoMovimentacaoEnum.SAIDA);
		
		Float saldo = 0f;
		Float quantidade = 0f;
		
		MoedaConverter mc = new MoedaConverter();
		
		for (Movimentacao movimentacao : listaMovimentacaos) {
			EstoqueEspecificoDTO dto = new EstoqueEspecificoDTO();
		
			
			if (movimentacao.getLoteMovimentacao() != null && movimentacao.getLoteMovimentacao().getInstituicao() != null && movimentacao.getLoteMovimentacao().getInstituicao().getId() != null){
				dto.setCodigo(movimentacao.getLoteMovimentacao().getInstituicao().getId().toString());
			}
			else if (movimentacao.getLoteMovimentacao() != null && movimentacao.getLoteMovimentacao().getInstituicaoCampanha() != null && movimentacao.getLoteMovimentacao().getInstituicaoCampanha().getId() != null){
				dto.setCodigo(movimentacao.getLoteMovimentacao().getInstituicaoCampanha().getId().toString());
			}
			
//			dto.setCodigo(movimentacao.getId().toString());
			
			dto.setData(DateUtil.dataToString(movimentacao.getData()));
			dto.setDocumentoSaida(movimentacao.getId().toString());
			dto.setNomeProduto(movimentacao.getProduto().getNome());
			dto.setQuantidadeSaida(mc.getAsString(null, null, movimentacao.getQuantidade()));
			dto.setValorMedioSaida("");
			dto.setValorTotalSaida(mc.getAsString(null, null, movimentacao.getValor()));
			
			lista.add(dto);
			
			saldo = NumeroUtil.somarDinheiro(saldo, movimentacao.getValor(), 6);
			quantidade = NumeroUtil.somarDinheiro(quantidade, movimentacao.getQuantidade(), 6);
			
		}
		
		
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(lista);
		
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("titulo", "Relatório doações para instituições");
		
		mapa.put("dataEmissao", DateUtil.dataToString(new Date()));
		mapa.put("horarioEmissao", DateUtil.getHoraAtual());
		mapa.put("data", DateUtil.dataToString(dataInicial) + " - " + DateUtil.dataToString(dataFinal));
		mapa.put("totalQuantidadeSaidas", mc.getAsString(null, null, quantidade));
		mapa.put("totalSaldoSaidas", mc.getAsString(null, null, saldo));
		
		String descricao = ""; 
		
		if (instituicao == null || instituicao.getId() == null || instituicao.getId().equals(0)){
			descricao = "Instituição: Todas";
		} else{
			instituicao = instituicaoService.obterInstituicao(instituicao.getId());
			descricao = "Instituição: "+ instituicao.getNome();
		}
		
		
		mapa.put("descricao", descricao);
		
		
		//ESSE Atributo está indo para a tabela...
		mapa.put("zabumba", beanColDataSource);
		
		JasperReport report;
		try {
			report = JasperCompileManager
					.compileReport(PropertiesLoaderImpl.getValor("especifico"));
			// // preenchimento do relatorio, note que o metodo
			// recebe 3 parametros: // 1 - o relatorio // // 2 - um
			// Map, com parametros que sao passados ao relatorio //
			// no momento do preenchimento. No nosso caso eh null,
			// pois nao // estamos usando nenhum parametro // // 3 -
			// o data source. Note que nao devemos passar a lista
			// diretamente, // e sim "transformar" em um data source
			// utilizando a classe //
			// JRBeanCollectionDataSource
			JasperPrint print = JasperFillManager.fillReport(report, mapa,
					new JRBeanCollectionDataSource(lista)); // exportacao do
			// relatorio para outro formato, no caso PDF]]

			// JasperExportManager.exportReportToPdfFile(print,
			// "/home/giuliano/RelatorioClientes.pdf");
			System.out.println("Relatório gerado.");

			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byteArrayOutputStream.write(JasperExportManager
					.exportReportToPdf(print));

			return byteArrayOutputStream;
		
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
		
	
	}
	
	
	public ByteArrayOutputStream relatorioSaidaAnonimo(Date dataInicial, Date dataFinal) throws ApplicationException{
		System.out.println("zaaaa");
		
		List<EstoqueEspecificoDTO> lista = new ArrayList<EstoqueEspecificoDTO>();
		List<Movimentacao> listaMovimentacaos = movimentacaoService.pesquisarMovimentacaoPorAnonimo(dataInicial, dataFinal,TipoMovimentacaoEnum.SAIDA);
		
		Float saldo = 0f;
		Float quantidade = 0f;
		
		MoedaConverter mc = new MoedaConverter();
		
		for (Movimentacao movimentacao : listaMovimentacaos) {
			EstoqueEspecificoDTO dto = new EstoqueEspecificoDTO();
			
			dto.setCodigo(movimentacao.getId().toString());
			
			dto.setData(DateUtil.dataToString(movimentacao.getData()));
			dto.setDocumentoSaida(movimentacao.getId().toString());
			dto.setNomeProduto(movimentacao.getProduto().getNome());
			dto.setQuantidadeSaida(mc.getAsString(null, null, movimentacao.getQuantidade()));
			dto.setValorMedioSaida("");
			dto.setValorTotalSaida(mc.getAsString(null, null, movimentacao.getValor()));
			
			lista.add(dto);
			
			saldo = NumeroUtil.somarDinheiro(saldo, movimentacao.getValor(), 6);
			quantidade = NumeroUtil.somarDinheiro(quantidade, movimentacao.getQuantidade(), 6);
			
		}
		
		
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(lista);
		
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("titulo", "Relatório doações para anônimos");
		
		mapa.put("dataEmissao", DateUtil.dataToString(new Date()));
		mapa.put("horarioEmissao", DateUtil.getHoraAtual());
		mapa.put("data", DateUtil.dataToString(dataInicial) + " - " + DateUtil.dataToString(dataFinal));
		mapa.put("totalQuantidadeSaidas", mc.getAsString(null, null, quantidade));
		mapa.put("totalSaldoSaidas", mc.getAsString(null, null, saldo));
		
		String descricao = "Anônimo"; 
		
		
		mapa.put("descricao", descricao);
		//ESSE Atributo está indo para a tabela...
		mapa.put("zabumba", beanColDataSource);
		
		JasperReport report;
		try {
			report = JasperCompileManager
					.compileReport(PropertiesLoaderImpl.getValor("especifico"));
			// // preenchimento do relatorio, note que o metodo
			// recebe 3 parametros: // 1 - o relatorio // // 2 - um
			// Map, com parametros que sao passados ao relatorio //
			// no momento do preenchimento. No nosso caso eh null,
			// pois nao // estamos usando nenhum parametro // // 3 -
			// o data source. Note que nao devemos passar a lista
			// diretamente, // e sim "transformar" em um data source
			// utilizando a classe //
			// JRBeanCollectionDataSource
			JasperPrint print = JasperFillManager.fillReport(report, mapa,
					new JRBeanCollectionDataSource(lista)); // exportacao do
			// relatorio para outro formato, no caso PDF]]

			// JasperExportManager.exportReportToPdfFile(print,
			// "/home/giuliano/RelatorioClientes.pdf");
			System.out.println("Relatório gerado.");

			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byteArrayOutputStream.write(JasperExportManager
					.exportReportToPdf(print));

			return byteArrayOutputStream;
		
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
		
	
	}
}
