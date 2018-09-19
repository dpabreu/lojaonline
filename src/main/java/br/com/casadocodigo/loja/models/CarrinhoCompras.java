package br.com.casadocodigo.loja.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArrayBuilder;

import org.omnifaces.util.Components.ForEach;

import br.com.casadocodigo.loja.daos.CompraDao;
import br.com.casadocodigo.loja.daos.UsuarioDao;

@Named
@SessionScoped
public class CarrinhoCompras implements Serializable {

	private static final long serialVersionUID = 5379979992160215665L;
	
	private Set<CarrinhoItem> itens = new HashSet<>();
	
	@Inject
	private UsuarioDao usuarioDao;

	@Inject
	private CompraDao compraDao;
	
	public void add(CarrinhoItem item) {
		itens.add(item);
	}

	public List<CarrinhoItem> getItens() {
		
		List<CarrinhoItem> carrinhoItens = new ArrayList<CarrinhoItem>(itens);
		
		return carrinhoItens;
	}
	
	public BigDecimal getTotal(CarrinhoItem item) {
		
		return item.getLivro().getPreco().
				multiply(new BigDecimal(item.getQuantidade()));
		
	}	
	
	public Integer getQtdTotal() {
		return this.itens.stream().mapToInt(item -> item.getQuantidade()).sum();
	}
	
	public BigDecimal getTotal() {
		BigDecimal total = BigDecimal.ZERO ;
		
		for (CarrinhoItem carrinhoItem : itens) {
			total = total.add(carrinhoItem.getLivro().getPreco().
					multiply(new BigDecimal(carrinhoItem.getQuantidade())));
		}
		
		
		return total;
	}

	public void remove(CarrinhoItem item) {
		
		this.itens.remove(item);
		
	}

	public void finalizar(Usuario usuario) {
		Compras compra = new Compras();
		compra.setUsuario(usuario);
		compra.setItens(this.toJson());
		usuarioDao.salvar(usuario);
		compraDao.salvar(compra);
	}

	private String toJson() {
		
		JsonArrayBuilder builder = Json.createArrayBuilder();
		for (CarrinhoItem item : itens) {
			builder.add(Json.createObjectBuilder()
					.add("Titulo", item.getLivro().getTitulo())
					.add("Preco", item.getLivro().getPreco())
					.add("quantidade", item.getQuantidade())
					.add("total", getTotal(item)));
		}
		
		String json = builder.build().toString();
		System.out.println(json);
		
		return json;
	}
}
