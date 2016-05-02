package org.controller.model;

import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import org.entity.Produto;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.service.ProdutoService;
import org.util.Message;


public class ProdutoDataModel extends LazyDataModel<Produto> {
    
	private static final long serialVersionUID = 1L;

	
	@Inject
	private ProdutoService produtoService;
	
    private List<Produto> datasource;  
    
    @Inject
    private Produto produtoPesquisa;
 
    public ProdutoDataModel() {  

    }  
                
    @Override  
    public Produto getRowData(String rowKey) {  
        for(Produto produto : datasource) {  
            if(produto.getId().equals(rowKey))  
                return produto;  
        }    
        return null;  
        
    }
    
    @Override  
    public Object getRowKey(Produto produto) {  
    	return produto.getId(); 
    }

	public List<Produto> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<Produto> datasource) {
		this.datasource = datasource;
	}  
	  	
    @Override  
    public List<Produto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
    	try {
    		//Realiza a contagem do total de dados a serem paginados
    		this.setRowCount(produtoService.obterQtdeProduto(produtoPesquisa));
    		
    		//Seta os dados na lista de forma paginada
    		this.setDatasource(produtoService.pesquisarProduto(produtoPesquisa, first,pageSize));
    		
    	}catch (Exception e) {
    		Message.setMessage("br.gov.pr.celepar.exemplo.controller.model.DataModel.load.ERRO", new String[]{"alunos"}, FacesMessage.SEVERITY_ERROR);
    	}    	
		return this.getDatasource(); 
    }

	public Produto getProdutoPesquisa() {
		return produtoPesquisa;
	}

	public void setProdutoPesquisa(Produto produtoPesquisa) {
		this.produtoPesquisa = produtoPesquisa;
	}
}