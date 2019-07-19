package org.util;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.entity.Configuracao;
import org.service.ConfiguracaoService;

@ApplicationScoped
public class ConfiguracaoAplicacao implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Date dataMinimaMovimentacao;
	
	@EJB
	@Inject
	private ConfiguracaoService configService;
	
	private Date dataUltimaMovimentacao;
	
	@PostConstruct
	public void iniciar(){
		
		Configuracao config = configService.obterConfigAtual();
		
		LocalDate data1 = LocalDate.of(   config.getAno(),config.getMes() ,1);
		
//		data1 = data1.minusDays(1);
//		data1 = data1.plusMonths(1);
		data1 = LocalDate.of(   config.getAno(),config.getMes() , 1);
		
		data1 = data1.with(TemporalAdjusters.lastDayOfMonth());
		
		dataMinimaMovimentacao = Date.from(data1.atStartOfDay(ZoneId.systemDefault()).toInstant());
		dataMinimaMovimentacao.setHours(23);
		dataMinimaMovimentacao.setMinutes(59);
		dataMinimaMovimentacao.setSeconds(59);
		
		dataUltimaMovimentacao = config.getDataUltimaMovimentacao();
	}
	

	public Date getDataMinimaMovimentacao() {
		return dataMinimaMovimentacao;
	}


	public void setDataMinimaMovimentacao(Date dataMinimaMovimentacao) {
		this.dataMinimaMovimentacao = dataMinimaMovimentacao;
	}




	public Date getDataUltimaMovimentacao() {
		return dataUltimaMovimentacao;
	}




	public void setDataUltimaMovimentacao(Date dataUltimaMovimentacao) {
		this.dataUltimaMovimentacao = dataUltimaMovimentacao;
	}
}
