package org.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.dto.EntradaDTO;
import org.dto.EstoqueSinteticoDTO;
import org.dto.RelatorioEntradaProdutoDTO;
import org.entity.Movimentacao;
import org.entity.Produto;
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
		List<EstoqueSinteticoDTO> estoqueSintetico = new ArrayList<EstoqueSinteticoDTO>();
		
		for (Produto produto : listaProdutos) {
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
				valorTotalSaida = NumeroUtil.somarDinheiro(valorTotalSaida, (NumeroUtil.multiplicarDinheiro(saida.getValorMediaUltimo(), saida.getQuantidade(), 3)), 3);
			}
			MoedaConverter mc = new MoedaConverter();
			
			dto.setQuantidadeSaldoAnterior(mc.getAsString(null, null, quantidadeSaldoAnterior));
			dto.setValorSaldoAnterior( mc.getAsString(null, null, valorTotalAnterior) .toString());
			
			dto.setQuantidadeEntrada(mc.getAsString(null, null, quantidadeTotalEntrada) .toString());
			dto.setValorEntrada(mc.getAsString(null, null, valorTotalEntrada) .toString());
			
			dto.setQuantidadeSaida(mc.getAsString(null, null, quantidadeTotalSaida));
			dto.setValorSaida(mc.getAsString(null, null, valorTotalSaida));
			
			dto.setQuantidadeSaldoAtual(mc.getAsString(null, null, quantidadeSaldoAtual) );
			dto.setValorSaldoAtual(mc.getAsString(null, null, valorTotalAtual) );
			dto.setValorMedioAtual(mc.getAsString(null, null, valorMedioAtual));
			
			dto.setUnidadeMedida(produto.getTipoMedida().getAbreviatura());
			
			estoqueSintetico.add(dto);
			
		}
		
		
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(estoqueSintetico);
		
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("produto", "TIRAR ESSE NOME DO PRODUTO AQUI...");
		//ESSE Atributo está indo para a tabela...
		mapa.put("zabumba", beanColDataSource);
		
		mapa.put("dataEmissao", DateUtil.dataToString(new Date()));
		mapa.put("horarioEmissao", DateUtil.getHoraAtual());
		mapa.put("data", DateUtil.dataToString(dataInicio) + " - " + DateUtil.dataToString(dataFim));
		
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
}
