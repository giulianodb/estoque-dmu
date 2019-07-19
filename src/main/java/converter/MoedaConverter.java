package converter;

import java.text.DecimalFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.util.NumeroUtil;

@FacesConverter("converter.MoedaConverter")
public class MoedaConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String valor) {
		Float retorno = new Float(0);
		if (valor != null && !valor.equals("")){
			
			valor = valor.replace(",", ".");
			retorno = Float.parseFloat(valor);
			//inserido para deixar como duas casas...
			retorno = NumeroUtil.deixarFloatDuasCasas(retorno);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		String moeda = "";
		
		DecimalFormat formatCampo = new DecimalFormat("###,###,###,###.##");
		
		if (arg2 != null) {
			Float teste = (Float) arg2;
			arg2 = formatCampo.format(teste);
			
			
			moeda = arg2.toString();
			
		}
			
		String[] moedaSplit = moeda.split(",");
		
		if (moedaSplit.length == 1){
			moeda = moeda + ",00";
		}
		
		if (moedaSplit.length >= 2 && moedaSplit[1].length() == 1){
			moeda = moeda + "0";
		}
		
		if (moedaSplit.length >= 2) {
			if ( moedaSplit[1].length() > 2){
				moeda = moedaSplit[0] +","+ moedaSplit[1].substring(0, 2); 
		
			}
		}
		
		return moeda;
	}

}
