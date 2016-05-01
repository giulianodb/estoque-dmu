package org.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.DuplicateKeyException;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.entity.Papel;
import org.entity.Usuario;
import org.service.PapelService;
import org.service.UsuarioService;

import filter.ControleFiltro;

@Named
@SessionScoped
public class UsuarioControl implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5317108768461608646L;

	@Inject
	private Usuario usuario;
	
	private List<Papel> listPapel;
	
	private List<Usuario> listUsuario = new ArrayList<Usuario>();
	
	@EJB
	private UsuarioService usuarioService;
	
	@EJB
	private PapelService papelService;

	private String operacao;
	

	public String iniciarListarUsuario(){
		try {
			usuario = new Usuario();
			listUsuario = usuarioService.pesquisarUsuario(usuario);
			return "/pages/usuario/listar_usuario";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro listar usuarios", ""));
			return "/pages/inicial";
		}
	}
	
	public String iniciarCadastrarUsuario(){
		usuario = new Usuario();
		operacao = "Incluir";
	
		try {
			usuario.setPapel(new Papel());
			listPapel = papelService.listaPapel();
			
		} 
		
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar cadastro de usuario", ""));
			return iniciarListarUsuario();
		}
		return "/pages/usuario/editar_usuario";
	}
	
	public String iniciarAltarerarUsuario(){
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		operacao = "Alterar";
		
		//Popular lista de papel
		listPapel = papelService.listaPapel();
		
		return "/pages/usuario/editar_usuario";
	}
	
	public String iniciarExcluirUsuario(){
		operacao = "Excluir";
		
		try {
			listPapel = papelService.listaPapel();
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar cadastro de usuario", ""));
			return pesquisarUsuario();
		}
		//Popular lista de papel
		
		return "/pages/usuario/editar_usuario";
	}
	
	public String incluirUsuario(){
		try {
			usuarioService.incluirRecuso(usuario);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso ao cadastrar usuario", ""));
			
			usuario = new Usuario();
		}
		catch (DuplicateKeyException de){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, de.getMessage(), ""));
			return "/pages/usuario/editar_usuario";
		}
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao cadastrar usuário", ""));
			
			return "/pages/usuario/editar_usuario";
		}
		
		return iniciarListarUsuario();
	}
	
	public String excluirUsuario(){
		
		try {
			usuarioService.excluirUsuario(usuario);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso ao excluir usuario", ""));
			//TODO lançar mensagem de sucesso
		} 
	
		catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir usuário "+usuario.getNome(), ""));
		}
		
		return iniciarListarUsuario();
	}
	
	public String alterarUsuario(){
		try{
			usuarioService.alterarUsuario(usuario);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso ao alterar usuário", ""));
			usuario = new Usuario();
		}
		
		catch (DuplicateKeyException de){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, de.getMessage(), ""));
			return "/pages/usuario/editar_usuario";
		}
		catch (Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao alterar usuário", ""));
		}
		
		return iniciarListarUsuario();
	}

	public String pesquisarUsuario(){
		try {
			listUsuario = usuarioService.pesquisarUsuario(usuario);
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar lista de usuário", ""));
			return "/pages/inicial";
		}
		
		return "/pages/usuario/listar_usuario"; 
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Papel> getListPapel() {
		return listPapel;
	}

	public void setListPapel(List<Papel> listPapel) {
		this.listPapel = listPapel;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}


	public PapelService getPapelService() {
		return papelService;
	}

	public void setPapelService(PapelService papelService) {
		this.papelService = papelService;
	}

	public List<Usuario> getListUsuario() {
		return listUsuario;
	}

	public void setListUsuario(List<Usuario> listUsuario) {
		this.listUsuario = listUsuario;
	}

	
}
