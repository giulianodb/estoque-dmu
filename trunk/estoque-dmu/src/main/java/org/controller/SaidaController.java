package org.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.controller.model.MovimentacaoDataModel;
import org.entity.Campanha;
import org.entity.Familia;
import org.entity.Instituicao;
import org.entity.Movimentacao;
import org.entity.Produto;
import org.entity.TipoParceiroEnum;
import org.exception.ApplicationException;
import org.exception.ControllerExceptionHandler;
import org.service.CampanhaService;
import org.service.FamiliaService;
import org.service.InstituicaoService;
import org.service.MovimentacaoService;
import org.service.ProdutoService;
import org.util.Message;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class SaidaController implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private MovimentacaoDataModel saidaDataModel;
	
	@Inject
	private Movimentacao saida;
	
	@Inject
	private MovimentacaoService saidaService;
	
	@Inject
	private CampanhaService campanhaService;
	
	@Inject
	private FamiliaService familiaService;
	
	@Inject
	private InstituicaoService instituicaoService;
	
	@Inject
	private Provider<Movimentacao> movimentacaoProvider;
	
	private List<Produto> listProdutoCombo;
	
	private List<TipoParceiroEnum> listaTipoParceiroCombo;
	
	private List<TipoParceiroEnum> listaTipoParceiroCampanhaCombo;
	
	private List<Campanha> listaCampanhaCombo;
	
	private List<Instituicao> listaInstituicaoCombo;
	
	private List<Familia> listaFamiliaCombo;
	
	private TipoParceiroEnum tipoParceiroSelecionado;
	
	private TipoParceiroEnum tipoParceiroCampanhaSelecionado;
	
	private Boolean mostrarComboCampanha;
	
	private Boolean mostrarComboInstituicao;
	
	private Boolean mostrarComboFamilia;
	
	private Boolean mostrarComboFamiliaCampanha = false;
	
	private Boolean mostrarComboInstituicaoCampanha = false;
	
	private Float valorMedioProduto;
	
	@Inject
	private ProdutoService produtoService;
	
	
	public String iniciarPesquisaSaida(){
		
		return "/pages/movimentacao/listar_movimentacao";
	}
	
	public String iniciarIncluirMovimentacaoSaida() throws ApplicationException{
		
		saida = movimentacaoProvider.get();
		valorMedioProduto = 0f;
		//carregando combos iniciais
		listProdutoCombo = produtoService.pesquisarProduto(new Produto(), 0, 0);
		listaTipoParceiroCombo = new ArrayList<TipoParceiroEnum>(Arrays.asList(TipoParceiroEnum.values()));
		listaTipoParceiroCampanhaCombo = new ArrayList<TipoParceiroEnum>();
		
		listaTipoParceiroCampanhaCombo.add(TipoParceiroEnum.FAMILIA);
		listaTipoParceiroCampanhaCombo.add(TipoParceiroEnum.INSTITUICAO);
//		
//		saida.getLoteMovimentacao().setFamiliaCampanha(new Familia());
//		saida.getLoteMovimentacao().setInstituicaoCampanha(new Instituicao());
		
		
		mostrarComboCampanha = false;
		mostrarComboFamilia = false;
		mostrarComboInstituicao = false;
		
		saida.setData(new Date());
		
		return "/pages/movimentacao/saida/editar_saida";
	}
	
	
	public String iniciarAlterarMovimentacaoSaida() throws ApplicationException{
		
//		valorMedioProduto = 0f;
		
		//carregando combos iniciais
		listProdutoCombo = produtoService.pesquisarProduto(new Produto(), 0, 0);
		listaTipoParceiroCombo = new ArrayList<TipoParceiroEnum>(Arrays.asList(TipoParceiroEnum.values()));
		listaTipoParceiroCampanhaCombo = new ArrayList<TipoParceiroEnum>();
		
		listaTipoParceiroCampanhaCombo.add(TipoParceiroEnum.FAMILIA);
		listaTipoParceiroCampanhaCombo.add(TipoParceiroEnum.INSTITUICAO);
//		
//		saida.getLoteMovimentacao().setFamiliaCampanha(new Familia());
//		saida.getLoteMovimentacao().setInstituicaoCampanha(new Instituicao());
		
		if (saida.getLoteMovimentacao().getCampanha() != null){
			tipoParceiroSelecionado = TipoParceiroEnum.CAMPANHA;
			mostrarComboCampanha = true;
			mostrarComboInstituicao = false;
			mostrarComboFamilia = false;
			mostrarComboFamiliaCampanha = false;
			
			if(listaCampanhaCombo == null || listaCampanhaCombo.size() == 0){
				listaCampanhaCombo = campanhaService.pesquisarCampanha(new Campanha());
			}
			
			
		} else if(saida.getLoteMovimentacao().getFamilia() != null){
			tipoParceiroSelecionado = TipoParceiroEnum.FAMILIA;
			mostrarComboCampanha = false;
			mostrarComboInstituicao = false;
			mostrarComboFamilia = true;
			mostrarComboFamiliaCampanha = false;
			if(listaFamiliaCombo == null || listaFamiliaCombo.size() == 0){
				listaFamiliaCombo = familiaService.pesquisarFamilia(new Familia());
			}
		} else if(saida.getLoteMovimentacao().getInstituicao() != null){
			tipoParceiroSelecionado = TipoParceiroEnum.INSTITUICAO;
			mostrarComboCampanha = false;
			mostrarComboInstituicao = true;
			mostrarComboFamilia = false;
			mostrarComboFamiliaCampanha = false;
			if(listaInstituicaoCombo == null || listaInstituicaoCombo.size() == 0){
				listaInstituicaoCombo = instituicaoService.pesquisarInstituicao(new Instituicao());
			}
		} else {
			tipoParceiroSelecionado = TipoParceiroEnum.ANONIMO;
			mostrarComboCampanha = false;
			mostrarComboInstituicao = false;
			mostrarComboFamilia = false;
			mostrarComboFamiliaCampanha = false;
		}
		
		
		
		
		if (saida.getLoteMovimentacao().getFamiliaCampanha() != null){
			tipoParceiroCampanhaSelecionado = TipoParceiroEnum.FAMILIA;
			if(listaFamiliaCombo == null || listaFamiliaCombo.size() == 0){
				listaFamiliaCombo = familiaService.pesquisarFamilia(new Familia());
			}
			mostrarComboInstituicaoCampanha = false;
			mostrarComboFamiliaCampanha = true;
		} else if(saida.getLoteMovimentacao().getInstituicaoCampanha() != null){
			tipoParceiroCampanhaSelecionado = TipoParceiroEnum.INSTITUICAO;
			if(listaInstituicaoCombo == null || listaInstituicaoCombo.size() == 0){
				listaInstituicaoCombo = instituicaoService.pesquisarInstituicao(new Instituicao());
			}
			mostrarComboInstituicaoCampanha = true;
			mostrarComboFamiliaCampanha = false;
		}
			

	
		listenerObterPrecoMedio();
		
		
		return "/pages/movimentacao/saida/editar_saida";
	}
	
	public String excluirSaida(){
		
		return null;
	}
	
	public String incluirSaida(){
		try {
			
			if (saida.getLoteMovimentacao().getFamiliaCampanha() != null && saida.getLoteMovimentacao().getFamiliaCampanha().getId() != null){
				saida.getLoteMovimentacao().setFamiliaCampanha(familiaService.obterFamilia(saida.getLoteMovimentacao().getFamiliaCampanha().getId()));
				
			}else {
				saida.getLoteMovimentacao().setFamiliaCampanha(null);
			}
			
			
			if (saida.getLoteMovimentacao().getFamilia() != null && saida.getLoteMovimentacao().getFamilia().getId() != null){
				saida.getLoteMovimentacao().setFamilia(familiaService.obterFamilia(saida.getLoteMovimentacao().getFamilia().getId()));
				
			}else {
				saida.getLoteMovimentacao().setFamilia(null);
			}
			
			
			
			if (saida.getLoteMovimentacao().getInstituicao() != null && saida.getLoteMovimentacao().getInstituicao().getId() != null){
				saida.getLoteMovimentacao().setInstituicao(instituicaoService.obterInstituicao(saida.getLoteMovimentacao().getInstituicao().getId()));
				
			}else {
				saida.getLoteMovimentacao().setInstituicao(null);
			}
			
			
			
			if (saida.getLoteMovimentacao().getInstituicaoCampanha() != null && saida.getLoteMovimentacao().getInstituicaoCampanha().getId() != null){
				saida.getLoteMovimentacao().setInstituicaoCampanha(instituicaoService.obterInstituicao(saida.getLoteMovimentacao().getInstituicaoCampanha().getId()));
				
			}else {
				saida.getLoteMovimentacao().setInstituicaoCampanha(null);
			}
			
			
			if (saida.getLoteMovimentacao().getCampanha() != null && saida.getLoteMovimentacao().getCampanha().getId() != null){
				saida.getLoteMovimentacao().setCampanha(campanhaService.obterCampanha(saida.getLoteMovimentacao().getCampanha().getId()));
				
			}else {
				saida.getLoteMovimentacao().setCampanha(null);
			}
			
			
			
//			
			saidaService.incluirSaida(saida);
			Message.setMessage("controller.incluirSaida.SUCESSO");
			
		} catch (Exception e) {
			Message.setMessage("ERRO", FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
		}
		
		return iniciarPesquisaSaida();
	}
	
	public String alterarSaida(){
		
		try {
			saidaService.alterarSaida1(saida);
			
			saidaService.alterarSaida2(saida);
			Message.setMessage("controller.incluirSaida.SUCESSO");
			
		} catch (Exception e) {
			e.printStackTrace();
			Message.setMessage("ERRO", FacesMessage.SEVERITY_ERROR);
		}
		return iniciarPesquisaSaida();
	}
	
	public void listenerAlterarTipoReceptor() throws ApplicationException{
		switch (tipoParceiroSelecionado.ordinal()) {
		case 0: {
			if(listaCampanhaCombo == null || listaCampanhaCombo.size() == 0){
				listaCampanhaCombo = campanhaService.pesquisarCampanha(new Campanha());
			}
			saida.getLoteMovimentacao().setCampanha(new Campanha());
			mostrarComboCampanha = true;
			mostrarComboFamilia = false;
			mostrarComboInstituicao = false;
			mostrarComboFamiliaCampanha = false;
			saida.getLoteMovimentacao().setFamiliaCampanha(new Familia());
			saida.getLoteMovimentacao().setInstituicaoCampanha(new Instituicao());
			
			if(listaFamiliaCombo == null || listaFamiliaCombo.size() == 0){
				listaFamiliaCombo = familiaService.pesquisarFamilia(new Familia());
			}
			if(listaInstituicaoCombo == null || listaInstituicaoCombo.size() == 0){
				listaInstituicaoCombo = instituicaoService.pesquisarInstituicao(new Instituicao());
			}
			
			
			break;
		}
		case 1: {
			if(listaFamiliaCombo == null || listaFamiliaCombo.size() == 0){
				listaFamiliaCombo = familiaService.pesquisarFamilia(new Familia());
			}
			
			saida.getLoteMovimentacao().setFamilia(new Familia());
			mostrarComboCampanha = false;
			mostrarComboInstituicao = false;
			mostrarComboFamilia = true;
			mostrarComboFamiliaCampanha = false;
			break;
		}
		
		case 2: {
			mostrarComboCampanha = false;
			mostrarComboFamilia = false;
			mostrarComboInstituicao = false;
			mostrarComboFamiliaCampanha = false;
			break;
		}
		case 3: {
			if(listaInstituicaoCombo == null || listaInstituicaoCombo.size() == 0){
				listaInstituicaoCombo = instituicaoService.pesquisarInstituicao(new Instituicao());
			}
			saida.getLoteMovimentacao().setInstituicao(new Instituicao());
			
			mostrarComboCampanha = false;
			mostrarComboFamilia = false;
			mostrarComboInstituicao = true;
			mostrarComboFamiliaCampanha = false;
			break;
		}
		
		}
	}
	
	public void listenerAlterarTipoReceptorCampanha() throws ApplicationException{
		switch (tipoParceiroCampanhaSelecionado.ordinal()) {
	
		case 1: {
			if(listaFamiliaCombo == null || listaFamiliaCombo.size() == 0){
				listaFamiliaCombo = familiaService.pesquisarFamilia(new Familia());
			}
			
			saida.getLoteMovimentacao().setFamiliaCampanha(new Familia());
			mostrarComboInstituicaoCampanha = false;
			mostrarComboFamiliaCampanha = true;
			break;
		}
		

		case 3: {
			if(listaInstituicaoCombo == null || listaInstituicaoCombo.size() == 0){
				listaInstituicaoCombo = instituicaoService.pesquisarInstituicao(new Instituicao());
			}
			saida.getLoteMovimentacao().setInstituicaoCampanha(new Instituicao());
			
			mostrarComboInstituicaoCampanha = true;
			mostrarComboFamiliaCampanha = false;
			break;
		}
		
		}
	}
	
	
	public void listenerObterPrecoMedio(){
		saida.setProduto(produtoService.obterProduto(saida.getProduto().getId()));
		valorMedioProduto = (saida.getProduto().valorMedioProduto());
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

	public List<Familia> getListaFamiliaCombo() {
		return listaFamiliaCombo;
	}

	public void setListaFamiliaCombo(List<Familia> listaFamiliaCombo) {
		this.listaFamiliaCombo = listaFamiliaCombo;
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

	public Boolean getMostrarComboFamilia() {
		return mostrarComboFamilia;
	}

	public void setMostrarComboFamilia(Boolean mostrarComboFamilia) {
		this.mostrarComboFamilia = mostrarComboFamilia;
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

	public Boolean getMostrarComboFamiliaCampanha() {
		return mostrarComboFamiliaCampanha;
	}

	public void setMostrarComboFamiliaCampanha(Boolean mostrarComboFamiliaCampanha) {
		this.mostrarComboFamiliaCampanha = mostrarComboFamiliaCampanha;
	}

	public Movimentacao getSaida() {
		return saida;
	}

	public void setSaida(Movimentacao saida) {
		this.saida = saida;
	}

	public Boolean getMostrarComboInstituicaoCampanha() {
		return mostrarComboInstituicaoCampanha;
	}

	public void setMostrarComboInstituicaoCampanha(
			Boolean mostrarComboInstituicaoCampanha) {
		this.mostrarComboInstituicaoCampanha = mostrarComboInstituicaoCampanha;
	}

	public TipoParceiroEnum getTipoParceiroCampanhaSelecionado() {
		return tipoParceiroCampanhaSelecionado;
	}

	public void setTipoParceiroCampanhaSelecionado(
			TipoParceiroEnum tipoParceiroCampanhaSelecionado) {
		this.tipoParceiroCampanhaSelecionado = tipoParceiroCampanhaSelecionado;
	}

	public List<TipoParceiroEnum> getListaTipoParceiroCampanhaCombo() {
		return listaTipoParceiroCampanhaCombo;
	}

	public void setListaTipoParceiroCampanhaCombo(
			List<TipoParceiroEnum> listaTipoParceiroCampanhaCombo) {
		this.listaTipoParceiroCampanhaCombo = listaTipoParceiroCampanhaCombo;
	}

}
