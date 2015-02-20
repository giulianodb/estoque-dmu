package org.controller;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.entity.Produto;
import org.exception.ControllerExceptionHandler;
import org.service.EntradaService;
import org.service.RelatorioService;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class RelatorioController implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2964921300303274340L;
	@EJB
	private RelatorioService relatorioService;
	
	public void gerarRelatorioFaturaExito(Produto produto){
		try {
			
			ByteArrayOutputStream byteArrayOutputStream  = relatorioService.relatorioEntrada(null,null,produto);
			
			FacesContext facesContext = FacesContext.getCurrentInstance();

			HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.addHeader("Content-disposition","attachment; filename=\" relatorio.pdf\"");
			
			OutputStream os = null;
			os = response.getOutputStream();
			
			byteArrayOutputStream.writeTo(os);  
			os.flush();  
			os.close();  
			byteArrayOutputStream.close();  
			
			

			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void relatorioEstoqueSintetico(){
		try {
			
			ByteArrayOutputStream byteArrayOutputStream  = relatorioService.relatorioEstoqueSintetico(new Date(2012, 12, 1), new Date(2015, 12, 1));
			
			FacesContext facesContext = FacesContext.getCurrentInstance();

			HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.addHeader("Content-disposition","attachment; filename=\" relatorio.pdf\"");
			
			OutputStream os = null;
			os = response.getOutputStream();
			
			byteArrayOutputStream.writeTo(os);  
			os.flush();  
			os.close();  
			byteArrayOutputStream.close();  
			
			

			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void relatorioEstoqueAnalitico(){
		try {
			
			ByteArrayOutputStream byteArrayOutputStream  = relatorioService.relatorioEstoqueSintetico(new Date(2012, 12, 1), new Date(2015, 12, 1));
			
			FacesContext facesContext = FacesContext.getCurrentInstance();

			HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.addHeader("Content-disposition","attachment; filename=\" relatorio.pdf\"");
			
			OutputStream os = null;
			os = response.getOutputStream();
			
			byteArrayOutputStream.writeTo(os);  
			os.flush();  
			os.close();  
			byteArrayOutputStream.close();  
			
			

			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}
