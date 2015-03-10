package org.controller.model;

import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import org.entity.Movimentacao;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.service.MovimentacaoService;
import org.util.Message;


public class MovimentacaoDataModel extends LazyDataModel<Movimentacao> {
    
	private static final long serialVersionUID = 1L;

	
	@Inject
	private MovimentacaoService movimentacaoService;
	
    private List<Movimentacao> datasource;  
    
    @Inject
    private Movimentacao movimentacaoPesquisa;
 
    public MovimentacaoDataModel() {  

    }  
                
    @Override  
    public Movimentacao getRowData(String rowKey) {  
        for(Movimentacao movimentacao : datasource) {  
            if(movimentacao.getId().equals(rowKey))  
                return movimentacao;  
        }    
        return null;  
        
    }
    
    @Override  
    public Object getRowKey(Movimentacao movimentacao) {  
    	return movimentacao.getId(); 
    }

	public List<Movimentacao> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<Movimentacao> datasource) {
		this.datasource = datasource;
	}  
	  	
    @Override  
    public List<Movimentacao> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
    	try {
    		//Realiza a contagem do total de dados a serem paginados
    		this.setRowCount(movimentacaoService.obterQtdeMovimentacao(movimentacaoPesquisa));
    		
    		//Seta os dados na lista de forma paginada
    		this.setDatasource(movimentacaoService.pesquisarMovimentacao(movimentacaoPesquisa, first,pageSize));
    		
    	}catch (Exception e) {
    		Message.setMessage("br.gov.pr.celepar.exemplo.controller.model.DataModel.load.ERRO", new String[]{"alunos"}, FacesMessage.SEVERITY_ERROR);
    	}    	
		return this.getDatasource(); 
    }

	public Movimentacao getMovimentacaoPesquisa() {
		return movimentacaoPesquisa;
	}

	public void setMovimentacaoPesquisa(Movimentacao movimentacaoPesquisa) {
		this.movimentacaoPesquisa = movimentacaoPesquisa;
	}
}