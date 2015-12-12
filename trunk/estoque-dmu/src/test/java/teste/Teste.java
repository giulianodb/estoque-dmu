package teste;

import java.text.DecimalFormat;

import converter.MoedaConverter;

public class Teste {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		 String numero = "4545,00";
		 String numeroSemVirgula = "";
		 
		 String[] numeros = numero.split(",");
		 
		 if (numeros.length > 0 && numeros[0] != null){
			 numeroSemVirgula = numeros[0];
		 }
		 else {
			 numeroSemVirgula = numero; 
		 }
		 
		 char[] letras = numeroSemVirgula.toCharArray();
		 String numeroResolvido = "";
		 
		 for (int i = 0; i < letras.length; i++) {
			 if (i != 0){
				 if (i/3 == 0){
					 
				 }
				 
			 }
		 }
		 
		 DecimalFormat formatCampo = new DecimalFormat("###,###,###");
		 formatCampo.format(1000);
		 
//		 System.out.println( formatCampo.format(500));
		 
		 MoedaConverter mc = new MoedaConverter();
		 
		 System.out.println(mc.getAsString(null, null, 999999.99F));
		 
		
	}

}
