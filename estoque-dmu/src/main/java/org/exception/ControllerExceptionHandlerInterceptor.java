package org.exception;


import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interceptor utilizado para tratar as exceções lançadas pela aplicação e
 * inserir a mensagem de erro no contexto do JSF.
 * 
 * @author Celepar - GIC
 * @since 1.0
 */
@ControllerExceptionHandler
@Interceptor
public class ControllerExceptionHandlerInterceptor implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 * Intercepta os métodos da classe do tipo Controller, tratando as possíveis exceções.
	 * 
	 * @param invocationContext informações do contexto
	 * @return
	 * @throws ApplicationException
	 */
	@AroundInvoke
	public Object handleException(InvocationContext invocationContext) throws Exception {
		// recupera o nome da classe
		final String targetClassName = invocationContext.getTarget().getClass().getName();
		// recupera o nome do método
		final String methodName = invocationContext.getMethod().getName();

		Logger logger = LoggerFactory.getLogger(targetClassName);
				
		logger.debug("Interceptor ControllerExceptionHandler sobre a Classe: " + targetClassName + " - Metodo: " + methodName);
		try {
			// Executa o método anotado.
			return invocationContext.proceed();
			
		} catch (ApplicationException appEx) {
			if(appEx.getRootCause() != null){
				logger.error("Erro tratado pela aplicacao: ", appEx);
			}
			else {
				logger.debug("Erro tratado pela aplicacao: ", appEx);
			}
			
			this.addMessage(appEx.getSeverity(), appEx.getMessage(), "");
			
		} catch (Exception ex) {
			logger.error("Erro NAO tratado pela aplicacao. Operacao [" + methodName + "] em [" + targetClassName + "] - " + ex.getMessage(), ex);
			this.addMessage(FacesMessage.SEVERITY_ERROR, Message.getMessage("mensagem.padrao"), ex.getMessage());
		}
		logger.debug("Fim Interceptor ControllerExceptionHandler.");
		
		return null;
	}


	/**
	 * Adiciona uma mensagem no contexto JSF
	 * @param severity
	 * @param summary
	 * @param detail
	 */
	private void addMessage(Severity severity, String summary, String detail) {
		//Recupera o contexto da aplicação
		FacesContext context = FacesContext.getCurrentInstance();
	    
		//Determina o tipo de mensagem e recupera a mensagem desejada
		FacesMessage msg = new FacesMessage(severity, summary, detail);
		
		//Seta a mensagem no contexto JSF
		context.addMessage(null, msg);
	}	
}