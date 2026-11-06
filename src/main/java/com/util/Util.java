package com.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Util {

	public java.sql.Date stringToDate(String fechaString) {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		try {
		    Date utilFecha = formato.parse(fechaString);
		    java.sql.Date fechaSQL = new java.sql.Date(utilFecha.getTime());
		    return fechaSQL;
		} catch (java.text.ParseException e) {
		    System.err.println("Error: El formato de la fecha debe ser dd/MM/yyyy");
		    e.printStackTrace();
		    return null;
		}
	}

	public String dateToString(java.sql.Date fechaSQL) {
	    if (fechaSQL == null) {
	        return null;
	    }
	    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	    return formato.format(fechaSQL);
	}

	public java.sql.Date stringToDateNew(String fechaString) {
	    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    
	    LocalDate localDate = LocalDate.parse(fechaString, formato);
	    
	    return java.sql.Date.valueOf(localDate);
	}
}
