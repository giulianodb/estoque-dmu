package org.controller;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.controller.model.DoadorDataModel;
import org.entity.Doador;
import org.exception.ControllerExceptionHandler;
import org.service.DoadorService;
import org.util.Message;

@Named
@ViewAccessScoped
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
	
	
	public String iniciarPesquisaDoador(){
		
		return "/pages/doador/listar_doador";
	}
	
	public String iniciarIncluirDoador(){
		
		doador = doadorProvider.get();
		
		return "/pages/doador/editar_doador";
	}
	
	public String excluirDoador(){
		
		return null;
	}
	
	public String incluirDoador(){
		try {
			doadorService.incluirDoador(doador);
			Message.setMessage("SUCESSO");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return iniciarPesquisaDoador();
	}
	
	public String alterarDoador(){
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
