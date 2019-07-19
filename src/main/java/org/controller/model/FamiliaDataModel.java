package org.controller.model;

import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import org.entity.Familia;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.service.FamiliaService;
import org.util.Message;


public class FamiliaDataModel extends LazyDataModel<Familia> {
    
	private static final long serialVersionUID = 1L;

	
	@Inject
	private FamiliaService familiaService;
	
    private List<Familia> datasource;  
    
    @Inject
    private Familia familiaPesquisa;
 
    public FamiliaDataModel() {  

    }  
                
    @Override  
    public Familia getRowData(String rowKey) {  
        for(Familia familia : datasource) {  
            if(familia.getId().equals(rowKey))  
                return familia;  
        }    
        return null;  
        
    }
    
    @Override  
    public Object getRowKey(Familia familia) {  
    	return familia.getId(); 
    }

	public List<Familia> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<Familia> datasource) {
		this.datasource = datasource;
	}  
	  	
    @Override  
    public List<Familia> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
    	try {
    		//Realiza a contagem do total de dados a serem paginados
    		this.setRowCount(familiaService.obterQtdeFamilia(familiaPesquisa));
    		
    		//Seta os dados na lista de forma paginada
    		this.setDatasource(familiaService.pesquisarFamilia(familiaPesquisa, first,pageSize));
    		
    	}catch (Exception e) {
    		Message.setMessage("br.gov.pr.celepar.exemplo.controller.model.DataModel.load.ERRO", new String[]{"alunos"}, FacesMessage.SEVERITY_ERROR);
    	}    	
		return this.getDatasource(); 
    }

	public Familia getFamiliaPesquisa() {
		return familiaPesquisa;
	}

	public void setFamiliaPesquisa(Familia familiaPesquisa) {
		this.familiaPesquisa = familiaPesquisa;
	}
}