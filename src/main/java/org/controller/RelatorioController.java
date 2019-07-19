package org.controller;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.entity.Campanha;
import org.entity.Familia;
import org.entity.Instituicao;
import org.entity.LoteMovimentacao;
import org.entity.Produto;
import org.exception.ControllerExceptionHandler;
import org.service.CampanhaService;
import org.service.FamiliaService;
import org.service.InstituicaoService;
import org.service.ProdutoService;
import org.service.RelatorioService;

@Named
@ViewScoped
@ControllerExceptionHandler
public class RelatorioController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2964921300303274340L;
	@EJB
	private RelatorioService relatorioService;

	@EJB
	private CampanhaService campanhaService;

	@EJB
	private FamiliaService familiaService;
	
	@EJB
	private InstituicaoService instituicaoService;

	private Date dataInicialPesquisa;

	private Date dataFinalPesquisa;

	@Inject
	private Produto produto;

	@Inject
	private Campanha campanha;

	@Inject
	private Familia familia;

	@Inject
	private Instituicao instituicao;

	private List<Produto> listProdutoCombo;

	private List<Campanha> listCampanhaCombo;

	private List<Familia> listFamiliaCombo;
	
	private List<Instituicao> listInstituicaoCombo;

	@Inject
	private ProdutoService produtoService;

	public void gerarRelatorioFaturaExito(Produto produto) {
		try {

			ByteArrayOutputStream byteArrayOutputStream = relatorioService
					.relatorioEntrada(null, null, produto);

			FacesContext facesContext = FacesContext.getCurrentInstance();

			HttpServletResponse response = (HttpServletResponse) facesContext
					.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.addHeader("Content-disposition",
					"attachment; filename=\" relatorio.pdf\"");

			OutputStream os = null;
			os = response.getOutputStream();

			byteArrayOutputStream.writeTo(os);
			os.flush();
			os.close();
			byteArrayOutputStream.close();

			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void paginaRelatorioEstoqueSintetico() {
//		try {
//			return "/pages/relatorio/sintetico";
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "/pages/inicial";
//		}

	}

	public void paginaRelatorioEstoqueAnalitico() {
		try {
			listProdutoCombo = produtoService.pesquisarProduto(new Produto(),
					0, 0);
			
			String sData = "01/01/2018";
			String fData = "31/12/2018";
			SimpleDateFormat dataNascimento = new SimpleDateFormat("dd/MM/yyyy");   
			dataInicialPesquisa = dataNascimento.parse(sData); 
			
			dataFinalPesquisa = dataNascimento.parse(fData);
			
//			return "/pages/relatorio/analitico";
		} catch (Exception e) {
			e.printStackTrace();
//			return "/pages/inicial";
		}

	}

	public void paginaRelatorioCampanha() {
		try {
			listCampanhaCombo = campanhaService
					.pesquisarCampanha(new Campanha());
//			return "/pages/relatorio/campanha";
		} catch (Exception e) {
			e.printStackTrace();
//			return "/pages/inicial";
		}

	}

	public void paginaRelatorioFamilia() {
		try {

			listFamiliaCombo = familiaService.pesquisarFamilia(new Familia());
//			return "/pages/relatorio/familia";
		} catch (Exception e) {
			e.printStackTrace();
//			return "/pages/inicial";
		}

	}
	
	public void paginaRelatorioInstituicao() {
		try {

			listInstituicaoCombo = instituicaoService.pesquisarInstituicao(new Instituicao());
//			return "/pages/relatorio/instituicao";
		} catch (Exception e) {
			e.printStackTrace();
//			return "/pages/inicial";
		}

	}
	
	public void paginaRelatorioAnonimo() {
//		try {
//
//			return "/pages/relatorio/anonimo";
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "/pages/inicial";
//		}

	}

	public void relatorioEstoqueSintetico() {
		try {

			ByteArrayOutputStream byteArrayOutputStream = relatorioService
					.relatorioEstoqueSintetico(dataInicialPesquisa,
							dataFinalPesquisa,true);

			FacesContext facesContext = FacesContext.getCurrentInstance();

			HttpServletResponse response = (HttpServletResponse) facesContext
					.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.addHeader("Content-disposition",
					"attachment; filename=\" relatorio.pdf\"");

			OutputStream os = null;
			os = response.getOutputStream();

			byteArrayOutputStream.writeTo(os);
			os.flush();
			os.close();
			byteArrayOutputStream.close();

			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	public void relatorioEstoqueSinteticoTxt() {
		try {

			ByteArrayOutputStream byteArrayOutputStream = relatorioService
					.relatorioEstoqueSintetico(dataInicialPesquisa,
							dataFinalPesquisa,false);

			FacesContext facesContext = FacesContext.getCurrentInstance();

			HttpServletResponse response = (HttpServletResponse) facesContext
					.getExternalContext().getResponse();
			response.setContentType("application/text");
			response.addHeader("Content-disposition",
					"attachment; filename=\" relatorio.txt\"");

			OutputStream os = null;
			os = response.getOutputStream();

			byteArrayOutputStream.writeTo(os);
			os.flush();
			os.close();
			byteArrayOutputStream.close();

			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void relatorioEstoqueAnalitico() {
		try {

			ByteArrayOutputStream byteArrayOutputStream = relatorioService
					.relatorioEstoqueAnalitico(dataInicialPesquisa,
							dataFinalPesquisa, produto);

			FacesContext facesContext = FacesContext.getCurrentInstance();

			HttpServletResponse response = (HttpServletResponse) facesContext
					.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.addHeader("Content-disposition",
					"attachment; filename=\" relatorio.pdf\"");

			OutputStream os = null;
			os = response.getOutputStream();

			byteArrayOutputStream.writeTo(os);
			os.flush();
			os.close();
			byteArrayOutputStream.close();

			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void relatorioCampanha() {
		try {

			ByteArrayOutputStream byteArrayOutputStream = relatorioService
					.relatorioCampanha(dataInicialPesquisa, dataFinalPesquisa,
							campanha);

			FacesContext facesContext = FacesContext.getCurrentInstance();

			HttpServletResponse response = (HttpServletResponse) facesContext
					.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.addHeader("Content-disposition",
					"attachment; filename=\" relatorio.pdf\"");

			OutputStream os = null;
			os = response.getOutputStream();

			byteArrayOutputStream.writeTo(os);
			os.flush();
			os.close();
			byteArrayOutputStream.close();

			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void reciboEntrada(LoteMovimentacao lote) {
		try {

			ByteArrayOutputStream byteArrayOutputStream = relatorioService
					.reciboEntrada(lote);

			FacesContext facesContext = FacesContext.getCurrentInstance();

			HttpServletResponse response = (HttpServletResponse) facesContext
					.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.addHeader("Content-disposition",
					"attachment; filename=\" Recibo_entrada.pdf\"");

			OutputStream os = null;
			os = response.getOutputStream();

			byteArrayOutputStream.writeTo(os);
			os.flush();
			os.close();
			byteArrayOutputStream.close();

			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void reciboSaida(LoteMovimentacao lote) {
		try {

			ByteArrayOutputStream byteArrayOutputStream = relatorioService
					.reciboSaida(lote);

			FacesContext facesContext = FacesContext.getCurrentInstance();

			HttpServletResponse response = (HttpServletResponse) facesContext
					.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.addHeader("Content-disposition",
					"attachment; filename=\" Receibo_saída-"+lote.getCodigo()+".pdf\"");

			OutputStream os = null;
			os = response.getOutputStream();

			byteArrayOutputStream.writeTo(os);
			os.flush();
			os.close();
			byteArrayOutputStream.close();

			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void relatorioFamilia() {
		try {

			ByteArrayOutputStream byteArrayOutputStream = relatorioService.relatorioSaidaFamilia(familia,
					dataInicialPesquisa, dataFinalPesquisa);


			FacesContext facesContext = FacesContext.getCurrentInstance();

			HttpServletResponse response = (HttpServletResponse) facesContext
					.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.addHeader("Content-disposition",
					"attachment; filename=\" relatorio.pdf\"");

			OutputStream os = null;
			os = response.getOutputStream();

			byteArrayOutputStream.writeTo(os);
			os.flush();
			os.close();
			byteArrayOutputStream.close();
			
			 
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void relatorioIinstituicao() {
		try {

			ByteArrayOutputStream byteArrayOutputStream = relatorioService.relatorioSaidaInstituicao(instituicao,
					dataInicialPesquisa, dataFinalPesquisa);

			FacesContext facesContext = FacesContext.getCurrentInstance();

			HttpServletResponse response = (HttpServletResponse) facesContext
					.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.addHeader("Content-disposition",
					"attachment; filename=\" relatorio.pdf\"");

			OutputStream os = null;
			os = response.getOutputStream();

			byteArrayOutputStream.writeTo(os);
			os.flush();
			os.close();
			byteArrayOutputStream.close();
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}

	}
	
	public void relatorioAnonimo() {
		try {

			ByteArrayOutputStream byteArrayOutputStream = relatorioService.relatorioSaidaAnonimo(dataInicialPesquisa, dataFinalPesquisa);

			FacesContext facesContext = FacesContext.getCurrentInstance();

			HttpServletResponse response = (HttpServletResponse) facesContext
					.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.addHeader("Content-disposition",
					"attachment; filename=\" relatorio.pdf\"");

			OutputStream os = null;
			os = response.getOutputStream();

			byteArrayOutputStream.writeTo(os);
			os.flush();
			os.close();
			byteArrayOutputStream.close();
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}

	}

	public Date getDataInicialPesquisa() {
		return dataInicialPesquisa;
	}

	public void setDataInicialPesquisa(Date dataInicialPesquisa) {
		this.dataInicialPesquisa = dataInicialPesquisa;
	}

	public Date getDataFinalPesquisa() {
		return dataFinalPesquisa;
	}

	public void setDataFinalPesquisa(Date dataFinalPesquisa) {
		this.dataFinalPesquisa = dataFinalPesquisa;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<Produto> getListProdutoCombo() {
		return listProdutoCombo;
	}

	public void setListProdutoCombo(List<Produto> listProdutoCombo) {
		this.listProdutoCombo = listProdutoCombo;
	}

	public List<Campanha> getListCampanhaCombo() {
		return listCampanhaCombo;
	}

	public void setListCampanhaCombo(List<Campanha> listCampanhaCombo) {
		this.listCampanhaCombo = listCampanhaCombo;
	}

	public Campanha getCampanha() {
		return campanha;
	}

	public void setCampanha(Campanha campanha) {
		this.campanha = campanha;
	}

	public List<Familia> getListFamiliaCombo() {
		return listFamiliaCombo;
	}

	public void setListFamiliaCombo(List<Familia> listFamiliaCombo) {
		this.listFamiliaCombo = listFamiliaCombo;
	}

	public Familia getFamilia() {
		return familia;
	}

	public void setFamilia(Familia familia) {
		this.familia = familia;
	}

	public Instituicao getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(Instituicao instituicao) {
		this.instituicao = instituicao;
	}

	public List<Instituicao> getListInstituicaoCombo() {
		return listInstituicaoCombo;
	}

	public void setListInstituicaoCombo(List<Instituicao> listInstituicaoCombo) {
		this.listInstituicaoCombo = listInstituicaoCombo;
	}

}
