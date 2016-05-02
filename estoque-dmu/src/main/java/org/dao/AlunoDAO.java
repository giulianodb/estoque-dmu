package org.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.entity.Usuario;
import org.exception.ApplicationException;

/**
 * Classe de manipulação de objetos da classe Aluno.
 * @author GIC
 * @since 1.0
 * @version 1.0, 23/01/14
 */

@Stateless
public class AlunoDAO {

	@PersistenceContext(unitName = "estoque")
	private EntityManager em;

	
	/**
	 * Cadastro de aluno
	 * @param aluno
	 * @throws ApplicationException
	 */
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void incluir(Usuario usuario) throws ApplicationException {
		if (usuario == null) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.parametrosInvalidos.ERRO", new String[]{"salvar aluno"});
		}
		try {
			em.persist(usuario);
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.AlunoDAO.incluir.ERRO", e);
		}
	}

}
