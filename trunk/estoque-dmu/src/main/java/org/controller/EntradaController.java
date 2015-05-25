package org.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.entity.Campanha;
import org.entity.Doador;
import org.entity.Instituicao;
import org.entity.Movimentacao;
import org.entity.Produto;
import org.entity.TipoParceiroEnum;
import org.exception.ApplicationException;
import org.exception.ControllerExceptionHandler;
import org.service.CampanhaService;
import org.service.DoadorService;
import org.service.FamiliaService;
import org.service.InstituicaoService;
import org.service.MovimentacaoService;
import org.service.ProdutoService;
import org.util.Message;
import org.util.NumeroUtil;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class EntradaController implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Movimentacao entrada;
	
	@Inject
	private MovimentacaoService entradaService;
	
	@Inject
	private CampanhaService campanhaService;
	
	@Inject
	private DoadorService doadorService;
	
	@Inject
	private InstituicaoService instituicaoService;
	
	@Inject
	private Provider<Movimentacao> movimentacaoProvider;
	
	@EJB
	private FamiliaService familiaService;
	
	private List<Produto> listProdutoCombo;
	
	private List<TipoParceiroEnum> listaTipoParceiroCombo;
	
	private List<Campanha> listaCampanhaCombo;
	
	private List<Instituicao> listaInstituicaoCombo;
	
	private List<Doador> listaDoadorCombo;
	
	private TipoParceiroEnum tipoParceiroSelecionado;
	
	private Boolean mostrarComboCampanha;
	
	private Boolean mostrarComboInstituicao;
	
	private Boolean mostrarComboDoador;
	
	private Float valorMedioProduto;
	
	//usado para definir o valor unitario...
	private Float valorUnitarioController;
	
	@EJB
	private ProdutoService produtoService;
	
	
	public String iniciarPesquisaEntrada(){
		
		return "/pages/movimentacao/listar_movimentacao";
	}
	
	public String iniciarIncluirEntrada() throws ApplicationException{
		
		entrada = movimentacaoProvider.get();
		valorMedioProduto = 0f;
		//carregando combos iniciais
		listProdutoCombo = produtoService.pesquisarProduto(new Produto(), 0, 0);
		listaTipoParceiroCombo = new ArrayList<TipoParceiroEnum>(Arrays.asList(TipoParceiroEnum.values()));
//		listaTipoParceiroCombo.add(TipoParceiroEnum.ANONIMO);
//		listaTipoParceiroCombo.add(TipoParceiroEnum.INSTITUICAO);
//		listaTipoParceiroCombo.add(TipoParceiroEnum.PESSOA);
		
		
		listaTipoParceiroCombo.remove(1);
		listaTipoParceiroCombo.remove(0);
		
		mostrarComboCampanha = false;
		mostrarComboDoador = false;
		mostrarComboInstituicao = false;
		
		entrada.getLoteMovimentacao().setData(new Date());
		
		return "/pages/movimentacao/entrada/editar_entrada";
	}
	
	
	public String iniciarAlterarEntrada() throws ApplicationException{
		
		listenerObterPrecoMedio();
		//carregando combos iniciais
		listProdutoCombo = produtoService.pesquisarProduto(new Produto(), 0, 0);
		listaTipoParceiroCombo = new ArrayList<TipoParceiroEnum>(Arrays.asList(TipoParceiroEnum.values()));
		listaTipoParceiroCombo.remove(1);
		listaTipoParceiroCombo.remove(0);
		valorUnitarioController = NumeroUtil.DividirDinheiro(entrada.getValor(), entrada.getQuantidade(), 3);
		
		if (entrada.getLoteMovimentacao().getCampanha() != null){
			if(listaCampanhaCombo == null || listaCampanhaCombo.size() == 0){
				listaCampanhaCombo = campanhaService.pesquisarCampanha(new Campanha());
			}
			tipoParceiroSelecionado = TipoParceiroEnum.CAMPANHA;
			mostrarComboCampanha = true;
			mostrarComboDoador = false;
			mostrarComboInstituicao = false;
			
		} else if(entrada.getLoteMovimentacao().getInstituicao() != null){
			if(listaInstituicaoCombo == null || listaInstituicaoCombo.size() == 0){
				listaInstituicaoCombo = instituicaoService.pesquisarInstituicao(new Instituicao());
			}
			tipoParceiroSelecionado = TipoParceiroEnum.INSTITUICAO;
			mostrarComboCampanha = false;
			mostrarComboDoador = false;
			mostrarComboInstituicao = true;
		} else if(entrada.getLoteMovimentacao().getDoador() != null){
			if(listaDoadorCombo == null || listaDoadorCombo.size() == 0){
				listaDoadorCombo = doadorService.pesquisarDoador(new Doador());
			}
			tipoParceiroSelecionado = TipoParceiroEnum.PESSOA;
			mostrarComboCampanha = false;
			mostrarComboDoador = true;
			mostrarComboInstituicao = false;
		} else {
			tipoParceiroSelecionado = TipoParceiroEnum.ANONIMO;
			mostrarComboCampanha = false;
			mostrarComboDoador = false;
			mostrarComboInstituicao = false;
		}
		
		return "/pages/movimentacao/entrada/editar_entrada";
	}
	
	
	
	public String excluirEntrada(){
		
		return null;
	}
	
	public String incluirEntrada(){
		try {
			
			entrada.setValor(NumeroUtil.multiplicarDinheiro(valorUnitarioController, entrada.getQuantidade(), 3));
			
			if (entrada.getLoteMovimentacao().getFamiliaCampanha() != null && entrada.getLoteMovimentacao().getFamiliaCampanha().getId() != null){
				entrada.getLoteMovimentacao().setFamiliaCampanha(familiaService.obterFamilia(entrada.getLoteMovimentacao().getFamiliaCampanha().getId()));
				
			}else {
				entrada.getLoteMovimentacao().setFamiliaCampanha(null);
			}
			if (entrada.getLoteMovimentacao().getFamilia() != null && entrada.getLoteMovimentacao().getFamilia().getId() != null){
				entrada.getLoteMovimentacao().setFamilia(familiaService.obterFamilia(entrada.getLoteMovimentacao().getFamilia().getId()));
				
			}else {
				entrada.getLoteMovimentacao().setFamilia(null);
			}
			
			
			if (entrada.getLoteMovimentacao().getInstituicao() != null && entrada.getLoteMovimentacao().getInstituicao().getId() != null){
				entrada.getLoteMovimentacao().setInstituicao(instituicaoService.obterInstituicao(entrada.getLoteMovimentacao().getInstituicao().getId()));
				
			}else {
				entrada.getLoteMovimentacao().setInstituicao(null);
			}
			
			
			
			if (entrada.getLoteMovimentacao().getInstituicaoCampanha() != null && entrada.getLoteMovimentacao().getInstituicaoCampanha().getId() != null){
				entrada.getLoteMovimentacao().setInstituicaoCampanha(instituicaoService.obterInstituicao(entrada.getLoteMovimentacao().getInstituicaoCampanha().getId()));
				
			}else {
				entrada.getLoteMovimentacao().setInstituicaoCampanha(null);
			}
			
			
			if (entrada.getLoteMovimentacao().getCampanha() != null && entrada.getLoteMovimentacao().getCampanha().getId() != null){
				entrada.getLoteMovimentacao().setCampanha(campanhaService.obterCampanha(entrada.getLoteMovimentacao().getCampanha().getId()));
				
			}else {
				entrada.getLoteMovimentacao().setCampanha(null);
			}
			
			
			
			entradaService.incluirEntrada(entrada);
			Message.setMessage("controller.incluirEntrada.SUCESSO");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
//		entrada = movimentacaoProvider.get();
		return iniciarPesquisaEntrada();
	}
	
	public String alterarEntrada(){
		
		
		try {
			entradaService.alterarEntrada1(entrada);
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		try {
			entradaService.alterarEntrada2(entrada);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return iniciarPesquisaEntrada();
	}
	
	public void listenerAlterarTipoDoador() throws ApplicationException{
		switch (tipoParceiroSelecionado.ordinal()) {
		case 0: {
			if(listaCampanhaCombo == null || listaCampanhaCombo.size() == 0){
				listaCampanhaCombo = campanhaService.pesquisarCampanha(new Campanha());
			}
			entrada.getLoteMovimentacao().setCampanha(new Campanha());
			entrada.getLoteMovimentacao().setInstituicao(null);
			entrada.getLoteMovimentacao().setDoador(null);
			
			mostrarComboCampanha = true;
			mostrarComboDoador = false;
			mostrarComboInstituicao = false;
			
			break;
		}
		case 4: {
			if(listaDoadorCombo == null || listaDoadorCombo.size() == 0){
				listaDoadorCombo = doadorService.pesquisarDoador(new Doador());
			}
			
			entrada.getLoteMovimentacao().setDoador(new Doador());
			entrada.getLoteMovimentacao().setInstituicao(null);
			entrada.getLoteMovimentacao().setCampanha(null);
			
			mostrarComboCampanha = false;
			mostrarComboInstituicao = false;
			mostrarComboDoador = true;
			break;
		}
		
		case 2: {
			mostrarComboCampanha = false;
			mostrarComboDoador = false;
			mostrarComboInstituicao = false;
			entrada.getLoteMovimentacao().setInstituicao(null);
			entrada.getLoteMovimentacao().setDoador(null);
			entrada.getLoteMovimentacao().setCampanha(null);
			
			break;
		}
		case 3: {
			if(listaInstituicaoCombo == null || listaInstituicaoCombo.size() == 0){
				listaInstituicaoCombo = instituicaoService.pesquisarInstituicao(new Instituicao());
			}
			entrada.getLoteMovimentacao().setInstituicao(new Instituicao());
			entrada.getLoteMovimentacao().setDoador(null);
			entrada.getLoteMovimentacao().setCampanha(null);
			
			mostrarComboCampanha = false;
			mostrarComboDoador = false;
			mostrarComboInstituicao = true;
			break;
		}
		
		}
	}
	
	public void listenerObterPrecoMedio(){
		entrada.setProduto(produtoService.obterProduto(entrada.getProduto().getId()));
		valorMedioProduto = (entrada.getProduto().valorMedioHistoricoProduto());
	}

	public List<Produto> getListProdutoCombo() {
		return listProdutoCombo;
	}

	public void setListProdutoCombo(List<Produto> listProdutoCombo) {
		this.listProdutoCombo = listProdutoCombo;
	}

	public List<TipoParceiroEnum> getListaTipoParceiroCombo() {
		return listaTipoParceiroCombo;
	}

	public void setListaTipoParceiroCombo(
			List<TipoParceiroEnum> listaTipoParceiroCombo) {
		this.listaTipoParceiroCombo = listaTipoParceiroCombo;
	}

	public List<Campanha> getListaCampanhaCombo() {
		return listaCampanhaCombo;
	}

	public void setListaCampanhaCombo(List<Campanha> listaCampanhaCombo) {
		this.listaCampanhaCombo = listaCampanhaCombo;
	}

	public List<Instituicao> getListaInstituicaoCombo() {
		return listaInstituicaoCombo;
	}

	public void setListaInstituicaoCombo(List<Instituicao> listaInstituicaoCombo) {
		this.listaInstituicaoCombo = listaInstituicaoCombo;
	}

	public List<Doador> getListaDoadorCombo() {
		return listaDoadorCombo;
	}

	public void setListaDoadorCombo(List<Doador> listaDoadorCombo) {
		this.listaDoadorCombo = listaDoadorCombo;
	}

	public Boolean getMostrarComboCampanha() {
		return mostrarComboCampanha;
	}

	public void setMostrarComboCampanha(Boolean mostrarComboCampanha) {
		this.mostrarComboCampanha = mostrarComboCampanha;
	}

	public Boolean getMostrarComboInstituicao() {
		return mostrarComboInstituicao;
	}

	public void setMostrarComboInstituicao(Boolean mostrarComboInstituicao) {
		this.mostrarComboInstituicao = mostrarComboInstituicao;
	}

	public Boolean getMostrarComboDoador() {
		return mostrarComboDoador;
	}

	public void setMostrarComboDoador(Boolean mostrarComboDoador) {
		this.mostrarComboDoador = mostrarComboDoador;
	}

	public TipoParceiroEnum getTipoParceiroSelecionado() {
		return tipoParceiroSelecionado;
	}

	public void setTipoParceiroSelecionado(TipoParceiroEnum tipoParceiroSelecionado) {
		this.tipoParceiroSelecionado = tipoParceiroSelecionado;
	}

	public Float getValorMedioProduto() {
		return valorMedioProduto;
	}

	public void setValorMedioProduto(Float valorMedioProduto) {
		this.valorMedioProduto = valorMedioProduto;
	}

	public Movimentacao getEntrada() {
		return entrada;
	}

	public void setEntrada(Movimentacao entrada) {
		this.entrada = entrada;
	}

	public Float getValorUnitarioController() {
		return valorUnitarioController;
	}

	public void setValorUnitarioController(Float valorUnitarioController) {
		this.valorUnitarioController = valorUnitarioController;
	}

}
