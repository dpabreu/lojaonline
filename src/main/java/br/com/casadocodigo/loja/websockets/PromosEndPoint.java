package br.com.casadocodigo.loja.websockets;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import br.com.casadocodigo.loja.models.Promo;

@ServerEndpoint(value = "/canal/promos")
public class PromosEndPoint {
	
	@Inject
	private UsuariosSession usuarios;

	public void send(Promo promo) {
		
		List<Session> sessions = usuarios.getUsuarios();
		
		for (Session session : sessions) {
			if(session.isOpen()) {
				
				try {
					session.getBasicRemote().sendText(promo.toJson());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	@OnOpen
	public void onMessage(Session session) {
		usuarios.add(session);
	}
	
	@OnClose
	public void onClose(Session session, CloseReason reason) {
		usuarios.remove(session);
		System.out.println(reason.getCloseCode());
	}
	
	
}
