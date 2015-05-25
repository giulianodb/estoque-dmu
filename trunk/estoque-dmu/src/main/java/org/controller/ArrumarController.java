package org.controller;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.entity.Movimentacao;
import org.exception.ControllerExceptionHandler;
import org.service.ArrumarService;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class ArrumarController implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4771467384987833914L;
	@EJB
	private ArrumarService arrumarService;
	public String teste(){
		try {
			
//			arrumarService.teste3();
			
			int total = 500;
			
			for (int i = 0; i < total; i++) {
				if (i == 24){
					System.out.println("AQUI CAGOU...");
				}
				List<Movimentacao> lista = arrumarService.teste(i);
				arrumarService.zerarSaldos(i);
				
				arrumarService.teste2(lista);
				System.out.println("FInalizou id:" +i);
			}
			
			
			System.out.println("SUCESSSOO");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return null;
	}
}
