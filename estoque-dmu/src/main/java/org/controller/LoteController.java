package org.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.controller.model.LoteDataModel;
import org.controller.model.MovimentacaoDataModel;
import org.entity.Movimentacao;
import org.entity.TipoMovimentacaoEnum;
import org.entity.TipoParceiroEnum;
import org.exception.ControllerExceptionHandler;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class LoteController implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Movimentacao movimentacao;
	
	@Inject
	private LoteDataModel loteDataModel;
	
	private List<TipoMovimentacaoEnum> tipoMovimentacaoCombo;
	
	
	public String iniciarPesquisaReceibo(){
		
		if (tipoMovimentacaoCombo == null){
			tipoMovimentacaoCombo = new ArrayList<TipoMovimentacaoEnum>(Arrays.asList(TipoMovimentacaoEnum.values()));
		}
		
		return "/pages/movimentacao/listar_recibo";
	}
	
	
	
	
	public String excluirEntrada(){
		
		return null;
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
