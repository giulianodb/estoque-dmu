package org.controller;

import java.io.Serializable;

import javax.inject.Named;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.exception.ControllerExceptionHandler;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class UsuarioController implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	public String iniciarCadastrarUsuario(){
		
		return "/pages/usuario/editar_usuario";
	}
}
