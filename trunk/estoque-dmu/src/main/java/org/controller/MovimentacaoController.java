package org.controller;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.controller.model.MovimentacaoDataModel;
import org.entity.Movimentacao;
import org.exception.ControllerExceptionHandler;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class MovimentacaoController implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Movimentacao movimentacao;
	
	@Inject
	private MovimentacaoDataModel movimentacaoDataModel;
	
	
	public String iniciarPesquisaMovimentacao(){
		
		return "/pages/movimentacao/listar_movimentacao";
	}
	
	
	
	
	public String excluirEntrada(){
		
		return null;
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
