package org.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.controller.model.DoadorDataModel;
import org.entity.Doador;
import org.exception.ApplicationException;
import org.exception.ControllerExceptionHandler;
import org.service.DoadorService;
import org.util.Message;

@Named
@ViewScoped
@ControllerExceptionHandler
public class DoadorController implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DoadorDataModel doadorDataModel;
	
	@Inject
	private Doador doador;
	
	@Inject
	private DoadorService doadorService;
	
	@Inject
	private Provider<Doador> doadorProvider;
	
	public void iniciarPesquisaDoador(){
		
//		return "/pages/doador/listar_doador";
	}
	
	public void iniciarEditarDoador(String codDoador){
		
		if (!"".equals(codDoador) && codDoador != null) {
			try {
				doador = doadorService.obterDoador(Integer.valueOf(codDoador));
			} catch (ApplicationException e) {
				e.printStackTrace();
				doador = doadorProvider.get();
			}
		}
			else {
				doador = doadorProvider.get();
				
			}
		
//		return "/pages/doador/editar_doador";
	}
	
	public String iniciarIncluirDoador(){
		
		
		return "/pages/doador/editar_doador?faces-redirect=true";
	}
	
	public String iniciarAlterarDoador(){
		
		try {
			doador = doadorService.obterDoador(doador.getId());
			
			StringBuilder outcome = new StringBuilder("/pages/doador/editar_doador.jsf?faces-redirect=true");
			outcome.append("&codDoador=").append(doador.getId());
			return outcome.toString();
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Message.setMessage("ERRO");
			
			return "";
		}
		
//		return "/pages/familia/editar_familia";
	}
	
	
	public String excluirDoador(){
		try {
			doadorService.deletarDoador(doador);
			Message.setMessage("controller.excluirDoador.SUCESSO");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return "/pages/doador/listar_doador?faces-redirect=true";
	}
	
	public String incluirDoador(){
		try {
			doadorService.incluirDoador(doador);
			Message.setMessage("controller.incluirDoador.SUCESSO");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return "/pages/doador/listar_doador.jsf?faces-redirect=true";
	}
	
	public String alterarDoador(){
		try {
			doadorService.alterarDoador(doador);
			Message.setMessage("controller.alterarDoador.SUCESSO");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
			Message.setMessage("ERRO",FacesMessage.SEVERITY_ERROR);
		}
		return "/pages/doador/listar_doador";
	}

	public DoadorDataModel getDoadorDataModel() {
		return doadorDataModel;
	}

	public void setDoadorDataModel(DoadorDataModel doadorDataModel) {
		this.doadorDataModel = doadorDataModel;
	}

	public Doador getDoador() {
		return doador;
	}

	public void setDoador(Doador doador) {
		this.doador = doador;
	}

}
