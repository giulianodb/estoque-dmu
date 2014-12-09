package org.controller.model;

import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import org.entity.Entrada;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.service.EntradaService;
import org.util.Message;


public class EntradaDataModel extends LazyDataModel<Entrada> {
    
	private static final long serialVersionUID = 1L;

	
	@Inject
	private EntradaService entradaService;
	
    private List<Entrada> datasource;  
    
    @Inject
    private Entrada entradaPesquisa;
 
    public EntradaDataModel() {  

    }  
                
    @Override  
    public Entrada getRowData(String rowKey) {  
        for(Entrada entrada : datasource) {  
            if(entrada.getId().equals(rowKey))  
                return entrada;  
        }    
        return null;  
        
    }
    
    @Override  
    public Object getRowKey(Entrada entrada) {  
    	return entrada.getId(); 
    }

	public List<Entrada> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<Entrada> datasource) {
		this.datasource = datasource;
	}  
	  	
    @Override  
    public List<Entrada> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
    	try {
    		//Realiza a contagem do total de dados a serem paginados
    		this.setRowCount(entradaService.obterQtdeEntrada(entradaPesquisa));
    		
    		//Seta os dados na lista de forma paginada
    		this.setDatasource(entradaService.pesquisarEntrada(entradaPesquisa, first,pageSize));
    		
    	}catch (Exception e) {
    		Message.setMessage("br.gov.pr.celepar.exemplo.controller.model.DataModel.load.ERRO", new String[]{"alunos"}, FacesMessage.SEVERITY_ERROR);
    	}    	
		return this.getDatasource(); 
    }

	public Entrada getEntradaPesquisa() {
		return entradaPesquisa;
	}

	public void setEntradaPesquisa(Entrada entradaPesquisa) {
		this.entradaPesquisa = entradaPesquisa;
	}
}