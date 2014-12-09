package org.controller.model;

import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import org.entity.Saida;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.service.SaidaService;
import org.util.Message;


public class SaidaDataModel extends LazyDataModel<Saida> {
    
	private static final long serialVersionUID = 1L;

	
	@Inject
	private SaidaService saidaService;
	
    private List<Saida> datasource;  
    
    @Inject
    private Saida saidaPesquisa;
 
    public SaidaDataModel() {  

    }  
                
    @Override  
    public Saida getRowData(String rowKey) {  
        for(Saida saida : datasource) {  
            if(saida.getId().equals(rowKey))  
                return saida;  
        }    
        return null;  
        
    }
    
    @Override  
    public Object getRowKey(Saida saida) {  
    	return saida.getId(); 
    }

	public List<Saida> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<Saida> datasource) {
		this.datasource = datasource;
	}  
	  	
    @Override  
    public List<Saida> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
    	try {
    		//Realiza a contagem do total de dados a serem paginados
    		this.setRowCount(saidaService.obterQtdeSaida(saidaPesquisa));
    		
    		//Seta os dados na lista de forma paginada
    		this.setDatasource(saidaService.pesquisarSaida(saidaPesquisa, first,pageSize));
    		
    	}catch (Exception e) {
    		Message.setMessage("br.gov.pr.celepar.exemplo.controller.model.DataModel.load.ERRO", new String[]{"alunos"}, FacesMessage.SEVERITY_ERROR);
    	}    	
		return this.getDatasource(); 
    }

	public Saida getSaidaPesquisa() {
		return saidaPesquisa;
	}

	public void setSaidaPesquisa(Saida saidaPesquisa) {
		this.saidaPesquisa = saidaPesquisa;
	}
}