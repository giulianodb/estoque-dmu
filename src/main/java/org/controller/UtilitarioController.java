package org.controller;

import java.io.Serializable;
import java.util.Calendar;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.exception.ControllerExceptionHandler;
import org.service.ConfiguracaoService;
import org.util.ConfiguracaoAplicacao;
import org.util.Message;
import org.util.NumeroUtil;

@Named
@ViewScoped
@ControllerExceptionHandler
public class UtilitarioController implements Serializable {

	private Float gramas;

	private Float quilos;

	private Float centimetros;

	private Float metros;
	
	private Integer ano;
	
	private Integer mes;
	
	@Inject
	private ConfiguracaoAplicacao config;
	
	@Inject
	private ConfiguracaoService configSer;

	public void gramasParaQuilos() {

		quilos = NumeroUtil.DividirDinheiro(gramas, 1000f, 3);
		metros = NumeroUtil.DividirDinheiro(centimetros, 100f, 3);

	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public void quilosParaGramas() {

		gramas = NumeroUtil.multiplicarDinheiro(quilos, 1000f, 3);
		centimetros = NumeroUtil.multiplicarDinheiro(metros, 100f, 3);

	}
	
	public void paginaLimiteMovimentacao() {
		
		Calendar c = Calendar.getInstance();
		c.setTime(config.getDataMinimaMovimentacao());
		System.out.println(c.get(Calendar.DATE));
		System.out.println(c.get(Calendar.MONTH)); // janeiro é 0
		System.out.println(c.get(Calendar.YEAR));
		
		
		ano = c.get(Calendar.YEAR);
		mes = c.get(Calendar.MONTH)+1;
	}
	
	
	public void atualizarLimiteMovimentacao() {
		try {
			configSer.atualizarDataLimiteMovi(mes, ano);
			config.iniciar();
			
			Message.setMessage("controller.alterarLimiete.SUCESSO");
			
		} catch (Exception e) {
			// TODO: handle exception
			
			Message.setMessage("controller.alterarLimiete.ERRO",FacesMessage.SEVERITY_ERROR);
			
			
		}
	}
	
	public Float getGramas() {
		return gramas;
	}

	public void setGramas(Float gramas) {
		this.gramas = gramas;
	}

	public Float getQuilos() {
		return quilos;
	}

	public void setQuilos(Float quilos) {
		this.quilos = quilos;
	}

	public Float getCentimetros() {
		return centimetros;
	}

	public void setCentimetros(Float centimetros) {
		this.centimetros = centimetros;
	}

	public Float getMetros() {
		return metros;
	}

	public void setMetros(Float metros) {
		this.metros = metros;
	}

}
