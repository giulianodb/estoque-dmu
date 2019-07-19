package org.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.controller.model.InstituicaoDataModel;
import org.entity.Instituicao;
import org.exception.ApplicationException;
import org.exception.ControllerExceptionHandler;
import org.service.InstituicaoService;
import org.util.Message;

@Named
@ViewScoped
@ControllerExceptionHandler
public class InstituicaoController implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private InstituicaoDataModel instituicaoDataModel;
	
	@Inject
	private Instituicao instituicao;
	
	@Inject
	private InstituicaoService instituicaoService;
	
	@Inject
	private Provider<Instituicao> instituicaoProvider;
	
	
	public void iniciarPesquisaInstituicao(){
		
//		return "/pages/instituicao/listar_instituicao";
	}
	
	public void iniciarEditarInstituicao(String cod){
		
		if (!"".equals(cod) && cod != null) {
			try {
				instituicao = instituicaoService.obterInstituicao(Integer.valueOf(cod));
			} catch (ApplicationException e) {
				e.printStackTrace();
				instituicao = instituicaoProvider.get();
			}
		} else {
			instituicao = instituicaoProvider.get();
		}
//		return "/pages/instituicao/editar_instituicao";
	}
	
	public String iniciarIncluirInstituicao(){
		
		return "/pages/instituicao/editar_instituicao?faces-redirect=true";
	}
	
	public String iniciarAlterarInstituicao(){
		try {
			instituicao = instituicaoService.obterInstituicao(instituicao.getId());
			
			
			StringBuilder outcome = new StringBuilder("/pages/instituicao/editar_instituicao.jsf?faces-redirect=true");
			outcome.append("&codInstituicao=").append(instituicao.getId());
			return outcome.toString();
			
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Message.setMessage("ERRO");
		}
		
		return "/pages/instituicao/editar_instituicao";
	}
	
	public String excluirInstituicao(){
		try {
			instituicaoService.excluirInstituicao(instituicao);
			Message.setMessage("controller.excluirInstituicao.SUCESSO");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return "/pages/instituicao/listar_instituicao?faces-redirect=true";
	}
	
	public String incluirInstituicao(){
		try {
			instituicaoService.incluirInstituicao(instituicao);
			Message.setMessage("SUCESSO");
			
		} catch (ApplicationException e) {
			// TODO: handle exception
			e.printStackTrace();
			
			Message.setMessage(e);
			
			return "";
		}
		
		return "/pages/instituicao/listar_instituicao.jsf?faces-redirect=true";
	}
	
	public String alterarInstituicao(){
		
		try {
			instituicaoService.alterarInstituicao(instituicao);
			
			Message.setMessage("controller.alterarInstituicao.SUCESSO");
			
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			Message.setMessage(e);
			
			return "";
			
		}
		return "/pages/instituicao/listar_instituicao";
	}

	public InstituicaoDataModel getInstituicaoDataModel() {
		return instituicaoDataModel;
	}

	public void setInstituicaoDataModel(InstituicaoDataModel instituicaoDataModel) {
		this.instituicaoDataModel = instituicaoDataModel;
	}

	public Instituicao getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(Instituicao instituicao) {
		this.instituicao = instituicao;
	}

}
