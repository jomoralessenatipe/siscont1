package com.impl;

import com.dao.VendedorJpaDAO;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import com.jpa.VendedorJpa;
import java.util.List;

public class VendedorJpaImpl {
    
    // Instanciamos el DAO para poder usarlo
    private VendedorJpaDAO VendedorDAO = new VendedorJpaDAO();

    public String registrar(JsonObject VendedorInput) {
    	String mensaje;
        // Extraemos las propiedades del objeto JSON recibido
		String nombre             = VendedorInput.getString("nombre");            
		String tipoVendedor       = VendedorInput.getString("tipoVendedor");     
		String tipoDocumento      = VendedorInput.getString("tipoDocumento");    
		String numeroDocumento    = VendedorInput.getString("numeroDocumento");  
		String telefono           = VendedorInput.getString("telefono");          
		double porcentajeComision = VendedorInput.getJsonNumber("porcentajeComision").doubleValue();
        
        VendedorJpa registro = new VendedorJpa(nombre, tipoVendedor, tipoDocumento, numeroDocumento, telefono, porcentajeComision);
        
        // Manejo seguro del ID si viene o no en el JSON (es opcional al crear)
        int id = VendedorInput.containsKey("id") ? VendedorInput.getInt("id") : 0;
        
        System.out.println("====== JSON RECIBIDO DESDE VUE ======");
        System.out.println("VendedorJpaImpl - vendedorInput = " + VendedorInput);
        System.out.println("=====================================");

        if (id == 0) {
	        boolean exito = VendedorDAO.insertar(registro);
	        if (exito) {
                mensaje = "Vendedor creado con éxito";
	        } else {
	            mensaje = "Error crítico al guardar en la base de datos.";
	        }
        } else {
        	registro.setId(id);
	        boolean exito = VendedorDAO.actualizar(registro);
	
	        if (exito) {
                mensaje = "Vendedor actualizado con éxito";
	        } else {
	            mensaje = "Error crítico al guardar en la base de datos.";
	        }
        }
        return mensaje;
    }

    public String getListaJsonString () {
	    List<VendedorJpa> lista = VendedorDAO.listar();

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
	    
	    for (VendedorJpa registro : lista) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                    .add("id", registro.getId())
                    .add("nombre", registro.getNombre())
                    .add("tipoVendedor", registro.getTipoVendedor())
                    .add("tipoDocumento", registro.getTipoDocumento())
                    .add("numeroDocumento", registro.getNumeroDocumento())
                    .add("telefono", registro.getTelefono())
                    .add("porcentajeComision", registro.getPorcentajeComision());
                
            arrayBuilder.add(objectBuilder);
	    }
	
	    return arrayBuilder.build().toString();
	}

    public VendedorJpa buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null; 
        }

        VendedorJpa registro = VendedorDAO.buscarPorId(id);

        return registro;
    }
}
