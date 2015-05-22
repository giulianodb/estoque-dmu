package org.controller;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.controller.model.MovimentacaoDataModel;
import org.entity.Movimentacao;
import org.exception.ApplicationException;
import org.exception.ControllerExceptionHandler;
import org.service.MovimentacaoService;
import org.util.Message;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class MovimentacaoController implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Movimentacao movimentacao;
	
	@Inject
	private MovimentacaoDataModel movimentacaoDataModel;
	
	@EJB
	private MovimentacaoService movimentacaoService;
	
	
	public String iniciarPesquisaMovimentacao(){
		
		return "/pages/movimentacao/listar_movimentacao";
	}
	
	
	
	
	public String excluirEntrada(){
		try {
			movimentacaoService.excluirMovimentacao(movimentacao);
			Message.setMessage("controller.excluirMovimentacao.SUCESSO");
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			Message.setMessage("controller.incluirMovimentacao.ERRO", FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
		}
		
		
		return "/pages/movimentacao/listar_movimentacao";
	}


	public MovimentacaoDataModel getMovimentacaoDataModel() {
		return movimentacaoDataModel;
	}

	public void setMovimentacaoDataModel(MovimentacaoDataModel movimentacaoDataModel) {
		this.movimentacaoDataModel = movimentacaoDataModel;
	}




	public Movimentacao getMovimentacao() {
		return movimentacao;
	}




	public void setMovimentacao(Movimentacao movimentacao) {
		this.movimentacao = movimentacao;
	}
	
}
