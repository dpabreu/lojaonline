package br.com.casadocodigo.loja.service;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import br.com.casadocodigo.loja.daos.CompraDao;
import br.com.casadocodigo.loja.infra.MailSender;
import br.com.casadocodigo.loja.models.Compras;

@MessageDriven(activationConfig = {
	
		@ActivationConfigProperty(
				propertyName="destinationLookup",
				propertyValue="java:/jms/topics/CarrinhoComprasTopico"
				),
		@ActivationConfigProperty(
				propertyName="destinationType",
				propertyValue="javax.jms.Topic"
				)
})
public class EnviaEmailCompra implements MessageListener {

	@Inject
	private CompraDao compraDao;
	
	@Inject
	private MailSender mailSender;

	@Override
	public void onMessage(Message message){
		
		try {

			TextMessage txtMessage = (TextMessage) message;
			
			Compras compra = compraDao.buscaPorUuid(txtMessage.getText());
	
			String messageBody = "Sua compra foi realizada com sucesso, número do pedido " + compra.getUuid();
			
			mailSender.send("compras@casadocodigo.com.br", compra.getUsuario().getEmail(), 
					"Nova Compra CDC", messageBody);
		
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
