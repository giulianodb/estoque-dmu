package org.view.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.message.Message;


@FacesConverter("br.gov.pr.celepar.exemplo.view.converter.CepConverter")
public class CepConverter implements Converter {
	
	/**
	 * Remove o caracter '-' (traço) da string e a transforma num Integer. 
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		//Verifica se a string contendo o CEP é válida
		if(value != null && value.length() == 9){
			value = value.replace("-", "");
			return new Integer(value);
		} else {
			//Adiciona mensagem de erro e retorna exceção
			FacesMessage msg = new FacesMessage(Message.getMessage("br.gov.pr.celepar.exemplo.view.converter.ERRO"), "");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(msg);
		}		
	}
	
	/**
	 * Transforma o Object em String e adiciona o caracter '-' (traço). 
	 */
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		//Verifica se o objeto contendo o CEP é válido
		if (value != null && value.toString().length() == 8) {
			String cep = value.toString();
			return cep.substring(0, 5) + "-" + cep.substring(5, 8);
		} else {
			//Adiciona mensagem de erro e retorna exceção
			FacesMessage msg = new FacesMessage(Message.getMessage("br.gov.pr.celepar.exemplo.view.converter.ERRO"), "");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(msg);
		}		
	}	
}