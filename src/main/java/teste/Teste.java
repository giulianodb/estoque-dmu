package teste;

import java.io.BufferedReader;
import java.io.FileReader;

public class Teste {

	public static void main(String[] args) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("/home/giuliano/saidas.csv"));
			while(br.ready()){
				   String linha = br.readLine();
				   String[] split = linha.split(";");
				   
				   for (int i = 0; i < split.length; i++) {
					   System.out.print(split[i]);
					   System.out.print(" - ");
				   }
				   System.out.println();
				   System.out.println("=======================");
				}
				br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
