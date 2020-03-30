package org.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.util.DateUtil;
import org.util.NumeroUtil;

@Entity
@Table(name="loteMovimentacao",schema="estoque")
public class LoteMovimentacao implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8996466788745786863L;
	
	@SequenceGenerator(name = "LOTEMOVIMENTACAO_ID", sequenceName = "id_lotemovimentacao_seq", schema="estoque",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "LOTEMOVIMENTACAO_ID")
	@Id
	private Integer codigo;
	
	private String numeroEntrada;
	
	//Esses atribtutos serão populados de acordo com o tipo de doador
	@ManyToOne(fetch=FetchType.EAGER)
	private Campanha campanha;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Instituicao instituicao;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Doador doador;
	
	@Temporal(value=TemporalType.TIMESTAMP )
	@Column(name="dataEntrada")
	private Date data;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Familia familia;
	
	//Caso a saída for de campanha essa é a família que irá receber a saída de estqoue
	@ManyToOne(fetch=FetchType.EAGER)
	private Familia familiaCampanha;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Doador doadorCampanha;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Usuario usuarioFezCadastro;
	
	@Temporal(value=TemporalType.TIMESTAMP )
	private Date dataAcao;
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Instituicao instituicaoCampanha;
	
	@OneToMany(mappedBy = "loteMovimentacao",fetch = FetchType.LAZY)
	private List<Movimentacao> listMovimentacao;
	
	@Enumerated
	private TipoMovimentacaoEnum tipoMovimentacaoEnum;
	
	
	/**
	 * Retorna o nome do doador, verifica se é campanha  etc..
	 * @return
	 */
	public String obterNomeDoador(){
		if(campanha != null && campanha.getId()!=null){
			if (familiaCampanha != null){
				return campanha.getNome() + " - " + familiaCampanha.getNomeResponsavel();
			}
			else {
				if (doadorCampanha != null){
					return campanha.getNome()+" - "+ doadorCampanha.getNome();
				}
				else {
					if (instituicaoCampanha != null) {
						return campanha.getNome()+" - "+ instituicaoCampanha.getNome();
					}
					else {
						return campanha.getNome()+" - ";
					}
				}
			}
		}
		
		else if(instituicao!=null && instituicao.getId() != null) {
			return instituicao.getNome();
		}
		
		else if(doador != null && doador.getId() != null){
			return doador.getNome();
		}
		
		else if(familia!= null && familia.getId() != null) {
			return familia.getNomeResponsavel();
		}
		else {
			return "Anônimo";
		}
	}
	
	
	/**
	 * Retorna o nome do recebedor, verifica se é campanha  etc..
	 * @return
	 */
	public String obterNomeReceptor(Boolean mostrarCampanha){
		if(campanha != null && campanha.getId()!=null){
			if (familiaCampanha != null){
				if (mostrarCampanha){
					return campanha.getNome() + " - " + familiaCampanha.getNomeResponsavel();
				} else{
					return familiaCampanha.getNomeResponsavel();
				}
			}
			else {
				if (instituicaoCampanha == null) {
					return "RECEPTOR NÃO INFORMADO!";
					
				} else {
					return instituicaoCampanha.getNome();
				}
			}
		}	
		
		else if(instituicao!=null && instituicao.getId() != null) {
			return instituicao.getNome();
		}
		
		
		else if(familia!= null && familia.getId() != null) {
			return familia.getNomeResponsavel();
		}
		else {
			return "Anônimo";
		}
	}
	

	
	
	public String extrairNumeroLote(TipoMovimentacaoEnum tipoMovimentacaoEnum) {
		//alterando valores do produto
		
		//Definindo o código do numero de entrada que será como um numero do recibo
		String numeroEntrada = DateUtil.dataToString(getData(), "yyMMdd");
		
		Calendar myCal = new GregorianCalendar();
		myCal.setTime(getData());
		myCal.set(Calendar.HOUR, Calendar.getInstance().get(Calendar.HOUR));
		myCal.set(Calendar.MINUTE, Calendar.getInstance().get(Calendar.MINUTE));
		myCal.set(Calendar.SECOND, Calendar.getInstance().get(Calendar.SECOND));
		
		setData(myCal.getTime());
		
		if (getCampanha() != null && getCampanha().getId() != null){
			numeroEntrada = numeroEntrada +"-"+ getCampanha().getId() + TipoParceiroEnum.CAMPANHA.ordinal();
			
			if( getInstituicaoCampanha() != null && getInstituicaoCampanha().getId() != null){
				numeroEntrada = numeroEntrada + "-"+getInstituicaoCampanha().getId();
			} else if (getFamiliaCampanha() != null && getFamiliaCampanha().getId() != null){
				numeroEntrada = numeroEntrada + "-"+getFamiliaCampanha().getId();
			}
		
		} else if (getDoador() != null && getDoador().getId() != null){
			numeroEntrada = numeroEntrada + "-"+getDoador().getId() + TipoParceiroEnum.PESSOA.ordinal();
		
		} else if (getInstituicao() != null && getInstituicao().getId() != null){
			numeroEntrada = numeroEntrada + "-"+getInstituicao().getId() + TipoParceiroEnum.INSTITUICAO.ordinal();
		}
		
		else if (getFamilia() != null && getFamilia().getId() != null){
			numeroEntrada = numeroEntrada + "-"+getFamilia().getId() + TipoParceiroEnum.FAMILIA.ordinal();
		}
		
		else {
			//Senao é um anonimo
			numeroEntrada = numeroEntrada +"-"+ TipoParceiroEnum.ANONIMO.ordinal();
		}
		
		//adicionar se entrada ou saida
		
		numeroEntrada = numeroEntrada +"-"+ tipoMovimentacaoEnum.ordinal();

		return numeroEntrada;
	}

	
	
	public String obterNomeReceptor(){
		return obterNomeReceptor(false);
	}
	
	
	public String obterTipoMovimentacao(){
		if (tipoMovimentacaoEnum == null) {
			if (listMovimentacao != null && listMovimentacao.size() > 0){
				return listMovimentacao.get(0).getTipoMovimentacaoEnum().getDescricao();
			}
			else {
				return null;
			}
		}
		else {
			return tipoMovimentacaoEnum.getDescricao();
		}
		
	}
	
	public TipoMovimentacaoEnum obterTipoMovimentacaoEnum(){
		if (listMovimentacao != null && listMovimentacao.size() > 0){
			return listMovimentacao.get(0).getTipoMovimentacaoEnum();
		}
		else {
			return null;
		}
		
	}
	
	
	public Float valorTotalMovimentacao(){
		Float valorTotal = 0f;
		for (Movimentacao movimentacao : listMovimentacao) {
			valorTotal = NumeroUtil.somarDinheiro(valorTotal, movimentacao.getValor(), 3);
		}
		
		return valorTotal;
	}
	
	
	
	public String getNumeroEntrada() {
		return numeroEntrada;
	}

	public void setNumeroEntrada(String numeroEntrada) {
		this.numeroEntrada = numeroEntrada;
	}

	public Campanha getCampanha() {
		return campanha;
	}

	public void setCampanha(Campanha campanha) {
		this.campanha = campanha;
	}

	public Instituicao getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(Instituicao instituicao) {
		this.instituicao = instituicao;
	}

	public Doador getDoador() {
		return doador;
	}

	public void setDoador(Doador doador) {
		this.doador = doador;
	}
	
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Familia getFamilia() {
		return familia;
	}

	public void setFamilia(Familia familia) {
		this.familia = familia;
	}

	public Familia getFamiliaCampanha() {
		return familiaCampanha;
	}

	public void setFamiliaCampanha(Familia familiaCampanha) {
		this.familiaCampanha = familiaCampanha;
	}








	public Doador getDoadorCampanha() {
		return doadorCampanha;
	}








	public void setDoadorCampanha(Doador doadorCampanha) {
		this.doadorCampanha = doadorCampanha;
	}








	public Instituicao getInstituicaoCampanha() {
		return instituicaoCampanha;
	}








	public void setInstituicaoCampanha(Instituicao instituicaoCampanha) {
		this.instituicaoCampanha = instituicaoCampanha;
	}




	public List<Movimentacao> getListMovimentacao() {
		return listMovimentacao;
	}




	public void setListMovimentacao(List<Movimentacao> listMovimentacao) {
		this.listMovimentacao = listMovimentacao;
	}


	public Usuario getUsuarioFezCadastro() {
		return usuarioFezCadastro;
	}


	public void setUsuarioFezCadastro(Usuario usuarioFezCadastro) {
		this.usuarioFezCadastro = usuarioFezCadastro;
	}


	public Date getDataAcao() {
		return dataAcao;
	}


	public void setDataAcao(Date dataAcao) {
		this.dataAcao = dataAcao;
	}


	public TipoMovimentacaoEnum getTipoMovimentacaoEnum() {
		return tipoMovimentacaoEnum;
	}


	public void setTipoMovimentacaoEnum(TipoMovimentacaoEnum tipoMovimentacaoEnum) {
		this.tipoMovimentacaoEnum = tipoMovimentacaoEnum;
	}
	
	
	
	
}
