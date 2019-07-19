package org.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.controller.model.LoteDataModel;
import org.entity.LoteMovimentacao;
import org.entity.Movimentacao;
import org.entity.TipoMovimentacaoEnum;
import org.exception.ApplicationException;
import org.exception.ControllerExceptionHandler;
import org.service.LoteService;
import org.util.Message;

@Named
@ViewScoped
@ControllerExceptionHandler
public class LoteController implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Movimentacao movimentacao;
	
	@Inject
	private LoteService loteService;
	
	@Inject
	private LoteDataModel loteDataModel;
	
	private List<TipoMovimentacaoEnum> tipoMovimentacaoCombo;
	
	
	public void iniciarPesquisaReceibo(){
		
		if (tipoMovimentacaoCombo == null){
			tipoMovimentacaoCombo = new ArrayList<TipoMovimentacaoEnum>(Arrays.asList(TipoMovimentacaoEnum.values()));
		}
		
//		return "/pages/movimentacao/listar_recibo";
	}
	
	public String teste()
	
	{
		
		System.out.println("ZABUMABA");
		
		return null;
	}
	
	
	public String excluirLote(LoteMovimentacao lote){
		
		try {
			loteService.excluirLoteComMovimentacoes(lote.getCodigo());
			
			Message.setMessage("controller.excluirLote.SUCESSO", FacesMessage.SEVERITY_INFO);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			
			Message.setMessage(e);
			
			e.printStackTrace();
		}
		
		return "/pages/movimentacao/listar_recibo";
	}


	public Movimentacao getMovimentacao() {
		return movimentacao;
	}




	public void setMovimentacao(Movimentacao movimentacao) {
		this.movimentacao = movimentacao;
	}




	public LoteDataModel getLoteDataModel() {
		return loteDataModel;
	}




	public void setLoteDataModel(LoteDataModel loteDataModel) {
		this.loteDataModel = loteDataModel;
	}




	public List<TipoMovimentacaoEnum> getTipoMovimentacaoCombo() {
		return tipoMovimentacaoCombo;
	}




	public void setTipoMovimentacaoCombo(
			List<TipoMovimentacaoEnum> tipoMovimentacaoCombo) {
		this.tipoMovimentacaoCombo = tipoMovimentacaoCombo;
	}
	
}
