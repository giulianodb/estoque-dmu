package org.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.controller.model.CampanhaDataModel;
import org.entity.Campanha;
import org.exception.ApplicationException;
import org.exception.ControllerExceptionHandler;
import org.service.CampanhaService;
import org.util.DateUtil.MesesEnum;
import org.util.Message;

@Named
@ViewScoped
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
	
	@Inject
	private CampanhaDataModel campanhaDataModel;
	
	public void iniciarPesquisaCampanha(){
	
		listaMeses = new ArrayList<MesesEnum>(Arrays.asList(MesesEnum.values()));
		
//		return "/pages/campanha/listar_campanha";
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
	
	public void iniciarEditarCampanha(String cod){
		listaMeses = new ArrayList<MesesEnum>(Arrays.asList(MesesEnum.values()));
		
		
		if (!"".equals(cod) && cod != null) {
			try {
				campanha = campanhaService.obterCampanha(Integer.valueOf(cod));
			} catch (ApplicationException e) {
				e.printStackTrace();
				campanha = campanhaProvider.get();
			}
		} else {
			campanha = campanhaProvider.get();
		}
		
		
//		return "/pages/campanha/editar_campanha";
	}
	
	public String excluirCampanha(){
		try {
			campanhaService.excluirCampanha(campanha);
			Message.setMessage("controller.excluirCampanha.SUCESSO");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return "/pages/campanha/listar_campanha?faces-redirect=true";
		
	}
	
	public String incluirCampanha(){
		try {
			campanhaService.incluirCampanha(campanha);
			Message.setMessage("SUCESSO");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return "/pages/campanha/listar_campanha";
	}
	
	public String iniciarIncluirCampanha() {
		return "/pages/campanha/editar_campanha?faces-redirect=true";
	}
	
	public String iniciarAlterarCampanha(){
		try {
			campanha = campanhaService.obterCampanha(campanha.getId());
			
			StringBuilder outcome = new StringBuilder("/pages/campanha/editar_campanha.jsf?faces-redirect=true");
			outcome.append("&codCampanha=").append(campanha.getId());
			return outcome.toString();
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Message.setMessage("Erro ao alterar campanha");
		}
		
		return "/pages/campanha/editar_campanha.jsf?faces-redirect=true";
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

	public CampanhaDataModel getCampanhaDataModel() {
		return campanhaDataModel;
	}

	public void setCampanhaDataModel(CampanhaDataModel campanhaDataModel) {
		this.campanhaDataModel = campanhaDataModel;
	}

}
