package br.com.casadocodigo.loja.service;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import br.com.casadocodigo.loja.models.Pagamento;

public class PagamentoGetway implements Serializable {

	private static final long serialVersionUID = -2710678565184555437L;

	public String pagar(BigDecimal total) {
		
		Client client = ClientBuilder.newClient();
		Pagamento pagamento = new Pagamento(total);
		String target = "https://book-payment.herokuapp.com/payment";
		Entity<Pagamento> json = Entity.json(pagamento);
		
		return client.target(target).request().post(json, String.class);
	}
	
}
