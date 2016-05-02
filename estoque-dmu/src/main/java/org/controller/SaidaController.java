package org.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.controller.model.MovimentacaoDataModel;
import org.entity.Campanha;
import org.entity.Familia;
import org.entity.Instituicao;
import org.entity.LoteMovimentacao;
import org.entity.Movimentacao;
import org.entity.Produto;
import org.entity.TipoMovimentacaoEnum;
import org.entity.TipoParceiroEnum;
import org.exception.ApplicationException;
import org.exception.ControllerExceptionHandler;
import org.service.CampanhaService;
import org.service.FamiliaService;
import org.service.InstituicaoService;
import org.service.MovimentacaoService;
import org.service.ProdutoService;
import org.util.Message;

@Named
@ViewAccessScoped
@ControllerExceptionHandler
public class SaidaController implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private MovimentacaoDataModel saidaDataModel;
	
	@Inject
	private Movimentacao saida;
	
	@Inject
	private MovimentacaoService saidaService;
	
	@Inject
	private CampanhaService campanhaService;
	
	@Inject
	private FamiliaService familiaService;
	
	@Inject
	private InstituicaoService instituicaoService;
	
	@Inject
	private Provider<Movimentacao> movimentacaoProvider;
	
	private List<Produto> listProdutoCombo;
	
	private List<TipoParceiroEnum> listaTipoParceiroCombo;
	
	private List<TipoParceiroEnum> listaTipoParceiroCampanhaCombo;
	
	private List<Campanha> listaCampanhaCombo;
	
	private List<Instituicao> listaInstituicaoCombo;
	
	private List<Familia> listaFamiliaCombo;
	
	private TipoParceiroEnum tipoParceiroSelecionado;
	
	private TipoParceiroEnum tipoParceiroCampanhaSelecionado;
	
	private Boolean mostrarComboCampanha;
	
	private Boolean mostrarComboInstituicao;
	
	private Boolean mostrarComboFamilia;
	
	private Boolean mostrarComboFamiliaCampanha = false;
	
	private Boolean mostrarComboInstituicaoCampanha = false;
	
	private Float valorMedioProduto;
	
	@Inject
	private ProdutoService produtoService;
	
	
	private List<Movimentacao> listaMovimentacao;
	
	//Lista de movimentacoes para serem excluidas na alteracao
	private List<Movimentacao> listaMovimentacaoParaExcluir;
	
	@Inject
	private Movimentacao saidaDadosReceptor;
	
	@Inject
	private LoteController loteController;
	
	public String iniciarPesquisaSaida(){
		
		return "/pages/movimentacao/listar_movimentacao";
	}
	
	public String iniciarIncluirMovimentacaoSaida() throws ApplicationException{
		
		saida = movimentacaoProvider.get();
		valorMedioProduto = 0f;
		//carregando combos iniciais
		listProdutoCombo = produtoService.pesquisarProduto(new Produto(), 0, 0);
		listaTipoParceiroCombo = new ArrayList<TipoParceiroEnum>(Arrays.asList(TipoParceiroEnum.values()));
		listaTipoParceiroCombo.remove(TipoParceiroEnum.PESSOA);
		listaTipoParceiroCampanhaCombo = new ArrayList<TipoParceiroEnum>();
		
		listaTipoParceiroCampanhaCombo.add(TipoParceiroEnum.FAMILIA);
		listaTipoParceiroCampanhaCombo.add(TipoParceiroEnum.INSTITUICAO);
//		
//		saida.getLoteMovimentacao().setFamiliaCampanha(new Familia());
//		saida.getLoteMovimentacao().setInstituicaoCampanha(new Instituicao());
		
		
		mostrarComboCampanha = false;
		mostrarComboFamilia = false;
		mostrarComboInstituicao = false;
		
		saida.setData(new Date());
		
		return "/pages/movimentacao/saida/editar_saida";
	}
	
	
	public String iniciarAlterarMovimentacaoSaida(LoteMovimentacao lote) throws ApplicationException{
		 
		
//		valorMedioProduto = 0f;
		
		saidaDadosReceptor = movimentacaoProvider.get();
		saidaDadosReceptor.setLoteMovimentacao(lote);
		listaMovimentacao = lote.getListMovimentacao();
		
		//carregando combos iniciais
		listProdutoCombo = produtoService.pesquisarProduto(new Produto(), 0, 0);
		listaTipoParceiroCombo = new ArrayList<TipoParceiroEnum>(Arrays.asList(TipoParceiroEnum.values()));
		listaTipoParceiroCombo.remove(TipoParceiroEnum.PESSOA);
		
		listaTipoParceiroCampanhaCombo = new ArrayList<TipoParceiroEnum>();
		
		listaTipoParceiroCampanhaCombo.add(TipoParceiroEnum.FAMILIA);
		listaTipoParceiroCampanhaCombo.add(TipoParceiroEnum.INSTITUICAO);
//		
//		saida.getLoteMovimentacao().setFamiliaCampanha(new Familia());
//		saida.getLoteMovimentacao().setInstituicaoCampanha(new Instituicao());
		
		if (saidaDadosReceptor.getLoteMovimentacao().getCampanha() != null){
			tipoParceiroSelecionado = TipoParceiroEnum.CAMPANHA;
			mostrarComboCampanha = true;
			mostrarComboInstituicao = false;
			mostrarComboFamilia = false;
			mostrarComboFamiliaCampanha = false;
			
			if(listaCampanhaCombo == null || listaCampanhaCombo.size() == 0){
				listaCampanhaCombo = campanhaService.pesquisarCampanha(new Campanha());
			}
			
			
		} else if(saidaDadosReceptor.getLoteMovimentacao().getFamilia() != null){
			tipoParceiroSelecionado = TipoParceiroEnum.FAMILIA;
			mostrarComboCampanha = false;
			mostrarComboInstituicao = false;
			mostrarComboFamilia = true;
			mostrarComboFamiliaCampanha = false;
			if(listaFamiliaCombo == null || listaFamiliaCombo.size() == 0){
				listaFamiliaCombo = familiaService.pesquisarFamilia(new Familia());
			}
		} else if(saidaDadosReceptor.getLoteMovimentacao().getInstituicao() != null){
			tipoParceiroSelecionado = TipoParceiroEnum.INSTITUICAO;
			mostrarComboCampanha = false;
			mostrarComboInstituicao = true;
			mostrarComboFamilia = false;
			mostrarComboFamiliaCampanha = false;
			if(listaInstituicaoCombo == null || listaInstituicaoCombo.size() == 0){
				listaInstituicaoCombo = instituicaoService.pesquisarInstituicao(new Instituicao());
			}
		} else {
			tipoParceiroSelecionado = TipoParceiroEnum.ANONIMO;
			mostrarComboCampanha = false;
			mostrarComboInstituicao = false;
			mostrarComboFamilia = false;
			mostrarComboFamiliaCampanha = false;
		}
		
		
		
		
		if (saidaDadosReceptor.getLoteMovimentacao().getFamiliaCampanha() != null){
			tipoParceiroCampanhaSelecionado = TipoParceiroEnum.FAMILIA;
			if(listaFamiliaCombo == null || listaFamiliaCombo.size() == 0){
				listaFamiliaCombo = familiaService.pesquisarFamilia(new Familia());
			}
			mostrarComboInstituicaoCampanha = false;
			mostrarComboFamiliaCampanha = true;
		} else if(saidaDadosReceptor.getLoteMovimentacao().getInstituicaoCampanha() != null){
			tipoParceiroCampanhaSelecionado = TipoParceiroEnum.INSTITUICAO;
			if(listaInstituicaoCombo == null || listaInstituicaoCombo.size() == 0){
				listaInstituicaoCombo = instituicaoService.pesquisarInstituicao(new Instituicao());
			}
			mostrarComboInstituicaoCampanha = true;
			mostrarComboFamiliaCampanha = false;
		}
			

	
//		listenerObterPrecoMedio();
		
		
		return "/pages/movimentacao/saida/editar_saida";
	}
	
	
	
	public void excluirSaida(){
		listaMovimentacao.remove(saida);
		if (saida.getId() != null){
			if (listaMovimentacaoParaExcluir == null){
				listaMovimentacaoParaExcluir = new ArrayList<Movimentacao>();
			}
			listaMovimentacaoParaExcluir.add(saida);
		}
		saida = movimentacaoProvider.get();
	}
	
	public String incluirSaida() throws ApplicationException{
			
			resolverReceptor();
			
			saidaService.incluirSaidaLote(listaMovimentacao, saidaDadosReceptor);
			Message.setMessage("controller.incluirSaida.SUCESSO");
			
		
		
		return iniciarPesquisaSaida();
	}

	private void resolverReceptor() throws ApplicationException {
		if (saidaDadosReceptor.getLoteMovimentacao().getFamiliaCampanha() != null && saidaDadosReceptor.getLoteMovimentacao().getFamiliaCampanha().getId() != null){
			saidaDadosReceptor.getLoteMovimentacao().setFamiliaCampanha(familiaService.obterFamilia(saidaDadosReceptor.getLoteMovimentacao().getFamiliaCampanha().getId()));
			
		}else {
			saidaDadosReceptor.getLoteMovimentacao().setFamiliaCampanha(null);
		}
		
		
		if (saidaDadosReceptor.getLoteMovimentacao().getFamilia() != null && saidaDadosReceptor.getLoteMovimentacao().getFamilia().getId() != null){
			saidaDadosReceptor.getLoteMovimentacao().setFamilia(familiaService.obterFamilia(saidaDadosReceptor.getLoteMovimentacao().getFamilia().getId()));
			
		}else {
			saidaDadosReceptor.getLoteMovimentacao().setFamilia(null);
		}
		
		
		
		if (saidaDadosReceptor.getLoteMovimentacao().getInstituicao() != null && saidaDadosReceptor.getLoteMovimentacao().getInstituicao().getId() != null){
			saidaDadosReceptor.getLoteMovimentacao().setInstituicao(instituicaoService.obterInstituicao(saidaDadosReceptor.getLoteMovimentacao().getInstituicao().getId()));
			
		}else {
			saidaDadosReceptor.getLoteMovimentacao().setInstituicao(null);
		}
		
		
		
		if (saidaDadosReceptor.getLoteMovimentacao().getInstituicaoCampanha() != null && saidaDadosReceptor.getLoteMovimentacao().getInstituicaoCampanha().getId() != null){
			saidaDadosReceptor.getLoteMovimentacao().setInstituicaoCampanha(instituicaoService.obterInstituicao(saidaDadosReceptor.getLoteMovimentacao().getInstituicaoCampanha().getId()));
			
		}else {
			saidaDadosReceptor.getLoteMovimentacao().setInstituicaoCampanha(null);
		}
		
		
		if (saidaDadosReceptor.getLoteMovimentacao().getCampanha() != null && saidaDadosReceptor.getLoteMovimentacao().getCampanha().getId() != null){
			saidaDadosReceptor.getLoteMovimentacao().setCampanha(campanhaService.obterCampanha(saidaDadosReceptor.getLoteMovimentacao().getCampanha().getId()));
			
		}else {
			saidaDadosReceptor.getLoteMovimentacao().setCampanha(null);
		}
	}
	
	public String alterarSaida(){
		
		try {
			
			//Alterar para o utro método de alteraçcao.
			// primeiro verifica se a data está alterada ou se o recebedor esta alterado se estiver deve fazer o forwach em cada e execetar os deois meotodos noemalmente
			
			//caso data não foi alterada excluir as movimentacoes excluidas que estao na lsita de movEscluidas e inserir as movimentacoes cujo codigo estão nullas pois são novas.
			
			resolverReceptor();
			
			saidaDadosReceptor.setTipoMovimentacaoEnum(TipoMovimentacaoEnum.SAIDA);
			
			saidaService.alterarMovimentacaoLote(listaMovimentacao, listaMovimentacaoParaExcluir , saidaDadosReceptor);
			
			Message.setMessage("controller.incluirSaida.SUCESSO");
			
			return loteController.iniciarPesquisaReceibo();
		} catch (ApplicationException e) {
			e.printStackTrace();
			Message.setMessage(e);
			return null;
		}
		
		
		
	}
	
	public void listenerAlterarTipoReceptor() throws ApplicationException{
		switch (tipoParceiroSelecionado.ordinal()) {
		case 0: {
			if(listaCampanhaCombo == null || listaCampanhaCombo.size() == 0){
				listaCampanhaCombo = campanhaService.pesquisarCampanha(new Campanha());
			}
			saidaDadosReceptor.getLoteMovimentacao().setCampanha(new Campanha());
			mostrarComboCampanha = true;
			mostrarComboFamilia = false;
			mostrarComboInstituicao = false;
			mostrarComboFamiliaCampanha = false;
			mostrarComboInstituicaoCampanha = false;
			saidaDadosReceptor.getLoteMovimentacao().setFamiliaCampanha(new Familia());
			saidaDadosReceptor.getLoteMovimentacao().setInstituicaoCampanha(new Instituicao());
			
			if(listaFamiliaCombo == null || listaFamiliaCombo.size() == 0){
				listaFamiliaCombo = familiaService.pesquisarFamilia(new Familia());
			}
			if(listaInstituicaoCombo == null || listaInstituicaoCombo.size() == 0){
				listaInstituicaoCombo = instituicaoService.pesquisarInstituicao(new Instituicao());
			}
			
			
			break;
		}
		case 1: {
			if(listaFamiliaCombo == null || listaFamiliaCombo.size() == 0){
				listaFamiliaCombo = familiaService.pesquisarFamilia(new Familia());
			}
			
			saidaDadosReceptor.getLoteMovimentacao().setFamilia(new Familia());
			mostrarComboCampanha = false;
			mostrarComboInstituicao = false;
			mostrarComboFamilia = true;
			mostrarComboFamiliaCampanha = false;
			mostrarComboInstituicaoCampanha = false;
			break;
		}
		
		case 2: {
			mostrarComboCampanha = false;
			mostrarComboFamilia = false;
			mostrarComboInstituicao = false;
			mostrarComboFamiliaCampanha = false;
			mostrarComboInstituicaoCampanha = false;
			break;
		}
		case 3: {
			if(listaInstituicaoCombo == null || listaInstituicaoCombo.size() == 0){
				listaInstituicaoCombo = instituicaoService.pesquisarInstituicao(new Instituicao());
			}
			saidaDadosReceptor.getLoteMovimentacao().setInstituicao(new Instituicao());
			
			mostrarComboCampanha = false;
			mostrarComboFamilia = false;
			mostrarComboInstituicao = true;
			mostrarComboFamiliaCampanha = false;
			mostrarComboInstituicaoCampanha = false;
			break;
		}
		
		}
	}
	
	public void listenerAlterarTipoReceptorCampanha() throws ApplicationException{
		switch (tipoParceiroCampanhaSelecionado.ordinal()) {
	
		case 1: {
			if(listaFamiliaCombo == null || listaFamiliaCombo.size() == 0){
				listaFamiliaCombo = familiaService.pesquisarFamilia(new Familia());
			}
			
			saidaDadosReceptor.getLoteMovimentacao().setFamiliaCampanha(new Familia());
			mostrarComboInstituicaoCampanha = false;
			mostrarComboFamiliaCampanha = true;
			break;
		}
		

		case 3: {
			if(listaInstituicaoCombo == null || listaInstituicaoCombo.size() == 0){
				listaInstituicaoCombo = instituicaoService.pesquisarInstituicao(new Instituicao());
			}
			saidaDadosReceptor.getLoteMovimentacao().setInstituicaoCampanha(new Instituicao());
			
			mostrarComboInstituicaoCampanha = true;
			mostrarComboFamiliaCampanha = false;
			break;
		}
		
		}
	}
	
	
	public void adicionarSaidaLote() throws ApplicationException{
		if (listaMovimentacao == null){
			listaMovimentacao = new ArrayList<Movimentacao>();
		}
		
		
//		saida.setValor(NumeroUtil.multiplicarDinheiro(valorUnitarioController, entrada.getQuantidade(), 3));
		
		saida.setProduto(produtoService.obterProduto(saida.getProduto().getId()));
		
		listaMovimentacao.add(this.saida);
		
		saida = movimentacaoProvider.get();
		
//		prepararIncluirSaida();
		
	}
	
	public void prepararIncluirSaida() throws ApplicationException {
		saida = movimentacaoProvider.get();
		valorMedioProduto = 0f;
		//carregando combos iniciais
		listProdutoCombo = produtoService.pesquisarProduto(new Produto(), 0, 0);
		
		saida.getLoteMovimentacao().setData(new Date());
	}
	
	
	
//	public void listenerObterPrecoMedio(){
//		saida.setProduto(produtoService.obterProduto(saida.getProduto().getId()));
//		valorMedioProduto = (saida.getProduto().valorMedioProduto());
//	}
	
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

	public List<Familia> getListaFamiliaCombo() {
		return listaFamiliaCombo;
	}

	public void setListaFamiliaCombo(List<Familia> listaFamiliaCombo) {
		this.listaFamiliaCombo = listaFamiliaCombo;
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

	public Boolean getMostrarComboFamilia() {
		return mostrarComboFamilia;
	}

	public void setMostrarComboFamilia(Boolean mostrarComboFamilia) {
		this.mostrarComboFamilia = mostrarComboFamilia;
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

	public Boolean getMostrarComboFamiliaCampanha() {
		return mostrarComboFamiliaCampanha;
	}

	public void setMostrarComboFamiliaCampanha(Boolean mostrarComboFamiliaCampanha) {
		this.mostrarComboFamiliaCampanha = mostrarComboFamiliaCampanha;
	}

	public Movimentacao getSaida() {
		return saida;
	}

	public void setSaida(Movimentacao saida) {
		this.saida = saida;
	}

	public Boolean getMostrarComboInstituicaoCampanha() {
		return mostrarComboInstituicaoCampanha;
	}

	public void setMostrarComboInstituicaoCampanha(
			Boolean mostrarComboInstituicaoCampanha) {
		this.mostrarComboInstituicaoCampanha = mostrarComboInstituicaoCampanha;
	}

	public TipoParceiroEnum getTipoParceiroCampanhaSelecionado() {
		return tipoParceiroCampanhaSelecionado;
	}

	public void setTipoParceiroCampanhaSelecionado(
			TipoParceiroEnum tipoParceiroCampanhaSelecionado) {
		this.tipoParceiroCampanhaSelecionado = tipoParceiroCampanhaSelecionado;
	}

	public List<TipoParceiroEnum> getListaTipoParceiroCampanhaCombo() {
		return listaTipoParceiroCampanhaCombo;
	}

	public void setListaTipoParceiroCampanhaCombo(
			List<TipoParceiroEnum> listaTipoParceiroCampanhaCombo) {
		this.listaTipoParceiroCampanhaCombo = listaTipoParceiroCampanhaCombo;
	}

	public List<Movimentacao> getListaMovimentacao() {
		return listaMovimentacao;
	}

	public void setListaMovimentacao(List<Movimentacao> listaMovimentacao) {
		this.listaMovimentacao = listaMovimentacao;
	}

	public Movimentacao getSaidaDadosReceptor() {
		return saidaDadosReceptor;
	}

	public void setSaidaDadosReceptor(Movimentacao saidaDadosReceptor) {
		this.saidaDadosReceptor = saidaDadosReceptor;
	}

	public List<Movimentacao> getListaMovimentacaoParaExcluir() {
		return listaMovimentacaoParaExcluir;
	}

	public void setListaMovimentacaoParaExcluir(
			List<Movimentacao> listaMovimentacaoParaExcluir) {
		this.listaMovimentacaoParaExcluir = listaMovimentacaoParaExcluir;
	}

}
