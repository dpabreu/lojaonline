package br.com.casadocodigo.loja.beans;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import br.com.casadocodigo.loja.daos.LivroDao;
import br.com.casadocodigo.loja.models.Livro;

@Model
public class HomeBean {
	
	@Inject
	private LivroDao livroDao;
	
	public List<Livro> ultimosLancamentos(){
		return livroDao.ultimosLancamentos();
	}
	
	public List<Livro> demaisLivros(){
		return livroDao.demaisLivros();
	}
}
