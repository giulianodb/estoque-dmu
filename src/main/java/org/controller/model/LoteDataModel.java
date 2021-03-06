package org.controller.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import org.entity.LoteMovimentacao;
import org.entity.TipoMovimentacaoEnum;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.service.LoteService;
import org.util.Message;


public class LoteDataModel extends LazyDataModel<LoteMovimentacao> {
    
	private static final long serialVersionUID = 1L;

	
	@Inject
	private LoteService loteService;
	
    private List<LoteMovimentacao> datasource;  
    
    @Inject
    private LoteMovimentacao lotePesquisa;
    
    
   private Date dataInicioPesquisa;
   
   private Date dataFimPesquisa;
   
   private Integer numeroReciboPesquisa;
   
   private String nomePessoaInstituicaoPesquisa;
   
   public Integer getNumeroReciboPesquisa() {
	return numeroReciboPesquisa;
}

public void setNumeroReciboPesquisa(Integer numeroReciboPesquisa) {
	this.numeroReciboPesquisa = numeroReciboPesquisa;
}

private TipoMovimentacaoEnum tipoMovimentacaoPesquisa;
 
    public LoteDataModel() {  

    }  
                
    @Override  
    public LoteMovimentacao getRowData(String rowKey) {  
        for(LoteMovimentacao lote : datasource) {  
            if(lote.getCodigo().equals(rowKey))  
                return lote;  
        }    
        return null;  
        
    }
    
    @Override  
    public Object getRowKey(LoteMovimentacao lote) {  
    	return lote.getCodigo(); 
    }

	  	
    @Override  
    public List<LoteMovimentacao> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
    	try {
    		//Realiza a contagem do total de dados a serem paginados
//    		loteService.teste(lotePesquisa, dataInicioPesquisa, dataFimPesquisa, tipoMovimentacaoPesquisa, numeroReciboPesquisa,nomePessoaInstituicaoPesquisa);
    		this.setRowCount(loteService.obterQtdeLote(lotePesquisa, dataInicioPesquisa, dataFimPesquisa, tipoMovimentacaoPesquisa, numeroReciboPesquisa,nomePessoaInstituicaoPesquisa));
    		
    		//Seta os dados na lista de forma paginada
    		this.setDatasource(loteService.pesquisarLote(lotePesquisa,dataInicioPesquisa, dataFimPesquisa, tipoMovimentacaoPesquisa,numeroReciboPesquisa, first,pageSize,nomePessoaInstituicaoPesquisa));
    		
    	}catch (Exception e) {
    		e.printStackTrace();
    		Message.setMessage("br.gov.pr.celepar.exemplo.controller.model.DataModel.load.ERRO", new String[]{"alunos"}, FacesMessage.SEVERITY_ERROR);
    	}    	
		return this.getDatasource(); 
    }

	public LoteService getLoteService() {
		return loteService;
	}

	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}

	public List<LoteMovimentacao> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<LoteMovimentacao> datasource) {
		this.datasource = datasource;
	}

	public LoteMovimentacao getLotePesquisa() {
		return lotePesquisa;
	}

	public void setLotePesquisa(LoteMovimentacao lotePesquisa) {
		this.lotePesquisa = lotePesquisa;
	}

	public Date getDataInicioPesquisa() {
		return dataInicioPesquisa;
	}

	public void setDataInicioPesquisa(Date dataInicioPesquisa) {
		this.dataInicioPesquisa = dataInicioPesquisa;
	}

	public Date getDataFimPesquisa() {
		return dataFimPesquisa;
	}

	public void setDataFimPesquisa(Date dataFimPesquisa) {
		this.dataFimPesquisa = dataFimPesquisa;
	}

	public TipoMovimentacaoEnum getTipoMovimentacaoPesquisa() {
		return tipoMovimentacaoPesquisa;
	}

	public void setTipoMovimentacaoPesquisa(
			TipoMovimentacaoEnum tipoMovimentacaoPesquisa) {
		this.tipoMovimentacaoPesquisa = tipoMovimentacaoPesquisa;
	}

	public String getNomePessoaInstituicaoPesquisa() {
		return nomePessoaInstituicaoPesquisa;
	}

	public void setNomePessoaInstituicaoPesquisa(String nomePessoaInstituicaoPesquisa) {
		this.nomePessoaInstituicaoPesquisa = nomePessoaInstituicaoPesquisa;
	}



}