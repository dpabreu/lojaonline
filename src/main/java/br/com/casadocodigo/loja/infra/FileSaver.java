package br.com.casadocodigo.loja.infra;

import java.io.IOException;

import javax.servlet.http.Part;

public class FileSaver {

	private static final String SERVERPATH = "/Daniel/casadocodigo";

	public String write(Part part, String path) {
		String relativePath = path + "/" + part.getSubmittedFileName();
		try {
			part.write( SERVERPATH + "/" + relativePath);
			
			return relativePath;
		} catch(IOException e){
			throw new RuntimeException();
		}
	}
	
}
