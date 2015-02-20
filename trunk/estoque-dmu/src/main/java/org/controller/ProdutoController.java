package org.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.controller.model.ProdutoDataModel;
import org.entity.Produto;
import org.entity.TipoMedidaEnum;
import org.entity.TipoProdutoEnum;
import org.exception.ControllerExceptionHandler;
import org.service.ProdutoService;
import org.util.Message;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class ProdutoController implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ProdutoDataModel produtoDataModel;
	
	@Inject
	private Produto produto;
	
	@Inject
	private ProdutoService produtoService;
	
	private List<TipoMedidaEnum> listTipoMedida;
	
	private List<TipoProdutoEnum> listTipoProduto;
	
	@Inject
	private Provider<Produto> produtoProvider;
	
	
	public String iniciarPesquisaProduto(){
		
		return "/pages/produto/listar_produto";
	}
	
	public String iniciarIncluirProduto(){
		
		listTipoMedida = new ArrayList<TipoMedidaEnum>(Arrays.asList(TipoMedidaEnum.values()));
		listTipoProduto = new ArrayList<TipoProdutoEnum>(Arrays.asList(TipoProdutoEnum.values()));
		
		produto = produtoProvider.get();
		
		return "/pages/produto/editar_produto";
	}
	
	public String excluirProduto(){
		
		return null;
	}
	
	public String incluirProduto(){
		try {
			produtoService.incluirProduto(produto);
			Message.setMessage("SUCESSO");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return iniciarPesquisaProduto();
	}
	
	public String emitirRelatorioEntradaProduto(){
		
		
		
		return null;
	}
	
	public String alterarProduto(){
		return "/pages/produto/listar_produto";
	}

	public ProdutoDataModel getProdutoDataModel() {
		return produtoDataModel;
	}

	public void setProdutoDataModel(ProdutoDataModel produtoDataModel) {
		this.produtoDataModel = produtoDataModel;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<TipoMedidaEnum> getListTipoMedida() {
		return listTipoMedida;
	}

	public void setListTipoMedida(List<TipoMedidaEnum> listTipoMedida) {
		this.listTipoMedida = listTipoMedida;
	}

	public List<TipoProdutoEnum> getListTipoProduto() {
		return listTipoProduto;
	}

	public void setListTipoProduto(List<TipoProdutoEnum> listTipoProduto) {
		this.listTipoProduto = listTipoProduto;
	}
}
