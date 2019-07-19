package teste;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/")
public class Servico {
	
	
	@GET
	@Path("teste/{pesquisa}")
	@Produces("application/json")
	public String metodoGet(@PathParam("pesquisa") String pesquisa){
		
		
		
		return "{\"nome\":\"Tatu da silva\" ,\"telefone\":\"9999-9999\",\"item\":\""+pesquisa+"\"}";
	}
	
}
