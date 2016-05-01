package org.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.entity.Campanha;
import org.entity.Familia;
import org.entity.Instituicao;
import org.entity.LoteMovimentacao;
import org.entity.Movimentacao;
import org.entity.Produto;
import org.entity.TipoMovimentacaoEnum;
import org.entity.TipoParceiroEnum;
import org.exception.ApplicationException;
import org.jfree.data.DataUtilities;
import org.util.DateUtil;
import org.util.NumeroUtil;
import org.util.StringUtil;

import com.arjuna.ats.internal.jdbc.drivers.modifiers.list;

import converter.MoedaConverter;

@Stateless
public class MovimentacaoService {
	
	@PersistenceContext(unitName = "estoque")
	private EntityManager em;
	
	@Inject
	private ProdutoService produtoService;
	
	@EJB
	private LoteService loteService;
	
	@EJB
	private MovimentacaoService movimentacaoService;
	
	public List<Movimentacao> pesquisarMovimentacao(Movimentacao movimentacao, Integer primeiroRegistro, Integer tamanhoPagina) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT movimentacao FROM Movimentacao movimentacao ");	
			sb.append(" LEFT JOIN FETCH movimentacao.loteMovimentacao lote ");
			sb.append(" LEFT JOIN FETCH lote.campanha campanha  ");
			sb.append(" LEFT JOIN FETCH lote.instituicao instituicao  ");
//			sb.append(" LEFT JOIN FETCH lote.produto produto  ");
			
			if (movimentacao.getProduto() != null && movimentacao.getProduto().getNomeSemAcento()!=null && !"".equals( movimentacao.getProduto().getNomeSemAcento())){
				movimentacao.getProduto().setNomeSemAcento(StringUtil.trata(movimentacao.getProduto().getNomeSemAcento()));
				sb.append(" WHERE (movimentacao.produto.nomeSemAcento) like :nome ");
			}
			
			sb.append("ORDER BY movimentacao.data DESC ");
			
			TypedQuery<Movimentacao> query = em.createQuery(sb.toString(),Movimentacao.class);
			
			if (movimentacao.getProduto() != null && movimentacao.getProduto().getNomeSemAcento()!=null && !"".equals( movimentacao.getProduto().getNomeSemAcento())){
				query.setParameter("nome", "%"+movimentacao.getProduto().getNomeSemAcento()+"%");
			}
			
			//Delimita o num de registro para a pagina a ser recuperada
	        if (primeiroRegistro != null){
	        	query.setFirstResult(primeiroRegistro);
	        }
	        if (tamanhoPagina != null){
	        	query.setMaxResults(tamanhoPagina);				
	        }
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	public List<Movimentacao> pesquisarMovimentacao(Produto produto, Date dataInicial, Date dataFinal, int primeiroRegistro, int tamanhoPagina) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT movimentacao FROM Movimentacao movimentacao ");	
			sb.append(" LEFT JOIN FETCH movimentacao.produto produto ");
			sb.append(" LEFT JOIN FETCH movimentacao.loteMovimentacao lote ");
			sb.append(" LEFT JOIN FETCH lote.campanha campanha  ");
			sb.append(" LEFT JOIN FETCH lote.instituicao instituicao  ");
			
			if (produto.getId() != null){
				sb.append(" WHERE produto.id = :id ");
			}
			
			TypedQuery<Movimentacao> query = em.createQuery(sb.toString(),Movimentacao.class);
			
			if (produto.getId() != null){
				query.setParameter("id",produto.getId());
			}
			
			
			sb.append("ORDER BY movimentacao.nome ");
			
			//Delimita o num de registro para a pagina a ser recuperada
//	        query.setFirstResult(primeiroRegistro);
//	        query.setMaxResults(tamanhoPagina);				
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	

	public Movimentacao pesquisarUltimaMovimentacao(Produto produto, Date data) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT movimentacao FROM Movimentacao movimentacao ");	
			sb.append(" LEFT JOIN FETCH movimentacao.produto produto ");
			sb.append(" LEFT JOIN FETCH movimentacao.loteMovimentacao lote ");
			sb.append(" LEFT JOIN FETCH lote.campanha campanha  ");
			sb.append(" LEFT JOIN FETCH lote.instituicao instituicao  ");
			
			if (produto.getId() != null){
				sb.append(" WHERE produto.id = :id ");
			}
			
			if (data != null){
				sb.append("AND movimentacao.data < :data ");
			}
			
			sb.append("ORDER BY movimentacao.id DESC ");
			
			TypedQuery<Movimentacao> query = em.createQuery(sb.toString(),Movimentacao.class);
			
			if (produto.getId() != null){
				query.setParameter("id",produto.getId());
			}
			
			if (data != null){
				query.setParameter("data",data);
			}
			
			
		
			
			//Delimita o num de registro para a pagina a ser recuperada
//	        query.setFirstResult(primeiroRegistro);
//	        query.setMaxResults(tamanhoPagina);
			
			List<Movimentacao> lista = query.getResultList();
			
			if (lista.size() > 0){
				return lista.get(0);
			}
			else {
				return null;
			}
			
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	
	public List<Movimentacao> pesquisarMovimentacaoPorCampanha(Campanha campanha, Date dataInicial, Date dataFinal) throws ApplicationException{
		try {
			
			dataInicial = DateUtil.adicionarHoraInicio(dataInicial);
			dataFinal = DateUtil.adicionarHoraFim(dataFinal);
			
			StringBuilder sb = new StringBuilder("SELECT movimentacao FROM Movimentacao movimentacao ");	
			sb.append(" LEFT JOIN FETCH movimentacao.produto produto ");
			sb.append(" LEFT JOIN FETCH movimentacao.loteMovimentacao lote ");
			sb.append(" LEFT JOIN FETCH lote.campanha campanha  ");
			
			if (campanha != null && campanha.getId() != null && campanha.getId() != 0){
				sb.append(" WHERE campanha.id = :id AND ");
			}
			else {
				sb.append(" WHERE campanha.id <> NULL  AND ");
			}
			
			
			if(dataInicial != null){
				sb.append(" movimentacao.data >= :dataInicial AND ");
			}
			
			if(dataFinal != null){
				sb.append(" movimentacao.data >= :dataFinal AND ");
			}
			
			sb.append(" 1 = 1 ");
			
			TypedQuery<Movimentacao> query = em.createQuery(sb.toString(),Movimentacao.class);
			
			if (campanha != null && campanha.getId() != null && campanha.getId() != 0){
				query.setParameter("id",campanha.getId());
			}
			if(dataInicial != null){
				query.setParameter("dataInicial",dataInicial);
			}
			
			if(dataFinal != null){
				query.setParameter("dataFinal",dataFinal);
			}
			
			
			sb.append("ORDER BY movimentacao.nome ");
			
			//Delimita o num de registro para a pagina a ser recuperada
//	        query.setFirstResult(primeiroRegistro);
//	        query.setMaxResults(tamanhoPagina);				
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	public List<Movimentacao> pesquisarMovimentacaoPorFamilia(Familia familia, Date dataInicial, Date dataFinal, TipoMovimentacaoEnum tipoMovimentacao) throws ApplicationException{
		try {
			
			dataInicial = DateUtil.adicionarHoraInicio(dataInicial);
			dataFinal = DateUtil.adicionarHoraFim(dataFinal);
			
			StringBuilder sb = new StringBuilder("SELECT movimentacao FROM Movimentacao movimentacao ");	
			sb.append(" LEFT JOIN FETCH movimentacao.produto produto ");
			sb.append(" LEFT JOIN FETCH movimentacao.loteMovimentacao lote ");
			sb.append(" LEFT JOIN FETCH lote.familia familia  ");
//			sb.append(" LEFT JOIN FETCH lote.familiaCampanha familiaCampanha  ");
			
			if (familia != null && familia.getId() != null && familia.getId() != 0){
				sb.append(" WHERE familia.id = :id AND ");
//				sb.append(" WHERE familia.id = :id AND ");
			}
			else {
				sb.append(" WHERE familia.id <> NULL  AND ");
			}
			
			if(dataInicial != null){
				sb.append(" movimentacao.data >= :dataInicial AND ");
			}
			
			if(dataFinal != null){
				sb.append(" movimentacao.data <= :dataFinal AND ");
			}
			
			if (tipoMovimentacao != null){
				sb.append(" movimentacao.tipoMovimentacaoEnum = :tipoMovimentacao AND ");
			}
			
			sb.append(" 1 = 1 ");
			
			TypedQuery<Movimentacao> query = em.createQuery(sb.toString(),Movimentacao.class);
			
			if (familia != null && familia.getId() != null  && familia.getId() != 0){
				query.setParameter("id",familia.getId());
			}
			if(dataInicial != null){
				query.setParameter("dataInicial",dataInicial);
			}
			
			if(dataFinal != null){
				query.setParameter("dataFinal",dataFinal);
			}
			
			if (tipoMovimentacao != null){
				query.setParameter("tipoMovimentacao",tipoMovimentacao);
			}
			sb.append("ORDER BY movimentacao.nome ");
			
			//Delimita o num de registro para a pagina a ser recuperada
//	        query.setFirstResult(primeiroRegistro);
//	        query.setMaxResults(tamanhoPagina);				
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	public List<Movimentacao> pesquisarMovimentacaoPorInstituicao(Instituicao inst, Date dataInicial, Date dataFinal,TipoMovimentacaoEnum tipoMovimentacao) throws ApplicationException{
		try {
			dataInicial = DateUtil.adicionarHoraInicio(dataInicial);
			dataFinal = DateUtil.adicionarHoraFim(dataFinal);
			
			StringBuilder sb = new StringBuilder("SELECT DISTINCT movimentacao FROM Movimentacao movimentacao ");	
			sb.append(" LEFT JOIN FETCH movimentacao.produto produto ");
			sb.append(" LEFT JOIN FETCH movimentacao.loteMovimentacao lote ");
			sb.append(" LEFT JOIN FETCH lote.instituicao inst  ");
//			sb.append(" LEFT JOIN FETCH lote.instituicaoCampanha instituicaoCampanha  ");
			
			if (inst != null && inst.getId() != null && inst.getId() != 0){
				sb.append(" WHERE inst.id = :id  AND ");
//				sb.append(" WHERE familia.id = :id AND ");
			}
			else {
				sb.append(" WHERE inst.id <> NULL  AND ");
			}
			
			if(dataInicial != null){
				sb.append(" movimentacao.data >= :dataInicial AND ");
			}
			
			if(dataFinal != null){
				sb.append(" movimentacao.data <= :dataFinal AND ");
			}
			
			if (tipoMovimentacao != null){
				sb.append(" movimentacao.tipoMovimentacaoEnum = :tipoMovimentacao AND ");
			}
			
			sb.append(" 1 = 1 ");
			
			sb.append(" ORDER BY movimentacao.id ");
			
			TypedQuery<Movimentacao> query = em.createQuery(sb.toString(),Movimentacao.class);
			
			if (inst != null && inst.getId() != null  && inst.getId() != 0){
				query.setParameter("id",inst.getId());
			}
			if(dataInicial != null){
				query.setParameter("dataInicial",dataInicial);
			}
			
			if(dataFinal != null){
				query.setParameter("dataFinal",dataFinal);
			}
			
			if (tipoMovimentacao != null){
				query.setParameter("tipoMovimentacao",tipoMovimentacao);
			}
			
			
			sb.append("ORDER BY movimentacao.nome ");
			
			//Delimita o num de registro para a pagina a ser recuperada
//	        query.setFirstResult(primeiroRegistro);
//	        query.setMaxResults(tamanhoPagina);				
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	public List<Movimentacao> pesquisarMovimentacaoPorAnonimo(Date dataInicial, Date dataFinal,TipoMovimentacaoEnum tipoMovimentacao) throws ApplicationException{
		try {
			dataInicial = DateUtil.adicionarHoraInicio(dataInicial);
			dataFinal = DateUtil.adicionarHoraFim(dataFinal);
			
			StringBuilder sb = new StringBuilder("SELECT DISTINCT movimentacao FROM Movimentacao movimentacao ");	
			sb.append(" LEFT JOIN FETCH movimentacao.produto produto ");
			sb.append(" LEFT JOIN FETCH movimentacao.loteMovimentacao lote ");
			sb.append(" LEFT JOIN FETCH lote.instituicao inst  ");
			sb.append(" LEFT JOIN FETCH lote.familia familia  ");
			sb.append(" LEFT JOIN FETCH lote.campanha campanha  ");
			
			sb.append(" WHERE ");
			
			if(dataInicial != null){
				sb.append(" movimentacao.data >= :dataInicial AND ");
			}
			
			if(dataFinal != null){
				sb.append(" movimentacao.data <= :dataFinal AND ");
			}
			
			if (tipoMovimentacao != null){
				sb.append(" movimentacao.tipoMovimentacaoEnum = :tipoMovimentacao AND ");
			}
			
			sb.append(" (inst.id = NULL AND familia.id = NULL AND campanha.id = NULL) ");
			
			sb.append(" ORDER BY movimentacao.id ");
			
			TypedQuery<Movimentacao> query = em.createQuery(sb.toString(),Movimentacao.class);
			
			if(dataInicial != null){
				query.setParameter("dataInicial",dataInicial);
			}
			
			if(dataFinal != null){
				query.setParameter("dataFinal",dataFinal);
			}
			
			if (tipoMovimentacao != null){
				query.setParameter("tipoMovimentacao",tipoMovimentacao);
			}
			
			
			//Delimita o num de registro para a pagina a ser recuperada
//	        query.setFirstResult(primeiroRegistro);
//	        query.setMaxResults(tamanhoPagina);				
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	
	// lista movimentacao apartir de uma data
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Movimentacao> pesquisarMovimentacao(Produto produto, Date dataPesquisa) throws ApplicationException{
		try {
			StringBuilder sb = new StringBuilder("SELECT movimentacao FROM Movimentacao movimentacao ");	
			sb.append(" LEFT JOIN FETCH movimentacao.produto produto ");
			
				sb.append(" WHERE produto.id = :id ");
				sb.append(" AND movimentacao.data > :dataPesquisa ");
				sb.append("ORDER BY movimentacao.data ASC ");
			TypedQuery<Movimentacao> query = em.createQuery(sb.toString(),Movimentacao.class);
			
				query.setParameter("id",produto.getId());
				query.setParameter("dataPesquisa", dataPesquisa);
			
		
			
			//Delimita o num de registro para a pagina a ser recuperada
//	        query.setFirstResult(primeiroRegistro);
//	        query.setMaxResults(tamanhoPagina);				
			return query.getResultList();
		} catch(Exception e) {
			throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.listarPorAlunoComRelacionamentos.ERRO", e);
		}
		
	}
	
	
	public Integer obterQtdeMovimentacao(Movimentacao movimentacao) throws ApplicationException{
			
			try {
				StringBuilder sb = new StringBuilder("SELECT COUNT(movimentacao) FROM Movimentacao movimentacao ");
//				sb.append(" LEFT JOIN FETCH movimentacao.produto produto ");
//				sb.append(" LEFT JOIN FETCH movimentacao.loteMovimentacao lote ");
//				sb.append(" LEFT JOIN FETCH lote.campanha campanha  ");
				if (movimentacao.getProduto() != null && movimentacao.getProduto().getNomeSemAcento()!=null && !"".equals( movimentacao.getProduto().getNomeSemAcento())){
					movimentacao.getProduto().setNomeSemAcento(StringUtil.trata(movimentacao.getProduto().getNomeSemAcento()));
					sb.append(" WHERE (movimentacao.produto.nomeSemAcento) like :nome ");
				}
				
				Query query = em.createQuery(sb.toString());
				
				if (movimentacao.getProduto() != null && movimentacao.getProduto().getNomeSemAcento()!=null && !"".equals( movimentacao.getProduto().getNomeSemAcento())){
					query.setParameter("nome", "%"+movimentacao.getProduto().getNomeSemAcento()+"%");
				}
				
				Long x = (Long) query.getSingleResult();		
				return Integer.valueOf(x.intValue());
				
			} catch(Exception e) {
				throw new ApplicationException("br.gov.pr.celepar.exemplo.dao.MatriculaDAO.obterQtdPorAluno.ERRO", e);
			}	
		}
	
	/**
	 * Faaz o cadastro com uma lsita de movimentacções
	 * @param entrada
	 * @throws ApplicationException
	 */
	public void incluirEntradaLote(List<Movimentacao> entradas, Movimentacao dados) throws ApplicationException{
		
		for (Movimentacao movimentacao : entradas) {
			movimentacao.setData(dados.getLoteMovimentacao().getData());
			movimentacao.setLoteMovimentacao(dados.getLoteMovimentacao());
			
			movimentacaoService.incluirEntrada(movimentacao);
		}
		
	}
	
	
	/**
	 * Faaz o cadastro com uma lsita de movimentacções
	 * @param entrada
	 * @throws ApplicationException
	 */
	public void incluirSaidaLote(List<Movimentacao> saidas, Movimentacao dados) throws ApplicationException{
		
		for (Movimentacao movimentacao : saidas) {
			movimentacao.setData(dados.getLoteMovimentacao().getData());
			movimentacao.setLoteMovimentacao(dados.getLoteMovimentacao());
			
			movimentacaoService.incluirSaida(movimentacao);
		}
		
	}
	
	
	public void incluirEntrada(Movimentacao entrada) throws ApplicationException{
		entrada.setTipoMovimentacaoEnum(TipoMovimentacaoEnum.ENTRADA);
		String numeroEntrada = extrairNumeroLote(entrada);
		
		entrada.setData(DateUtil.adicionarHoraInicio(entrada.getData()));
		
		LoteMovimentacao loteMovimentacao = loteService.obterLoteMovimentacaoById(numeroEntrada);
		
		if (loteMovimentacao == null){
			loteMovimentacao = entrada.getLoteMovimentacao();
			loteMovimentacao.setNumeroEntrada(numeroEntrada);
			
			loteService.incluirLote(loteMovimentacao);
		}
		else {
			entrada.setLoteMovimentacao(loteMovimentacao);
		}
		
		//Precisa agrupar todas as entradas da mesma pessoa e da mesma data em um único "pacote" ou "lote" para gerar um relatório.
		
		//criamos um atributo ou uma classe que agrupe essas informacoes? Classe lote id baseado em data - codigo doador - codigo tipo doador e essa classe poderia conter as informacoes comuns
		
		
		Produto produto = produtoService.obterProduto(entrada.getProduto().getId());
		
		//TIRAR TODO
		
		
		if (produto.getId().equals(21)){
			System.out.println("AQUIIIIIIIIIIIIIIIIII");
		}
		
		
		
		//Verifica se o produto não possui movimentacoes posteriores, caso não existir realiza padrão, porém se existir deve "andar" os valores médios históricos
		
		List<Movimentacao> movimentacoesPosterioes =  movimentacaoService.pesquisarMovimentacao(produto, entrada.getData());
		
		arrumarMovimentacoesPosteriores(entrada, produto, movimentacoesPosterioes);
		
		//atualizando valores para historico e média 
		produto.setQuantidadeEstoque(NumeroUtil.somarDinheiro(produto.getQuantidadeEstoque(), entrada.getQuantidade(), 10));
		produto.setQuantidadeHistoricaTotal(NumeroUtil.somarDinheiro(produto.getQuantidadeHistoricaTotal(), entrada.getQuantidade(), 10));
		produto.setValorHistoricoTotal(NumeroUtil.somarDinheiro(produto.getValorHistoricoTotal(), entrada.getValor(), 10));
		produto.setSaldoEstoque(NumeroUtil.somarDinheiro(produto.getSaldoEstoque(), entrada.getValor(), 10));
		
		produtoService.alterarProduto(produto);
		
		em.persist(entrada);
	}


	private void arrumarMovimentacoesPosteriores(Movimentacao movimentacao, Produto produto, List<Movimentacao> movimentacoesPosterioes) throws ApplicationException {
		
		
		if (movimentacoesPosterioes == null || movimentacoesPosterioes.size() == 0){
			
			if (movimentacao.getTipoMovimentacaoEnum().equals(TipoMovimentacaoEnum.SAIDA)){
				movimentacao.setValor(NumeroUtil.multiplicarDinheiro(produto.valorMedioAtualProduto(), movimentacao.getQuantidade(), 10));
			}
			
			if (produto.getQuantidadeEstoque() > 0){
				movimentacao.setValorMediaUltimo(NumeroUtil.DividirDinheiro(produto.getSaldoEstoque(), produto.getQuantidadeEstoque(), 10));
			}
			else {
				movimentacao.setValorMediaUltimo(0f);
			}
			movimentacao.setQuantidadeUltimo(produto.getQuantidadeEstoque());
			movimentacao.setSaldoUltimo(produto.getSaldoEstoque());
			
		}
		else{
			
			movimentacao.setValorMediaUltimo(movimentacoesPosterioes.get(0).getValorMediaUltimo());
			movimentacao.setQuantidadeUltimo(movimentacoesPosterioes.get(0).getQuantidadeUltimo());
			movimentacao.setSaldoUltimo(movimentacoesPosterioes.get(0).getSaldoUltimo());
			
			
			Float quantidadeMovimentacao = movimentacao.getQuantidade();
			Float valorMovimentacao = movimentacao.getValor();
			if (movimentacao.getTipoMovimentacaoEnum().equals(TipoMovimentacaoEnum.SAIDA)){
				quantidadeMovimentacao = NumeroUtil.multiplicarDinheiro(quantidadeMovimentacao, -1f, 3);
				movimentacao.setValor(NumeroUtil.multiplicarDinheiro(movimentacoesPosterioes.get(0).getValorMediaUltimo(), movimentacao.getQuantidade(), 3));
				valorMovimentacao = NumeroUtil.multiplicarDinheiro(movimentacao.getValor(), -1f, 3);
				
			}
			
			for (int i = 0; i < movimentacoesPosterioes.size(); i++) {
//				if (i == 0){                                                       
//					movimentacoesPosterioes.get(i).setQuantidadeUltimo(NumeroUtil.somarDinheiro(movimentacao.getQuantidadeUltimo(), quantidadeMovimentacao, 3));
//					movimentacoesPosterioes.get(i).setSaldoUltimo(NumeroUtil.somarDinheiro(movimentacao.getSaldoUltimo(), valorMovimentacao, 3));
//					movimentacoesPosterioes.get(i).setValorMediaUltimo(NumeroUtil.DividirDinheiro(movimentacoesPosterioes.get(i).getSaldoUltimo(), movimentacoesPosterioes.get(i).getQuantidadeUltimo(), 3));
//				}
//				else {
					movimentacoesPosterioes.get(i).setQuantidadeUltimo(NumeroUtil.somarDinheiro(movimentacoesPosterioes.get(i).getQuantidadeUltimo(), quantidadeMovimentacao, 3));
					movimentacoesPosterioes.get(i).setSaldoUltimo(NumeroUtil.somarDinheiro(movimentacoesPosterioes.get(i).getSaldoUltimo(), valorMovimentacao, 3));
					if (movimentacoesPosterioes.get(i).getQuantidadeUltimo() > 0){
						movimentacoesPosterioes.get(i).setValorMediaUltimo(NumeroUtil.DividirDinheiro(movimentacoesPosterioes.get(i).getSaldoUltimo(), movimentacoesPosterioes.get(i).getQuantidadeUltimo(), 3));
					}
					else{
						movimentacoesPosterioes.get(i).setValorMediaUltimo(0f);
					}
					
//				}
					
					
					if (movimentacoesPosterioes.get(i).getQuantidadeUltimo() < 0){
						//lançar exption..
						System.out.println("FICOU COM SALDO NEGATIVO........");
						//estava comentado porém ACHO que precisa ficar descomentado.. assim que encontrar erro de saldo deve lançar erro!!!! Assim evita que no historico saldo fique negativo
//						throw new ApplicationException("service.movimentacao.saldoNegativo.ERRO",new String[]{DateUtil.dataToString(movimentacoesPosterioes.get(i).getData())});
						
						
						
						throw new ApplicationException("service.movimentacao.saldoNegativo.ERRO", new String[]{movimentacoesPosterioes.get(i).getQuantidadeUltimo().toString(),produto.getNome(),DateUtil.dataToString(movimentacoesPosterioes.get(i).getData())});
					}
				
				em.merge(movimentacoesPosterioes.get(i));
			}
			
		}
	}


	private String extrairNumeroLote(Movimentacao movimentacao) {
		//alterando valores do produto
		
		//Definindo o código do numero de entrada que será como um numero do recibo
		String numeroEntrada = DateUtil.dataToString(movimentacao.getLoteMovimentacao().getData(), "yyMMdd");
		
		Calendar myCal = new GregorianCalendar();
		myCal.setTime(movimentacao.getLoteMovimentacao().getData());
		myCal.set(Calendar.HOUR, Calendar.getInstance().get(Calendar.HOUR));
		myCal.set(Calendar.MINUTE, Calendar.getInstance().get(Calendar.MINUTE));
		myCal.set(Calendar.SECOND, Calendar.getInstance().get(Calendar.SECOND));
		
		movimentacao.getLoteMovimentacao().setData(myCal.getTime());
		movimentacao.setData(myCal.getTime());
		
		if (movimentacao.getLoteMovimentacao().getCampanha() != null && movimentacao.getLoteMovimentacao().getCampanha().getId() != null){
			numeroEntrada = numeroEntrada +"-"+ movimentacao.getLoteMovimentacao().getCampanha().getId() + TipoParceiroEnum.CAMPANHA.ordinal();
			
			if( movimentacao.getLoteMovimentacao().getInstituicaoCampanha() != null && movimentacao.getLoteMovimentacao().getInstituicaoCampanha().getId() != null){
				numeroEntrada = numeroEntrada + "-"+movimentacao.getLoteMovimentacao().getInstituicaoCampanha().getId();
			} else if (movimentacao.getLoteMovimentacao().getFamiliaCampanha() != null && movimentacao.getLoteMovimentacao().getFamiliaCampanha().getId() != null){
				numeroEntrada = numeroEntrada + "-"+movimentacao.getLoteMovimentacao().getFamiliaCampanha().getId();
			}
		
		} else if (movimentacao.getLoteMovimentacao().getDoador() != null && movimentacao.getLoteMovimentacao().getDoador().getId() != null){
			numeroEntrada = numeroEntrada + "-"+movimentacao.getLoteMovimentacao().getDoador().getId() + TipoParceiroEnum.PESSOA.ordinal();
		
		} else if (movimentacao.getLoteMovimentacao().getInstituicao() != null && movimentacao.getLoteMovimentacao().getInstituicao().getId() != null){
			numeroEntrada = numeroEntrada + "-"+movimentacao.getLoteMovimentacao().getInstituicao().getId() + TipoParceiroEnum.INSTITUICAO.ordinal();
		}
		
		else if (movimentacao.getLoteMovimentacao().getFamilia() != null && movimentacao.getLoteMovimentacao().getFamilia().getId() != null){
			numeroEntrada = numeroEntrada + "-"+movimentacao.getLoteMovimentacao().getFamilia().getId() + TipoParceiroEnum.FAMILIA.ordinal();
		}
		
		else {
			//Senao é um anonimo
			numeroEntrada = numeroEntrada +"-"+ TipoParceiroEnum.ANONIMO.ordinal();
		}
		
		//adicionar se entrada ou saida
		
		numeroEntrada = numeroEntrada +"-"+ movimentacao.getTipoMovimentacaoEnum().ordinal();

		return numeroEntrada;
	}

	
	
	public void incluirSaida(Movimentacao saida) throws ApplicationException{
		saida.setTipoMovimentacaoEnum(TipoMovimentacaoEnum.SAIDA);
		//alterando valores do produto
		
		Produto produto = produtoService.obterProduto(saida.getProduto().getId());
		
		String numeroEntrada = extrairNumeroLote(saida);
		
		LoteMovimentacao loteMovimentacao = loteService.obterLoteMovimentacaoById(numeroEntrada);
		
		if (loteMovimentacao == null){
			loteMovimentacao = saida.getLoteMovimentacao();
			loteMovimentacao.setNumeroEntrada(numeroEntrada);
			
			loteService.incluirLote(loteMovimentacao);
		}
		else {
			saida.setLoteMovimentacao(loteMovimentacao);
		}
	
		
		Calendar myCal = new GregorianCalendar();
		myCal.setTime(saida.getData());
		myCal.set(Calendar.HOUR, Calendar.getInstance().get(Calendar.HOUR));
		myCal.set(Calendar.MINUTE, Calendar.getInstance().get(Calendar.MINUTE));
		myCal.set(Calendar.SECOND, Calendar.getInstance().get(Calendar.SECOND));
		
		saida.setData(myCal.getTime());
		 
		
		
		List<Movimentacao> movimentacoesPosterioes =  movimentacaoService.pesquisarMovimentacao(produto, saida.getData());
		
		arrumarMovimentacoesPosteriores(saida, produto, movimentacoesPosterioes);
		
		
		//atualizando valores para historico e média 
		
		produto.setQuantidadeEstoque(NumeroUtil.diminuirDinheiro(produto.getQuantidadeEstoque(), saida.getQuantidade(), 10));
		if (produto.getQuantidadeEstoque() < 0){
			if (produto.getId().equals(101)){
				System.out.println("CAGOU AQUI.....");
			}
			//lançar exption
			throw new ApplicationException("service.movimentacao.saldoNegativo.ERRO", new String[]{produto.getQuantidadeEstoque().toString(),produto.getNome(),DateUtil.dataToString(saida.getData())});
		}
		
			produto.setSaldoEstoque(NumeroUtil.diminuirDinheiro(produto.getSaldoEstoque(), saida.getValor(), 10));
			
		
		
		produtoService.alterarProduto(produto);
		em.merge(saida);
	}

	/**
	 * Exclusão de movimentacao.
	 * deve verificar se existem movimentacoes posteriores, se tiver deve mudar os dados de quantidade ultimo, saldo e valor médio ultimo
	 * caso não existir, deve excluir
	 * ambos os casos devem diminuir o saldo do produto
	 * @param movimentacao
	 * @throws ApplicationException
	 */
	public void excluirMovimentacao(Movimentacao movimentacao) throws ApplicationException{
		movimentacao = movimentacaoService.obterMobiMovimentacaoById(movimentacao.getId());
		
		Produto produto = produtoService.obterProduto(movimentacao.getProduto().getId());
		
		List<Movimentacao> movimentacoesPosterioes =  movimentacaoService.pesquisarMovimentacao(movimentacao.getProduto(), movimentacao.getData());
		
		Float valorMovimentacao = movimentacao.getValor();
		Float quantidadeMovimentacao = movimentacao.getQuantidade();
		
		//ira definir se os valores são negativos ou positivos
		if (movimentacao.getTipoMovimentacaoEnum().equals(TipoMovimentacaoEnum.ENTRADA)){
			valorMovimentacao = NumeroUtil.multiplicarDinheiro(valorMovimentacao, -1f, 3);
			quantidadeMovimentacao = NumeroUtil.multiplicarDinheiro(quantidadeMovimentacao, -1f, 3);
		}

		if (movimentacoesPosterioes != null && movimentacoesPosterioes.size() != 0){
			
			for (int i = 0; i < movimentacoesPosterioes.size(); i++) {
					movimentacoesPosterioes.get(i).setQuantidadeUltimo(NumeroUtil.somarDinheiro(movimentacoesPosterioes.get(i).getQuantidadeUltimo(), quantidadeMovimentacao, 3));
					movimentacoesPosterioes.get(i).setSaldoUltimo(NumeroUtil.somarDinheiro(movimentacoesPosterioes.get(i).getSaldoUltimo(), valorMovimentacao, 3));
					//para nao dividir por zero...
					if (movimentacoesPosterioes.get(i).getQuantidadeUltimo().equals(0f)){
						movimentacoesPosterioes.get(i).setValorMediaUltimo(0f);
						
					}
					else {
						movimentacoesPosterioes.get(i).setValorMediaUltimo(NumeroUtil.DividirDinheiro(movimentacoesPosterioes.get(i).getSaldoUltimo(), movimentacoesPosterioes.get(i).getQuantidadeUltimo(), 3));
					}
					
					
					//Responsavel por verificar se está negativo as coisas..
					
					Float valorAposALteracoes = 0f;
					
					if (movimentacoesPosterioes.get(i).getTipoMovimentacaoEnum().equals(TipoMovimentacaoEnum.ENTRADA)){
						valorAposALteracoes = NumeroUtil.somarDinheiro(movimentacoesPosterioes.get(i).getQuantidadeUltimo(), movimentacoesPosterioes.get(i).getQuantidade(), 4);
					}
					else {
						valorAposALteracoes = NumeroUtil.diminuirDinheiro(movimentacoesPosterioes.get(i).getQuantidadeUltimo(), movimentacoesPosterioes.get(i).getQuantidade(), 4);
					}
					
					
					if (valorAposALteracoes < 0){
						//lançar exption..
						System.out.println("FICOU COM SALDO NEGATIVO........");
						//estava comentado porém ACHO que precisa ficar descomentado.. assim que encontrar erro de saldo deve lançar erro!!!! Assim evita que no historico saldo fique negativo
//						throw new ApplicationException("service.movimentacao.saldoNegativo.ERRO",new String[]{DateUtil.dataToString(movimentacoesPosterioes.get(i).getData())});
						throw new ApplicationException("service.movimentacao.saldoNegativo.ERRO", new String[]{movimentacoesPosterioes.get(i).getQuantidadeUltimo().toString(),produto.getNome(),DateUtil.dataToString(movimentacoesPosterioes.get(i).getData())});
					}
					
					
					
					
					em.merge(movimentacoesPosterioes.get(i));
					
					
					
				}
			
			
				
			}
		
		//somente altere se for entrada
		if (movimentacao.getTipoMovimentacaoEnum().equals(TipoMovimentacaoEnum.ENTRADA)){
			produto.setValorHistoricoTotal(NumeroUtil.somarDinheiro(produto.getValorHistoricoTotal(), valorMovimentacao, 3));
			produto.setQuantidadeHistoricaTotal(NumeroUtil.somarDinheiro(produto.getQuantidadeHistoricaTotal(), quantidadeMovimentacao, 3));
		}
		
		produto.setQuantidadeEstoque(NumeroUtil.somarDinheiro(produto.getQuantidadeEstoque(), quantidadeMovimentacao, 3));
//		produto.setSaldoEstoque(NumeroUtil.somarDinheiro(produto.getSaldoEstoque(), valorMovimentacao, 3));
		
		produtoService.alterarProduto(produto);
		
		//verifica se o lote possi mais movimentacoes, caos contrario, deleta tbm o lote
		
		
		Query query = em.createQuery("DELETE FROM Movimentacao m WHERE m.id = :id");
		int deletedCount = query.setParameter("id", movimentacao.getId()).executeUpdate();
		  
		if (movimentacao.getLoteMovimentacao().getListMovimentacao().size() == 1) {
			loteService.excluirLote(movimentacao.getLoteMovimentacao());
		}
		
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void alterarEntrada1(Movimentacao movimentacao) throws ApplicationException{
		Movimentacao movOriginal = movimentacaoService.obterMobiMovimentacaoById(movimentacao.getId());
		
			movimentacaoService.excluirMovimentacao(movOriginal);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void alterarEntrada2(Movimentacao movimentacao) throws ApplicationException{
		movimentacao.setId(null);
		movimentacao.getLoteMovimentacao().setCodigo(null);
		movimentacao.setId(null);
		movimentacao.getLoteMovimentacao().setCodigo(null);
		movimentacaoService.incluirEntrada(movimentacao);
		
	}
	
	
	//Esses métodos eram usados em ordem:
	//Primeiro era deletado a movimentacao
	//depois de deletar o outro método setava null nas informacoes e inseria de novo com as alterações....
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void alterarSaida1(Movimentacao movimentacao) throws ApplicationException{
		Movimentacao movOriginal = movimentacaoService.obterMobiMovimentacaoById(movimentacao.getId());
		
			movimentacaoService.excluirMovimentacao(movOriginal);
	}
	
	//
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void alterarSaida2(Movimentacao movimentacao) throws Exception{
		movimentacao.setId(null);
		movimentacao.getLoteMovimentacao().setCodigo(null);
		
		movimentacao.setId(null);
		movimentacao.getLoteMovimentacao().setCodigo(null);
		
		movimentacaoService.incluirSaida(movimentacao);
		
	}
	
	
	//Vai obter a lista de movimentacoes para excluir e exclui todas e insere as novas
	public void alterarMovimentacaoLote(List<Movimentacao> movimentacoes, List<Movimentacao> movimentacoesExcluir, Movimentacao dadosMovimentacao) throws ApplicationException{
		try {
			
			//// Para não dar saldos negativos gerando error
			if(dadosMovimentacao.getTipoMovimentacaoEnum().equals(TipoMovimentacaoEnum.SAIDA)){
				//Exclui as mov a serem excluidas
				if (movimentacoesExcluir != null){
					for (Movimentacao movimentacao : movimentacoesExcluir) {
						if(movimentacao.getId() != null ){
							movimentacaoService.excluirMovimentacao(movimentacao);
						}
					}
				}
			}
			
			//Procurando a data anterior para verificar se ela foi alterada.
			//Busca da lista para exclusão caso encontrar é o suficiente
			
			Movimentacao inicio = new Movimentacao();
			
			if (movimentacoesExcluir != null && movimentacoesExcluir.size() > 0){
				inicio =  movimentacoesExcluir.get(0);
			}
			
			//Caso não existir mov para excluir vasculha as movimentações em busca de uma que ficou sem alteração para saber qual a data estava marcada.
			else {
				for (Movimentacao movimentacao : movimentacoes) {
					if (movimentacao.getId() != null && movimentacao.getId() > 0){
						inicio = movimentacao;
						break;
					}
				}
			}
			
			
			if (inicio.getLoteMovimentacao() != null && !inicio.getLoteMovimentacao().getData().equals(dadosMovimentacao.getLoteMovimentacao().getData())){
				
				for (Movimentacao mov : movimentacoes) {
					
					//Caso a movimentacao não tiver ID quer dizer que é uma nova
					if (mov.getId() > 0){
						
						mov.setData(dadosMovimentacao .getLoteMovimentacao().getData());
						mov.setLoteMovimentacao(dadosMovimentacao.getLoteMovimentacao());
						
						if(dadosMovimentacao.getTipoMovimentacaoEnum().equals(TipoMovimentacaoEnum.SAIDA)){
							movimentacaoService.alterarSaida1(mov);
							movimentacaoService.alterarSaida2(mov);
						} else{
							movimentacaoService.alterarEntrada1(mov);
							movimentacaoService.alterarEntrada2(mov);
						}
						
						
					}
					else {
						if(dadosMovimentacao.getTipoMovimentacaoEnum().equals(TipoMovimentacaoEnum.SAIDA)){
							movimentacaoService.incluirSaida(mov);
						}
						else {
							movimentacaoService.incluirEntrada(mov);
						}
					}
					
				}
				
			} else {
				
				//Caso a movimentacao não tiver ID quer dizer que é uma nova
				for (Movimentacao mov : movimentacoes) {
					
					//Caso a movimentacao não tiver ID quer dizer que é uma nova
					if (mov.getId() == null){
						mov.setData(dadosMovimentacao .getLoteMovimentacao().getData());
						mov.setLoteMovimentacao(dadosMovimentacao.getLoteMovimentacao());
						
						if(dadosMovimentacao.getTipoMovimentacaoEnum().equals(TipoMovimentacaoEnum.SAIDA)){
							movimentacaoService.incluirSaida(mov);
						}
						else {
							movimentacaoService.incluirEntrada(mov);
						}
					}
					
				}
				
			}
			
			
			
			
			// Para não dar saldos negativos gerando error
			if(dadosMovimentacao.getTipoMovimentacaoEnum().equals(TipoMovimentacaoEnum.ENTRADA)){
				//Exclui as mov a serem excluidas
				if (movimentacoesExcluir != null){
					for (Movimentacao movimentacao : movimentacoesExcluir) {
						if(movimentacao.getId() != null ){
							movimentacaoService.excluirMovimentacao(movimentacao);
						}
					}
				}
			}
			
			
		}  catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		
		
	}
	
	
	
	
	
	
	public Movimentacao obterMobiMovimentacaoById(Integer codigo){
		
		return em.find(Movimentacao.class, codigo);
		
	}
	
	

}