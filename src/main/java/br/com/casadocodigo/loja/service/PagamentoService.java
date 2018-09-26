package br.com.casadocodigo.loja.service;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.servlet.ServletContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import br.com.casadocodigo.loja.daos.CompraDao;
import br.com.casadocodigo.loja.models.Compras;

@Path("/pagamento")
public class PagamentoService {
	
	@Context
	private ServletContext context;
	
	@Inject
	private CompraDao compraDao;
	
	@Inject
	private PagamentoGetway pagamentoGetway;
	
	@Inject
	private JMSContext jmsContext;

	@Resource(name="java:/jms/topics/CarrinhoComprasTopico")
	private Destination destination;
	
	private static ExecutorService executor = Executors.newFixedThreadPool(50);
	
	@POST
	public void pagar(@Suspended AsyncResponse ar, @QueryParam("uuid") String uuid) {
	
		Compras compra = compraDao.buscaPorUuid(uuid);
		String contextPath = context.getContextPath();
		
		JMSProducer jmsProducer = jmsContext.createProducer();
		
		executor.submit(() -> {
			try {
				pagamentoGetway.pagar(compra.getTotal());
				
				URI responseUri = UriBuilder
						.fromPath("http://localhost:8080" + contextPath + "/index.xhtml")
						.queryParam("msg", "Compra realizada com sucesso").build();
				
				Response response = Response.seeOther(responseUri).build();
				
				jmsProducer.send(destination, compra.getUuid());
				
				ar.resume(response);
				
			} catch (Exception e) {
				ar.resume(new WebApplicationException(e));
			}
		});
	}
}
