package org.exception;


import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

import org.message.Message;


/**
 * Classe utilizada para encapsular exceções da aplicação.
 * 
 * @author grupo Framework - Componentes
 * @version 1.1, 29/04/2005
 * @since 1.0 
 */
@javax.ejb.ApplicationException(rollback=true)
public class ApplicationException extends Exception {
	
	/** Serial Id **/
	private static final long serialVersionUID = 2013512271265915763L;

	/**  Guarda excecao ocorrida **/
	private Throwable rootCause = null;
	
	private Severity severity = FacesMessage.SEVERITY_ERROR;
	
	/**
	 * Constrói e recupera a mensagem a partir da chave informada.
	 * 
	 * @param messageKeyLoc chave para busca da mensagem
	 */
	public ApplicationException(String messageKeyLoc) {
		super(Message.getMessage(messageKeyLoc));
	}
	
	/**
	 * Constrói e recupera a mensagem a partir da chave informada.
	 * 
	 * @param messageKeyLoc chave para busca da mensagem
	 * @param tipoIcone indica o ícone que deve ser apresentado na mensagem
	 */
	public ApplicationException(String messageKeyLoc, Severity severity) {
		super(Message.getMessage(messageKeyLoc));
		this.severity = severity;
	}
	
	/**
	 * Constrói e recupera a mensagem a partir da chave informada substituindo
	 * as chaves encontradas na mensagem pelos parâmetros passados no array.
	 * 
	 * @param messageKeyLoc chave para busca da mensagem
	 * @param parameters parâmetros que serão substituídos na mensagem
	 */
	public ApplicationException(String messageKeyLoc, String[] parameters) {
		super(Message.getMessage(messageKeyLoc, parameters));
	}
	
	/**
	 * Constrói e recupera a mensagem a partir da chave informada substituindo
	 * as chaves encontradas na mensagem pelos parâmetros passados no array.
	 * 
	 * @param messageKeyLoc chave para busca da mensagem
	 * @param parameters parâmetros que serão substituídos na mensagem
	 * @param tipoIcone indica o ícone que deve ser apresentado na mensagem
	 */
	public ApplicationException(String messageKeyLoc, String[] parameters, Severity severity) {
		super(Message.getMessage(messageKeyLoc, parameters));
		this.severity = severity;
	}
	
	/**
	 * Constrói objeto com a pilha de exceção e com mensagem padrão. 
	 * 
	 * @param causa causa raiz da exceção ocorrida
	 */
	public ApplicationException(Throwable causa) {
		super(Message.getMessage("message.default"));
		setRootCause(causa);
	}
		
	/**
	 * Constrói objeto com a pilha de exceção e recupera a 
	 * mensagem a partir da chave informada.
	 * 
	 * @param messageKeyLoc chave para a busca da mensagem
	 * @param causa causa raiz da exceção ocorrida
	 */
	public ApplicationException(String messageKeyLoc, Throwable causa) {
		super(Message.getMessage(messageKeyLoc));
		setRootCause(causa);
	}
	
	/**
	 * Constrói objeto com a pilha de exceção e recupera a 
	 * mensagem a partir do chave informada.
	 * 
	 * @param messageKeyLoc chave para a busca da mensagem
	 * @param causa causa raiz da exceção ocorrida
	 * @param tipoIcone indica o ícone que deve ser apresentado na mensagem.
	 */
	public ApplicationException(String messageKeyLoc, Throwable causa, Severity severity) {
		super(Message.getMessage(messageKeyLoc));
		setRootCause(causa);
		this.severity = severity;
	}
	
	/**
	 * Constrói objeto com a pilha de exceção e recupera a mensagem a partir 
	 * da chave informada substituindo as chaves encontradas na mensagem pelos 
	 * parâmetros passados no array.
	 * 
	 * @param messageKeyLoc chave para busca da mensagem
	 * @param parameters parâmetros que serão substituídos na mensagem
	 * @param causa causa raiz da exceção ocorrida
	 */
	public ApplicationException(String messageKeyLoc, String[] parameters, Throwable causa) {
		super(Message.getMessage(messageKeyLoc, parameters));
		setRootCause(causa);
	}

	/**
	 * Constrói objeto com a pilha de exceção e recupera a mensagem a partir 
	 * da chave informada substituindo as chaves encontradas na mensagem pelos 
	 * parâmetros passados no array.
	 * 
	 * @param messageKeyLoc chave para busca da mensagem
	 * @param parameters prâmetros que serão substituídos na mensagem
	 * @param causa causa raiz da exceção ocorrida
	 * @param tipoIcone indica o ícone que deve ser apresentado na mensagem
	 */
	public ApplicationException(String messageKeyLoc, String[] parameters, Throwable causa, Severity severity) {
		super(Message.getMessage(messageKeyLoc, parameters));
		setRootCause(causa);
		this.severity = severity;
	}
	
	/**
	 * @return causa raiz
	 */
	public Throwable getRootCause() {
		return rootCause;
	}
	
	/**
	 * @param causaRaiz causa raiz da exceção ocorrida
	 */
	public void setRootCause(Throwable rootCause) {
		this.rootCause = rootCause;
	}
	
	/**
	 * @return severity - indica o ícone que deve ser apresentado na mensagem
	 */
	public Severity getSeverity() {
		return severity;
	}

	
}