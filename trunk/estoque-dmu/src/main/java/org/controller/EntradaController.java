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
import org.entity.LoteMovimentacao;
import org.entity.Movimentacao;
import org.entity.Produto;
import org.entity.TipoMovimentacaoEnum;
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
	private Movimentacao entradaDadosDoador;
	
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
	
	private String descricaoQuantidade;
	
	//usado para definir o valor unitario...
	private Float valorUnitarioController;
	
	@EJB
	private ProdutoService produtoService;
	
	private List<Movimentacao> listaMovimentacao;
	
	private List<Movimentacao> listaMovimentacaoParaExcluir;
	@Inject
	private LoteController loteController;
	
	
	public String iniciarPesquisaEntrada(){
		
		return "/pages/movimentacao/listar_movimentacao";
	}
	
	public String iniciarIncluirEntrada() throws ApplicationException{
		
		prepararIncluirEntrada();
		
		return "/pages/movimentacao/entrada/editar_entrada";
	}

	public void prepararIncluirEntrada() throws ApplicationException {
		entrada = movimentacaoProvider.get();
		valorMedioProduto = 0f;
		descricaoQuantidade = "";
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
	}
	
	
	public String iniciarAlterarEntrada(LoteMovimentacao lote) throws ApplicationException{
		
		
		entradaDadosDoador = movimentacaoProvider.get();
		entradaDadosDoador.setLoteMovimentacao(lote);
		listaMovimentacao = lote.getListMovimentacao();
		
		//carregando combos iniciais
		listProdutoCombo = produtoService.pesquisarProduto(new Produto(), 0, 0);
		listaTipoParceiroCombo = new ArrayList<TipoParceiroEnum>(Arrays.asList(TipoParceiroEnum.values()));
		listaTipoParceiroCombo.remove(1);
		listaTipoParceiroCombo.remove(0);
//		valorUnitarioController = NumeroUtil.DividirDinheiro(entrada.getValor(), entrada.getQuantidade(), 3);
		
		if (entradaDadosDoador.getLoteMovimentacao().getCampanha() != null){
			if(listaCampanhaCombo == null || listaCampanhaCombo.size() == 0){
				listaCampanhaCombo = campanhaService.pesquisarCampanha(new Campanha());
			}
			tipoParceiroSelecionado = TipoParceiroEnum.CAMPANHA;
			mostrarComboCampanha = true;
			mostrarComboDoador = false;
			mostrarComboInstituicao = false;
			
		} else if(entradaDadosDoador.getLoteMovimentacao().getInstituicao() != null){
			if(listaInstituicaoCombo == null || listaInstituicaoCombo.size() == 0){
				listaInstituicaoCombo = instituicaoService.pesquisarInstituicao(new Instituicao());
			}
			tipoParceiroSelecionado = TipoParceiroEnum.INSTITUICAO;
			mostrarComboCampanha = false;
			mostrarComboDoador = false;
			mostrarComboInstituicao = true;
		} else if(entradaDadosDoador.getLoteMovimentacao().getDoador() != null){
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
	
	
	
	public void excluirEntrada(){
		listaMovimentacao.remove(entrada);
		
		if (entrada.getId() != null){
			if (listaMovimentacaoParaExcluir == null){
				listaMovimentacaoParaExcluir = new ArrayList<Movimentacao>();
			}
			listaMovimentacaoParaExcluir.add(entrada);
		}
		entrada = movimentacaoProvider.get();
		
	}
	
	public String incluirEntrada(){
		try {
			
			resolverDoador();
			
//			entradaService.incluirEntrada(entrada);
			
			entradaService.incluirEntradaLote(listaMovimentacao, entradaDadosDoador);
			Message.setMessage("controller.incluirEntrada.SUCESSO");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
//		entrada = movimentacaoProvider.get();
		return iniciarPesquisaEntrada();
	}

	private void resolverDoador() throws ApplicationException {
		if (entradaDadosDoador.getLoteMovimentacao().getFamiliaCampanha() != null && entradaDadosDoador.getLoteMovimentacao().getFamiliaCampanha().getId() != null){
			entradaDadosDoador.getLoteMovimentacao().setFamiliaCampanha(familiaService.obterFamilia(entradaDadosDoador.getLoteMovimentacao().getFamiliaCampanha().getId()));
			
		}else {
			entradaDadosDoador.getLoteMovimentacao().setFamiliaCampanha(null);
		}
		if (entradaDadosDoador.getLoteMovimentacao().getFamilia() != null && entradaDadosDoador.getLoteMovimentacao().getFamilia().getId() != null){
			entradaDadosDoador.getLoteMovimentacao().setFamilia(familiaService.obterFamilia(entradaDadosDoador.getLoteMovimentacao().getFamilia().getId()));
			
		}else {
			entradaDadosDoador.getLoteMovimentacao().setFamilia(null);
		}
		
		
		if (entradaDadosDoador.getLoteMovimentacao().getInstituicao() != null && entradaDadosDoador.getLoteMovimentacao().getInstituicao().getId() != null){
			entradaDadosDoador.getLoteMovimentacao().setInstituicao(instituicaoService.obterInstituicao(entradaDadosDoador.getLoteMovimentacao().getInstituicao().getId()));
			
		}else {
			entradaDadosDoador.getLoteMovimentacao().setInstituicao(null);
		}
		
		
		
		if (entradaDadosDoador.getLoteMovimentacao().getInstituicaoCampanha() != null && entradaDadosDoador.getLoteMovimentacao().getInstituicaoCampanha().getId() != null){
			entradaDadosDoador.getLoteMovimentacao().setInstituicaoCampanha(instituicaoService.obterInstituicao(entradaDadosDoador.getLoteMovimentacao().getInstituicaoCampanha().getId()));
			
		}else {
			entradaDadosDoador.getLoteMovimentacao().setInstituicaoCampanha(null);
		}
		
		
		if (entradaDadosDoador.getLoteMovimentacao().getCampanha() != null && entradaDadosDoador.getLoteMovimentacao().getCampanha().getId() != null){
			entradaDadosDoador.getLoteMovimentacao().setCampanha(campanhaService.obterCampanha(entradaDadosDoador.getLoteMovimentacao().getCampanha().getId()));
			
		}else {
			entradaDadosDoador.getLoteMovimentacao().setCampanha(null);
		}
	}
	
	public String alterarEntrada(){
		
		
		try {
			resolverDoador();
			
			entradaDadosDoador.setTipoMovimentacaoEnum(TipoMovimentacaoEnum.ENTRADA);
			
			entradaService.alterarMovimentacaoLote(listaMovimentacao, listaMovimentacaoParaExcluir , entradaDadosDoador);
			
			Message.setMessage("controller.alterarMovimentacao.SUCESSO");
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Message.setMessage(e);
			
		}
		
		
		
		/*00000000000
		
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
		
		*/
		
		return loteController.iniciarPesquisaReceibo();
	}
	
	public void listenerAlterarTipoDoador() throws ApplicationException{
		switch (tipoParceiroSelecionado.ordinal()) {
		case 0: {
			if(listaCampanhaCombo == null || listaCampanhaCombo.size() == 0){
				listaCampanhaCombo = campanhaService.pesquisarCampanha(new Campanha());
			}
			entradaDadosDoador.getLoteMovimentacao().setCampanha(new Campanha());
			entradaDadosDoador.getLoteMovimentacao().setInstituicao(null);
			entradaDadosDoador.getLoteMovimentacao().setDoador(null);
			
			mostrarComboCampanha = true;
			mostrarComboDoador = false;
			mostrarComboInstituicao = false;
			
			break;
		}
		case 4: {
			if(listaDoadorCombo == null || listaDoadorCombo.size() == 0){
				listaDoadorCombo = doadorService.pesquisarDoador(new Doador());
			}
			
			entradaDadosDoador.getLoteMovimentacao().setDoador(new Doador());
			entradaDadosDoador.getLoteMovimentacao().setInstituicao(null);
			entradaDadosDoador.getLoteMovimentacao().setCampanha(null);
			
			mostrarComboCampanha = false;
			mostrarComboInstituicao = false;
			mostrarComboDoador = true;
			break;
		}
		
		case 2: {
			mostrarComboCampanha = false;
			mostrarComboDoador = false;
			mostrarComboInstituicao = false;
			entradaDadosDoador.getLoteMovimentacao().setInstituicao(null);
			entradaDadosDoador.getLoteMovimentacao().setDoador(null);
			entradaDadosDoador.getLoteMovimentacao().setCampanha(null);
			
			break;
		}
		case 3: {
			if(listaInstituicaoCombo == null || listaInstituicaoCombo.size() == 0){
				listaInstituicaoCombo = instituicaoService.pesquisarInstituicao(new Instituicao());
			}
			entradaDadosDoador.getLoteMovimentacao().setInstituicao(new Instituicao());
			entradaDadosDoador.getLoteMovimentacao().setDoador(null);
			entradaDadosDoador.getLoteMovimentacao().setCampanha(null);
			
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
		descricaoQuantidade = entrada.getProduto().getTipoMedida().getDescricao();
	}
	
	public void adicionarEntradaLote() throws ApplicationException{
		if (listaMovimentacao == null){
			listaMovimentacao = new ArrayList<Movimentacao>();
		}
		
		
		entrada.setValor(NumeroUtil.multiplicarDinheiro(valorUnitarioController, entrada.getQuantidade(), 3));
		
		listaMovimentacao.add(this.entrada);
		
		entrada = movimentacaoProvider.get();
		
//		prepararIncluirEntrada();
		
		valorUnitarioController = 0f;
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

	public List<Movimentacao> getListaMovimentacao() {
		return listaMovimentacao;
	}

	public void setListaMovimentacao(List<Movimentacao> listaMovimentacao) {
		this.listaMovimentacao = listaMovimentacao;
	}

	public Movimentacao getEntradaDadosDoador() {
		return entradaDadosDoador;
	}

	public void setEntradaDadosDoador(Movimentacao entradaDadosDoador) {
		this.entradaDadosDoador = entradaDadosDoador;
	}

	public List<Movimentacao> getListaMovimentacaoParaExcluir() {
		return listaMovimentacaoParaExcluir;
	}

	public void setListaMovimentacaoParaExcluir(
			List<Movimentacao> listaMovimentacaoParaExcluir) {
		this.listaMovimentacaoParaExcluir = listaMovimentacaoParaExcluir;
	}

	public String getDescricaoQuantidade() {
		return descricaoQuantidade;
	}

	public void setDescricaoQuantidade(String descricaoQuantidade) {
		this.descricaoQuantidade = descricaoQuantidade;
	}

}
