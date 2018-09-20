package br.com.casadocodigo.loja.daos;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.casadocodigo.loja.models.Compras;

public class CompraDao implements Serializable {

	private static final long serialVersionUID = 763851197483772003L;
	
	@PersistenceContext
	private EntityManager manager;

	public void salvar(Compras compra) {
		manager.persist(compra);
	}

	public Compras buscaPorUuid(String uuid) {
		return manager.createQuery("select c from Compras c where c.uuid = :uuid", Compras.class)
				.setParameter("uuid", uuid)
				.getSingleResult();
	}
	

}
