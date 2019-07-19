package org.controller.model;

import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import org.entity.Doador;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.service.DoadorService;
import org.util.Message;


public class DoadorDataModel extends LazyDataModel<Doador> {
    
	private static final long serialVersionUID = 1L;

	
	@Inject
	private DoadorService doadorService;
	
    private List<Doador> datasource;  
    
    @Inject
    private Doador doadorPesquisa;
 
    public DoadorDataModel() {  

    }  
                
    @Override  
    public Doador getRowData(String rowKey) {  
        for(Doador doador : datasource) {  
            if(doador.getId().equals(rowKey))  
                return doador;  
        }    
        return null;  
        
    }
    
    @Override  
    public Object getRowKey(Doador doador) {  
    	return doador.getId(); 
    }

	public List<Doador> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<Doador> datasource) {
		this.datasource = datasource;
	}  
	  	
    @Override  
    public List<Doador> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
    	try {
    		//Realiza a contagem do total de dados a serem paginados
    		this.setRowCount(doadorService.obterQtdeDoador(doadorPesquisa));
    		
    		//Seta os dados na lista de forma paginada
    		this.setDatasource(doadorService.pesquisarDoador(doadorPesquisa, first,pageSize));
    		
    	}catch (Exception e) {
    		Message.setMessage("br.gov.pr.celepar.exemplo.controller.model.DataModel.load.ERRO", new String[]{"alunos"}, FacesMessage.SEVERITY_ERROR);
    	}    	
		return this.getDatasource(); 
    }

	public Doador getDoadorPesquisa() {
		return doadorPesquisa;
	}

	public void setDoadorPesquisa(Doador doadorPesquisa) {
		this.doadorPesquisa = doadorPesquisa;
	}
}