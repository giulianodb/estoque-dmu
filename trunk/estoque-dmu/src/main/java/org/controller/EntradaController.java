package org.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.controller.model.EntradaDataModel;
import org.entity.Campanha;
import org.entity.Doador;
import org.entity.Entrada;
import org.entity.Instituicao;
import org.entity.Produto;
import org.entity.TipoParceiroEnum;
import org.exception.ApplicationException;
import org.exception.ControllerExceptionHandler;
import org.service.CampanhaService;
import org.service.DoadorService;
import org.service.EntradaService;
import org.service.InstituicaoService;
import org.service.ProdutoService;
import org.util.Message;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class EntradaController implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntradaDataModel entradaDataModel;
	
	@Inject
	private Entrada entrada;
	
	@Inject
	private EntradaService entradaService;
	
	@Inject
	private CampanhaService campanhaService;
	
	@Inject
	private DoadorService doadorService;
	
	@Inject
	private InstituicaoService instituicaoService;
	
	@Inject
	private Provider<Entrada> entradaProvider;
	
	private List<Produto> listProdutoCombo;
	
	private List<TipoParceiroEnum> listaTipoParceiroCombo;
	
	private List<Campanha> listaCampanhaCombo;
	
	private List<Instituicao> listaInstituicaoCombo;
	
	private List<Doador> listaDoadorCombo;
	
	private TipoParceiroEnum tipoParceiroSelecionado;
	
	private Boolean mostrarComboCampanha;
	
	private Boolean mostrarComboInstituicao;
	
	private Boolean mostrarComboDoador;
	
	private Float valorMedioProduto;
	
	@Inject
	private ProdutoService produtoService;
	
	
	public String iniciarPesquisaEntrada(){
		
		return "/pages/entrada/listar_entrada";
	}
	
	public String iniciarIncluirEntrada() throws ApplicationException{
		
		entrada = entradaProvider.get();
		valorMedioProduto = 0f;
		//carregando combos iniciais
		listProdutoCombo = produtoService.pesquisarProduto(new Produto(), 0, 0);
		listaTipoParceiroCombo = new ArrayList<TipoParceiroEnum>(Arrays.asList(TipoParceiroEnum.values()));
		listaTipoParceiroCombo.remove(1);
		
		mostrarComboCampanha = false;
		mostrarComboDoador = false;
		mostrarComboInstituicao = false;
		
		entrada.setData(new Date());
		
		return "/pages/entrada/editar_entrada";
	}
	
	public String excluirEntrada(){
		
		return null;
	}
	
	public String incluirEntrada(){
		try {
			entradaService.incluirEntrada(entrada);
			Message.setMessage("SUCESSO");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return iniciarPesquisaEntrada();
	}
	
	public String alterarEntrada(){
		return "/pages/entrada/listar_entrada";
	}
	
	public void listenerAlterarTipoDoador() throws ApplicationException{
		switch (tipoParceiroSelecionado.ordinal()) {
		case 0: {
			if(listaCampanhaCombo == null || listaCampanhaCombo.size() == 0){
				listaCampanhaCombo = campanhaService.pesquisarCampanha(new Campanha());
			}
			entrada.setCampanha(new Campanha());
			entrada.setInstituicao(null);
			entrada.setDoador(null);
			
			mostrarComboCampanha = true;
			mostrarComboDoador = false;
			mostrarComboInstituicao = false;
			
			break;
		}
		case 4: {
			if(listaDoadorCombo == null || listaDoadorCombo.size() == 0){
				listaDoadorCombo = doadorService.pesquisarDoador(new Doador());
			}
			
			entrada.setDoador(new Doador());
			entrada.setInstituicao(null);
			entrada.setCampanha(null);
			
			mostrarComboCampanha = false;
			mostrarComboInstituicao = false;
			mostrarComboDoador = true;
			break;
		}
		
		case 2: {
			mostrarComboCampanha = false;
			mostrarComboDoador = false;
			mostrarComboInstituicao = false;
			entrada.setInstituicao(null);
			entrada.setDoador(null);
			entrada.setCampanha(null);
			
			break;
		}
		case 3: {
			if(listaInstituicaoCombo == null || listaInstituicaoCombo.size() == 0){
				listaInstituicaoCombo = instituicaoService.pesquisarInstituicao(new Instituicao());
			}
			entrada.setInstituicao(new Instituicao());
			entrada.setDoador(null);
			entrada.setCampanha(null);
			
			mostrarComboCampanha = false;
			mostrarComboDoador = false;
			mostrarComboInstituicao = true;
			break;
		}
		
		}
	}
	
	public void listenerObterPrecoMedio(){
		entrada.setProduto(produtoService.obterProduto(entrada.getProduto().getId()));
		valorMedioProduto = (entrada.getProduto().valorMedioProduto());
	}
	
	public EntradaDataModel getEntradaDataModel() {
		return entradaDataModel;
	}

	public void setEntradaDataModel(EntradaDataModel entradaDataModel) {
		this.entradaDataModel = entradaDataModel;
	}

	public Entrada getEntrada() {
		return entrada;
	}

	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
	}

	public List<Produto> getListProdutoCombo() {
		return listProdutoCombo;
	}

	public void setListProdutoCombo(List<Produto> listProdutoCombo) {
		this.listProdutoCombo = listProdutoCombo;
	}

	public List<TipoParceiroEnum> getListaTipoParceiroCombo() {
		return listaTipoParceiroCombo;
	}

	public void setListaTipoParceiroCombo(
			List<TipoParceiroEnum> listaTipoParceiroCombo) {
		this.listaTipoParceiroCombo = listaTipoParceiroCombo;
	}

	public List<Campanha> getListaCampanhaCombo() {
		return listaCampanhaCombo;
	}

	public void setListaCampanhaCombo(List<Campanha> listaCampanhaCombo) {
		this.listaCampanhaCombo = listaCampanhaCombo;
	}

	public List<Instituicao> getListaInstituicaoCombo() {
		return listaInstituicaoCombo;
	}

	public void setListaInstituicaoCombo(List<Instituicao> listaInstituicaoCombo) {
		this.listaInstituicaoCombo = listaInstituicaoCombo;
	}

	public List<Doador> getListaDoadorCombo() {
		return listaDoadorCombo;
	}

	public void setListaDoadorCombo(List<Doador> listaDoadorCombo) {
		this.listaDoadorCombo = listaDoadorCombo;
	}

	public Boolean getMostrarComboCampanha() {
		return mostrarComboCampanha;
	}

	public void setMostrarComboCampanha(Boolean mostrarComboCampanha) {
		this.mostrarComboCampanha = mostrarComboCampanha;
	}

	public Boolean getMostrarComboInstituicao() {
		return mostrarComboInstituicao;
	}

	public void setMostrarComboInstituicao(Boolean mostrarComboInstituicao) {
		this.mostrarComboInstituicao = mostrarComboInstituicao;
	}

	public Boolean getMostrarComboDoador() {
		return mostrarComboDoador;
	}

	public void setMostrarComboDoador(Boolean mostrarComboDoador) {
		this.mostrarComboDoador = mostrarComboDoador;
	}

	public TipoParceiroEnum getTipoParceiroSelecionado() {
		return tipoParceiroSelecionado;
	}

	public void setTipoParceiroSelecionado(TipoParceiroEnum tipoParceiroSelecionado) {
		this.tipoParceiroSelecionado = tipoParceiroSelecionado;
	}

	public Float getValorMedioProduto() {
		return valorMedioProduto;
	}

	public void setValorMedioProduto(Float valorMedioProduto) {
		this.valorMedioProduto = valorMedioProduto;
	}

}
