package org.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.controller.model.FamiliaDataModel;
import org.entity.Familia;
import org.exception.ApplicationException;
import org.exception.ControllerExceptionHandler;
import org.service.FamiliaService;
import org.util.Message;

@Named
@ViewScoped
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
	
	
	public void iniciarPesquisaFamilia(){
		
//		return "/pages/familia/listar_familia";
	}
	
	public void iniciarEditarFamilia(String cod){
		if (!"".equals(cod) && cod != null) {
			try {
				familia = familiaService.obterFamilia(Integer.valueOf(cod));
			} catch (ApplicationException e) {
				e.printStackTrace();
				familia = familiaProvider.get();
			}
			} else {
				familia = familiaProvider.get();
		}
//		return "/pages/familia/editar_familia";
	}
	
	
	public String iniciarIncluirFamilia(){
		return "/pages/familia/editar_familia?faces-redirect-true";
	}
	
	
	public String iniciarAlterarFamilia(){
		
		try {
			familia = familiaService.obterFamilia(familia.getId());
			
			
			StringBuilder outcome = new StringBuilder("/pages/familia/editar_familia.jsf?faces-redirect=true");
			outcome.append("&codFamilia=").append(familia.getId());
			return outcome.toString();
			
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Message.setMessage(e);
		}
		
		return "/pages/familia/editar_familia";
	}
	
	public String excluirFamilia(){
		
		try {
			familiaService.excluirFamilia(familia);
			Message.setMessage("controller.excluirFamilia.SUCESSO");
		} 
		catch (ApplicationException e){
			Message.setMessage(e);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Message.setMessage(new ApplicationException(e));
		}
		return "";
		
	}
	
	public String incluirFamilia(){
		try {
			familiaService.incluirFamilia(familia);
			Message.setMessage("controller.incluirFamilia.SUCESSO");
			
		}
		catch (ApplicationException e){
			Message.setMessage(e);
			return "";
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Message.setMessage(new ApplicationException(e));
			
			return "";
		}
		
		return "/pages/familia/listar_familia.jsf?faces-redirect=true";
	}
	
	public String alterarFamilia(){
		try {
			familiaService.alterarFamilia(familia);
			Message.setMessage("controller.alterarFamilia.SUCESSO");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
