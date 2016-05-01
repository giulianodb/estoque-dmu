package org.controller;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.controller.model.InstituicaoDataModel;
import org.entity.Instituicao;
import org.exception.ApplicationException;
import org.exception.ControllerExceptionHandler;
import org.service.InstituicaoService;
import org.util.Message;

@Named
@ViewAccessScoped
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
	
	
	public String iniciarPesquisaInstituicao(){
		
		return "/pages/instituicao/listar_instituicao";
	}
	
	public String iniciarIncluirInstituicao(){
		
		instituicao = instituicaoProvider.get();
		
		return "/pages/instituicao/editar_instituicao";
	}
	
	public String iniciarAlterarInstituicao(){
		try {
			instituicao = instituicaoService.obterInstituicao(instituicao.getId());
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Message.setMessage("ERRO");
		}
		
		return "/pages/instituicao/editar_instituicao";
	}
	
	public String excluirInstituicao(){
		
		return null;
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
		
		return iniciarPesquisaInstituicao();
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
