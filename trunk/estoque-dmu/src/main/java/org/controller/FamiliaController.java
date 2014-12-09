package org.controller;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.controller.model.FamiliaDataModel;
import org.entity.Familia;
import org.exception.ControllerExceptionHandler;
import org.service.FamiliaService;
import org.util.Message;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class FamiliaController implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private FamiliaDataModel familiaDataModel;
	
	@Inject
	private Familia familia;
	
	@Inject
	private FamiliaService familiaService;
	
	@Inject
	private Provider<Familia> familiaProvider;
	
	
	public String iniciarPesquisaFamilia(){
		
		return "/pages/familia/listar_familia";
	}
	
	public String iniciarIncluirFamilia(){
		
		familia = familiaProvider.get();
		
		return "/pages/familia/editar_familia";
	}
	
	public String excluirFamilia(){
		
		return null;
	}
	
	public String incluirFamilia(){
		try {
			familiaService.incluirFamilia(familia);
			Message.setMessage("SUCESSO");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return iniciarPesquisaFamilia();
	}
	
	public String alterarFamilia(){
		return "/pages/familia/listar_familia";
	}

	public FamiliaDataModel getFamiliaDataModel() {
		return familiaDataModel;
	}

	public void setFamiliaDataModel(FamiliaDataModel familiaDataModel) {
		this.familiaDataModel = familiaDataModel;
	}

	public Familia getFamilia() {
		return familia;
	}

	public void setFamilia(Familia familia) {
		this.familia = familia;
	}

}
