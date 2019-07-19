package org.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateUtil {
	private DateUtil() {
	}

	public static Date retornarData(String dataParaParser, String pattern) {

		DateTimeFormatter formato = DateTimeFormatter.ofPattern(pattern);
		LocalDate data = LocalDate.parse(dataParaParser, formato);
		
		return asDate(data);
	}

	public static Date asDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static int diferencaEmDias(Date data1, Date data2) {
		long time1 = data1.getTime();
		long time2 = data2.getTime();
		// Diferente entre dias + UMA HORA (para compensar horario de verao...)
		long diffMilli = time2 - time1;
		long umaHoraEmMili = 1000 * 60 * 60;
		if (time1 < time2) {
			diffMilli += umaHoraEmMili;
		}

		return (int) (diffMilli / (umaHoraEmMili * 24));
	}

	public static Date somenteData(Date data) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(data);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date somaDias(Date data, int qtd) {
		Calendar dataSomada = new GregorianCalendar();
		dataSomada.setTime(data);
		dataSomada.add(Calendar.DATE, qtd);

		return dataSomada.getTime();
	}

	public static Date somaMeses(Date data, int qtd) {
		Calendar dataSomada = new GregorianCalendar();
		dataSomada.setTime(data);
		dataSomada.add(Calendar.MONTH, qtd);
		return dataSomada.getTime();
	}

	public static String dataToString(Date data) {

		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String retorno = formato.format(data);

		return retorno;
	}

	public static String dataToString(Date data, String formato) {

		SimpleDateFormat format = new SimpleDateFormat(formato);
		String retorno = format.format(data);

		return retorno;
	}

	public static String getHoraAtual() {

		GregorianCalendar teste = new GregorianCalendar();
		SimpleDateFormat teste2 = new SimpleDateFormat("HH:mm");
		System.out.println(teste2.format(teste.getTime()));

		return teste2.format(teste.getTime());
	}

	public static Date adicionarHoraInicio(Date data,int quantidadeSegundos) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(data); // colocando o objeto Date no Calendar
		calendar.set(Calendar.HOUR_OF_DAY, 0); // zerando as horas, minuots e segundos..
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, quantidadeSegundos);

		return calendar.getTime();
	}

	public static Date adicionarHoraFim(Date data,int quantidadeSegundos) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(data); // colocando o objeto Date no Calendar
		calendar.set(Calendar.HOUR_OF_DAY, 23); // zerando as horas, minuots e segundos..
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, quantidadeSegundos);

		return calendar.getTime();
	}

	public static String dataExtenso(String data) {

		String[] dataSplit = data.split("/");
		StringBuilder sb = new StringBuilder();
		sb.append("Curitiba, ");
		sb.append(dataSplit[0]);
		sb.append(" de ");
		sb.append(obterMes(dataSplit[1]));
		sb.append(" de ");
		sb.append(dataSplit[2]);

		return sb.toString();
	}

	public enum MesesEnum {
		JANEIRO("Janeiro", 1), FEVEREIRO("Fevereiro", 2), MARCO("Março", 3), ABRIL("Abril", 4), MAIO("Maio", 5), JUNHO(
				"Junho", 6), JULHO("Julho", 7), AGOSTO("Agosto", 8), SETEMBRO("Setembro",
						9), OUTUBRO("Outubro", 10), NOVEMBRO("Novembro", 11), DEZEMBRO("Dezembro", 12);

		private String descricao;
		private int inteiro;

		private MesesEnum(String descricao, int inteiro) {
			this.descricao = descricao;
			this.inteiro = inteiro;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}

		public int getInteiro() {
			return inteiro;
		}

		public void setInteiro(int inteiro) {
			this.inteiro = inteiro;
		}

	}

	public static String obterMes(String numero) {
		int posicao = Integer.parseInt(numero);
		List<MesesEnum> listaMeses = new ArrayList<MesesEnum>(Arrays.asList(MesesEnum.values()));
		for (MesesEnum mesesEnum : listaMeses) {
			if (mesesEnum.getInteiro() == posicao) {
				return mesesEnum.getDescricao();
			}
		}

		return "Janeiro";

	}
}
