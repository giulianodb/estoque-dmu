package org.controller.model;

import javax.inject.Inject;
import javax.inject.Named;

import org.entity.Usuario;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;


public class UsuarioDataModel extends LazyDataModel<Usuario> {
    
	private static final long serialVersionUID = 1L;

	
	/*
	@Inject
	private EscolaBusiness escolaBusiness;
	
    private List<Usuario> datasource;  

    public AlunoDataModel() {  

    }  
                
    @Override  
    public Usuario getRowData(String rowKey) {  
        for(Usuario aluno : datasource) {  
            if(aluno.getIdAluno().equals(rowKey))  
                return aluno;  
        }    
        return null;  
    }
  
    @Override  
    public Object getRowKey(Usuario aluno) {  
    	return aluno.getIdAluno(); 
    }

	public List<Usuario> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<Usuario> datasource) {
		this.datasource = datasource;
	}  
	  	
    @Override  
    public List<Usuario> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
    	try {
    		//Realiza a contagem do total de dados a serem paginados
    		this.setRowCount(escolaBusiness.obterQtdAlunos());
    		
    		//Seta os dados na lista de forma paginada
    		this.setDatasource(escolaBusiness.listarAlunos(first,pageSize));
    		
    	} catch (ApplicationException appEx) {
    		log.debug(appEx.getMessage(), appEx);
    		Message.setMessage(appEx);
    	} catch (Exception e) {
    		log.error(e.getMessage(), e);
    		Message.setMessage("br.gov.pr.celepar.exemplo.controller.model.DataModel.load.ERRO", new String[]{"alunos"}, FacesMessage.SEVERITY_ERROR);
    	}    	
		return this.getDatasource(); 
    }
    */
}