package org.service;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.entity.Configuracao;
import org.entity.Papel;


@Stateless
@LocalBean
public class ConfiguracaoService {
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	public List<Papel> listaPapel(){
		
		Query query = em.createNamedQuery("listarPapel");
		
		List<Papel> resultList = query.getResultList();
		
		return resultList;
		
	}
	
	
	public Configuracao obterConfigAtual(){
		
		return em.find(Configuracao.class, 1);
		
	}
	
	public void salvarConfiguracao(Configuracao config){
		em.merge(config);
		
	}
	
	public void atualizarDataLimiteMovi(Integer mes, Integer ano) {
		Configuracao config = obterConfigAtual();
		config.setAno(ano);
		config.setMes(mes);
		
		salvarConfiguracao(config);
	}
}
