package org.service;

import java.util.List;

import javax.ejb.DuplicateKeyException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.entity.StatusUsuario;
import org.entity.Usuario;

@Stateless
public class UsuarioService {
	
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	@EJB
	private UsuarioService usuarioService;
	
	public Usuario obterUsuarioPorLogin(String login){
		
		Query query = em.createNamedQuery("obterPorLogin");
		query.setParameter("login", login);
		
		return (Usuario)query.getSingleResult();
	}
	
	public Usuario obterUsuarioPorId(Integer id){
		
		return em.find(Usuario.class, id);
	}
	

	public void incluirRecuso(Usuario usuario) throws DuplicateKeyException {
		usuario.setStatusUsuario(StatusUsuario.ATIVO);
		
		if (usuarioService.loginDisponivel(usuario, true)){
			em.persist(usuario);
			
		}
		else {
			throw new DuplicateKeyException("Login já cadastrado.");
		}
		
		
	}

	public void excluirUsuario(Usuario usuario) throws Exception {
		
		//Verifica se usuário possui alguma dependencia
		
		boolean delecaoLogica = true;
		
		String stringQuery = "FROM LoteMovimentacao l Where l.usuarioFezCadastro.id = :idUsuario";
		Query query = em.createQuery(stringQuery);
		query.setParameter("idUsuario", usuario.getIdUsuario());
		
		if (query.getResultList().size() == 0){
			delecaoLogica = false;
		} 
		
		if (delecaoLogica){
			usuario.setStatusUsuario(StatusUsuario.INATIVO);
			
			em.merge(usuario);
		}
		else {
			usuario = em.find(Usuario.class, usuario.getIdUsuario());
			
			em.remove(usuario);
		}
	}

	public void alterarUsuario(Usuario usuario) throws DuplicateKeyException {
		
		if (usuarioService.loginDisponivel(usuario, false)){
			em.merge(usuario);
		}
		else {
			throw new DuplicateKeyException("Login já cadastrado.");
		}
	}
	
	public List<Usuario> listarAdvogados() {
		StringBuilder stringQuery = new StringBuilder();
		stringQuery.append("FROM Usuario u ");
		
			stringQuery.append("WHERE u.papel.nomePapel = :papelJunior OR u.papel.nomePapel = :papelPleno OR u.papel.nomePapel = :papelSenior OR  u.papel.nomePapel = :papelEstagiario ");
			
			//query.setParameter("nomeCliente", "%"+caso.getCliente().getNome()+"%");
		
		
		//TODO criar order by nome
		Query query = em.createQuery(stringQuery.toString());
		
			
			query.setParameter("papelJunior", "Advogado_Junior");
			query.setParameter("papelPleno", "Advogado_Pleno");
			query.setParameter("papelSenior", "Advogado_Senior");
			query.setParameter("papelEstagiario", "Estagiario");
		
		List<Usuario> lista = query.getResultList(); 
		
		return lista;
	}
	
	
	public List<Usuario> pesquisarUsuario(Usuario usuario) {
		
		StringBuilder stringQuery = new StringBuilder();
		stringQuery.append("FROM Usuario u ");
		stringQuery.append(" WHERE u.statusUsuario = :status ");
		if (usuario != null && usuario.getNome()!=null && !"".equals(usuario.getNome())){
			stringQuery.append("AND lower (u.nome) like :nome ");
			
			//query.setParameter("nomeCliente", "%"+caso.getCliente().getNome()+"%");
		}
		
		
		//TODO criar order by nome
		Query query = em.createQuery(stringQuery.toString());
		
		if (usuario != null && usuario.getNome()!=null && !"".equals(usuario.getNome())){
			
			query.setParameter("nome", "%"+usuario.getNome().toLowerCase()+"%");
		}
		query.setParameter("status", StatusUsuario.ATIVO);
		
		
		
		List<Usuario> lista = query.getResultList(); 
		
		return lista;
		
	}
	
	public boolean loginDisponivel(Usuario usuario, boolean incluir){
		if (incluir){
			Query query = em.createQuery("FROM Usuario u WHERE u.loginUsuario = :login");
			
			query.setParameter("login", usuario.getLoginUsuario());
			
			if (query.getResultList().size() > 0){
				return false;
			}
			else {
				return true;
			}
			
		}
		else {
			Query query = em.createQuery("FROM Usuario u WHERE u.loginUsuario = :login AND u.idUsuario <> :id");
			
			query.setParameter("login", usuario.getLoginUsuario());
			query.setParameter("id", usuario.getIdUsuario());
			
			if (query.getResultList().size() > 0){
				return false;
			}
			else {
				return true;
			}
		}
	}
	
	public List<Usuario> listarSocios () {
		
		StringBuilder stringQuery = new StringBuilder();
		stringQuery.append("FROM Usuario u ");
		
		stringQuery.append("WHERE u.participacao > 0 ");
		
		//TODO criar order by nome
		Query query = em.createQuery(stringQuery.toString());
		
		List<Usuario> lista = query.getResultList(); 
		
		return lista;
		
	}
	
	
	
	
}