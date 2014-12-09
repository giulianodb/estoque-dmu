package org.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	private DateUtil() {}
	
	public static int diferencaEmDias(Date data1, Date data2) {
		long time1 = data1.getTime();
		long time2 = data2.getTime();
		// Diferente entre dias + UMA HORA (para compensar horario de verao...)
		long diffMilli = time2 - time1;
		long umaHoraEmMili = 1000 * 60 * 60;			
		if (time1 < time2) {
			diffMilli += umaHoraEmMili;
		}
	
		return (int) (diffMilli  / (umaHoraEmMili * 24));
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

	public static String dataToString(Date data){
		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String retorno = formato.format(data);
		
		return retorno;
	}
	
	public static String dataToString(Date data,String formato){
		
		SimpleDateFormat format = new SimpleDateFormat(formato);
		String retorno = format.format(data);
		
		return retorno;
	}
	
	
	public enum MesesEnum{
		JANEIRO("Janeiro"),
		FEVEREIRO("Fevereiro"),
		MARCO("Março"),
		ABRIL("Abril"),
		MAIO("Maio"),
		JUNHO("Junho"),
		JULHO("Julho"),
		AGOSTO("Agosto"),
		SETEMBRO("Setembro"),
		OUTUBRO("Outubro"),
		NOVEMBRO("Novembro"),
		DEZEMBRO("Dezembro");
		
		
		private String descricao;
		
		private MesesEnum(String descricao) {
			this.descricao = descricao;
		}
		
		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
		
		
	}
}
