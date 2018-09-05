package br.com.casadocodigo.loja.converters;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass=Calendar.class)
public class CalendarConverter implements Converter{
	
	private DateTimeConverter converter = new DateTimeConverter();
	
	public CalendarConverter() {
	    converter.setPattern("dd/MM/yyyy");
	    converter.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
	}
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent component, String dataTexto) {
		
		Date data = (Date) converter.getAsObject(arg0, component, dataTexto);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		
		return calendar;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent component, Object dataObject) {
		
		if(dataObject == null)
			return null;
		
		Calendar calendar = (Calendar) dataObject;
		return converter.getAsString(arg0, component, calendar .getTime());
	}

	
	
}
