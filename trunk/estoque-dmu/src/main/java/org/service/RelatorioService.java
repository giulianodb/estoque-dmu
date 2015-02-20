package org.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.dto.EntradaDTO;
import org.dto.EstoqueSinteticoDTO;
import org.dto.RelatorioEntradaProdutoDTO;
import org.entity.Entrada;
import org.entity.Produto;
import org.entity.Saida;
import org.exception.ApplicationException;
import org.util.NumeroUtil;
import org.util.PropertiesLoaderImpl;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


@Stateless
public class RelatorioService {
	
	@EJB
	private EntradaService entradaService;
	
	@EJB
	private ProdutoService produtoService;
	
	@EJB
	private SaidaService saidaService;
	
	public ByteArrayOutputStream relatorioEntrada(Date dataInicio, Date dataFim, Produto produto) throws ApplicationException{
		
		List<Entrada> listaEntrada = entradaService.pesquisarEntrada(produto, null,null, 1, 50);
		
		RelatorioEntradaProdutoDTO jasper = new RelatorioEntradaProdutoDTO();
		jasper.setNomeProduto("Carrinho");
		

		
		List<EntradaDTO> lista = new ArrayList<EntradaDTO>();
		
		for (Entrada entrada : listaEntrada) {
			EntradaDTO entrada1 = new EntradaDTO();
			entrada1.setData(entrada.getLoteEntrada().getData());
			entrada1.setDescricaoNota(entrada.getDescricaoNota());
			entrada1.setNomeDoador(entrada.getLoteEntrada().getInstituicao().getNome());
			entrada1.setQuantidade(entrada.getQuantidade());
			entrada1.setNotaFiscal(entrada.getNumeroNF());
			entrada1.setNomeProduto(entrada.getProduto().getNome());
			
			lista.add(entrada1);
			
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
			
			for (Entrada entrada : produto.getListaEntrada()) {
				quantidadeTotalEntrada = NumeroUtil.somarDinheiro(quantidadeTotalEntrada, entrada.getQuantidade(), 3);
				valorTotalEntrada = NumeroUtil.somarDinheiro(valorTotalEntrada, entrada.getValor(), 3);
			}
			
			for (Saida saida : produto.getListaSaida()) {
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
			
		}
		
		
		return null;
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
			
			for (Entrada entrada : produto.getListaEntrada()) {
				quantidadeTotalEntrada = NumeroUtil.somarDinheiro(quantidadeTotalEntrada, entrada.getQuantidade(), 3);
				valorTotalEntrada = NumeroUtil.somarDinheiro(valorTotalEntrada, entrada.getValor(), 3);
			}
			
			for (Saida saida : produto.getListaSaida()) {
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
