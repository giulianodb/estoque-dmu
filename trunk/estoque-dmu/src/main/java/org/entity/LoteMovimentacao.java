package org.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.jboss.netty.channel.FailedChannelFuture;
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
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Instituicao instituicaoCampanha;
	
	@OneToMany(mappedBy = "loteMovimentacao",fetch = FetchType.EAGER)
	private List<Movimentacao> listMovimentacao;
	
	
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
					return campanha.getNome()+" - "+ instituicaoCampanha.getNome();
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
	
	
	
	
	public String obterTipoMovimentacao(){
		return listMovimentacao.get(0).getTipoMovimentacaoEnum().getDescricao();
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
	
	
	
	
}
