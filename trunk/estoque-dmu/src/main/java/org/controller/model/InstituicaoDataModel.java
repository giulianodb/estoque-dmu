package org.controller.model;

import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import org.entity.Instituicao;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.service.InstituicaoService;
import org.util.Message;


public class InstituicaoDataModel extends LazyDataModel<Instituicao> {
    
	private static final long serialVersionUID = 1L;

	
	@Inject
	private InstituicaoService instituicaoService;
	
    private List<Instituicao> datasource;  
    
    @Inject
    private Instituicao instituicaoPesquisa;
 
    public InstituicaoDataModel() {  

    }  
                
    @Override  
    public Instituicao getRowData(String rowKey) {  
        for(Instituicao instituicao : datasource) {  
            if(instituicao.getId().equals(rowKey))  
                return instituicao;  
        }    
        return null;  
        
    }
    
    @Override  
    public Object getRowKey(Instituicao instituicao) {  
    	return instituicao.getId(); 
    }

	public List<Instituicao> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<Instituicao> datasource) {
		this.datasource = datasource;
	}  
	  	
    @Override  
    public List<Instituicao> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
    	try {
    		//Realiza a contagem do total de dados a serem paginados
    		this.setRowCount(instituicaoService.obterQtdeInstituicao(instituicaoPesquisa));
    		
    		//Seta os dados na lista de forma paginada
    		this.setDatasource(instituicaoService.pesquisarInstituicao(instituicaoPesquisa, first,pageSize));
    		
    	}catch (Exception e) {
    		Message.setMessage("br.gov.pr.celepar.exemplo.controller.model.DataModel.load.ERRO", new String[]{"alunos"}, FacesMessage.SEVERITY_ERROR);
    	}    	
		return this.getDatasource(); 
    }

	public Instituicao getInstituicaoPesquisa() {
		return instituicaoPesquisa;
	}

	public void setInstituicaoPesquisa(Instituicao instituicaoPesquisa) {
		this.instituicaoPesquisa = instituicaoPesquisa;
	}
}