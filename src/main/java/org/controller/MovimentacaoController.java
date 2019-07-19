package org.controller;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.controller.model.MovimentacaoDataModel;
import org.entity.LoteMovimentacao;
import org.entity.Movimentacao;
import org.exception.ApplicationException;
import org.exception.ControllerExceptionHandler;
import org.service.LoteService;
import org.service.MovimentacaoService;
import org.util.Message;

@Named
@ViewScoped
@ControllerExceptionHandler
public class MovimentacaoController implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Movimentacao movimentacao;
	
	@Inject
	private MovimentacaoDataModel movimentacaoDataModel;
	
	@EJB
	private MovimentacaoService movimentacaoService;
	
	@Inject
	private RelatorioController relatorioController;
	
	@EJB
	private LoteService loteService;
	
	public void iniciarPesquisaMovimentacao(String cod) throws NumberFormatException, ApplicationException{
		
		if (!"".equals(cod) && cod != null) {
			LoteMovimentacao lote = loteService.obterLoteMovimentacaoByChave(Integer.valueOf(cod));
			relatorioController.reciboSaida(lote);
		}
		
//		return "/pages/movimentacao/listar_movimentacao";
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
		
		
		return "/pages/movimentacao/listar_movimentacao.jsf?faces-redirect=true";
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
