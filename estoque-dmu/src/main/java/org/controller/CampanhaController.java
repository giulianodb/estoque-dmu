package org.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.entity.Campanha;
import org.entity.TipoProdutoEnum;
import org.exception.ApplicationException;
import org.exception.ControllerExceptionHandler;
import org.service.CampanhaService;
import org.util.Message;
import org.util.DateUtil.MesesEnum;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class CampanhaController implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Campanha campanha;

	@Inject
	private Campanha campanhaPesquisa;
			
	@Inject
	private CampanhaService campanhaService;
	
	@Inject
	private Provider<Campanha> campanhaProvider;
	
	private List<Campanha> listaCampanha;
	
	private List<MesesEnum> listaMeses;
	
	public String iniciarPesquisaCampanha(){
		
		listaMeses = new ArrayList<MesesEnum>(Arrays.asList(MesesEnum.values()));
		
		return "/pages/campanha/listar_campanha";
	}
	
	public String pesquisarCampanha(){
		
		try {
			listaCampanha = campanhaService.pesquisarCampanha(campanhaPesquisa);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String iniciarIncluirCampanha(){
		listaMeses = new ArrayList<MesesEnum>(Arrays.asList(MesesEnum.values()));
		campanha = campanhaProvider.get();
		
		return "/pages/campanha/editar_campanha";
	}
	
	public String excluirCampanha(){
		
		return null;
	}
	
	public String incluirCampanha(){
		try {
			campanhaService.incluirCampanha(campanha);
			Message.setMessage("SUCESSO");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return iniciarPesquisaCampanha();
	}
	
	
	public String iniciarAlterarCampanha(){
		
		try {
			campanha = campanhaService.obterCampanha(campanha.getId());
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Message.setMessage("ERRO");
		}
		
		return "/pages/campanha/editar_campanha";
	}
	
	
	public String alterarCampanha(){
		
		try {
			campanhaService.alterarCampanha(campanha);
			Message.setMessage("controller.alterarCampanha.SUCESSO");
			
		} catch (Exception e) {
			// TODO: handle exception
			Message.setMessage("ERRO",FacesMessage.SEVERITY_ERROR);
		}
		
		return "/pages/campanha/listar_campanha";
	}

	public Campanha getCampanha() {
		return campanha;
	}

	public void setCampanha(Campanha campanha) {
		this.campanha = campanha;
	}

	public Campanha getCampanhaPesquisa() {
		return campanhaPesquisa;
	}

	public void setCampanhaPesquisa(Campanha campanhaPesquisa) {
		this.campanhaPesquisa = campanhaPesquisa;
	}

	public List<Campanha> getListaCampanha() {
		return listaCampanha;
	}

	public void setListaCampanha(List<Campanha> listaCampanha) {
		this.listaCampanha = listaCampanha;
	}

	public List<MesesEnum> getListaMeses() {
		return listaMeses;
	}

	public void setListaMeses(List<MesesEnum> listaMeses) {
		this.listaMeses = listaMeses;
	}

}
