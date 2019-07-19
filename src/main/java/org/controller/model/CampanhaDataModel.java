package org.controller.model;

import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import org.entity.Campanha;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.service.CampanhaService;
import org.util.Message;


public class CampanhaDataModel extends LazyDataModel<Campanha> {
    
	private static final long serialVersionUID = 1L;

	
	@Inject
	private CampanhaService campanhaService;
	
    private List<Campanha> datasource;  
    
    @Inject
    private Campanha campanhaPesquisa;
 
    public CampanhaDataModel() {  

    }  
                
    @Override  
    public Campanha getRowData(String rowKey) {  
        for(Campanha campanha : datasource) {  
            if(campanha.getId().equals(rowKey))  
                return campanha;  
        }    
        return null;  
        
    }
    
    @Override  
    public Object getRowKey(Campanha campanha) {  
    	return campanha.getId(); 
    }

	public List<Campanha> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<Campanha> datasource) {
		this.datasource = datasource;
	}  
	  	
    @Override  
    public List<Campanha> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
    	try {
    		//Realiza a contagem do total de dados a serem paginados
    		this.setRowCount(campanhaService.obterQtdeCampanha(campanhaPesquisa));
    		
    		//Seta os dados na lista de forma paginada
    		this.setDatasource(campanhaService.pesquisarCampanha(campanhaPesquisa, first,pageSize));
    		
    	}catch (Exception e) {
    		Message.setMessage("br.gov.pr.celepar.exemplo.controller.model.DataModel.load.ERRO", new String[]{"alunos"}, FacesMessage.SEVERITY_ERROR);
    	}    	
		return this.getDatasource(); 
    }

	public Campanha getCampanhaPesquisa() {
		return campanhaPesquisa;
	}

	public void setCampanhaPesquisa(Campanha campanhaPesquisa) {
		this.campanhaPesquisa = campanhaPesquisa;
	}
}