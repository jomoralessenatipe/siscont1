package com.impl;

import com.dao.ProveedorJpaDAO;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import com.jpa.ProveedorJpa;
import java.util.List;

public class ProveedorJpaImpl {
    
    // Instanciamos el DAO para poder usarlo
    private ProveedorJpaDAO proveedorDAO = new ProveedorJpaDAO();

    public String registrar(String id, 
    						String ruc, 
    						String razonSocial, 
    						String telefono, 
    						String direccion, 
    						String distritoId, 
    						String provinciaId, 
    						String departamentoId) {
        // 1. Regla de validación: Que los campos no estén vacíos
        if (	id             == null || id            .trim().isEmpty() ||
        		ruc            == null || ruc           .trim().isEmpty() ||
        		razonSocial    == null || razonSocial   .trim().isEmpty() ||
        		telefono       == null || telefono      .trim().isEmpty() ||
        		direccion      == null || direccion     .trim().isEmpty() ||
        		distritoId     == null || distritoId    .trim().isEmpty() ||
        		provinciaId    == null || provinciaId   .trim().isEmpty() ||
        		departamentoId == null || departamentoId.trim().isEmpty() ) {
            return "Todos los campos son obligatorios."; 
        }

        ProveedorJpa registroExistente = proveedorDAO.buscarPorId(id);
        
        if (registroExistente == null) {
	        ProveedorJpa nuevo = new ProveedorJpa(ruc, razonSocial, telefono, direccion, Integer.parseInt(distritoId), Integer.parseInt(distritoId), Integer.parseInt(distritoId));
	        boolean exito = proveedorDAO.insertar(nuevo);
	
	        if (exito) {
	            return "OK"; // Usamos "OK" como señal de que todo salió perfecto
	        } else {
	            return "Error crítico al guardar en la base de datos.";
	        }
        } else {
        	registroExistente.setId             (Integer.parseInt(id));            
        	registroExistente.setRuc            (ruc            );
        	registroExistente.setRazonSocial    (razonSocial    );
        	registroExistente.setTelefono       (telefono       );
        	registroExistente.setDireccion      (direccion      );
        	registroExistente.setDistritoId     (Integer.parseInt(distritoId    ));
        	registroExistente.setProvinciaId    (Integer.parseInt(provinciaId   ));
        	registroExistente.setDepartamentoId (Integer.parseInt(departamentoId));

        	boolean exito = proveedorDAO.actualizar(registroExistente);
	
	        if (exito) {
	            return "OK"; // Usamos "OK" como señal de que todo salió perfecto
	        } else {
	            return "Error crítico al guardar en la base de datos.";
	        }
        }
    }
    public String registrar(JsonObject proveedorInput) {
    	String mensaje;
        // Extraemos las propiedades del objeto JSON recibido
		String ruc            = proveedorInput.getString("ruc");
		String razonSocial    = proveedorInput.getString("razonSocial");
		String telefono       = proveedorInput.getString("telefono");
		String direccion      = proveedorInput.getString("direccion");
		int    distritoId     = proveedorInput.getInt   ("distritoId");
		int    provinciaId    = proveedorInput.getInt   ("provinciaId");
		int    departamentoId = proveedorInput.getInt   ("departamentoId");        
        
        ProveedorJpa registro = new ProveedorJpa(ruc, razonSocial, telefono, direccion, distritoId, distritoId, distritoId);
        
        // Manejo seguro del ID si viene o no en el JSON (es opcional al crear)
        int id = proveedorInput.containsKey("id") ? proveedorInput.getInt("id") : 0;
        
        System.out.println("ProveedorJpaImpl - id = " + id);

        if (id == 0) {
	        boolean exito = proveedorDAO.insertar(registro);
	        if (exito) {
                mensaje = "Proveedor creado con éxito";
	        } else {
	            mensaje = "Error crítico al guardar en la base de datos.";
	        }
        } else {
        	registro.setId(id);
	        boolean exito = proveedorDAO.actualizar(registro);
	
	        if (exito) {
                mensaje = "Proveedor actualizado con éxito";
	        } else {
	            mensaje = "Error crítico al guardar en la base de datos.";
	        }
        }
        return mensaje;
    }

    public String getListaJsonString () {
	    List<ProveedorJpa> lista = proveedorDAO.listar();

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
	    
	    for (ProveedorJpa registro : lista) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                    .add("id", registro.getId())
                    .add("ruc", registro.getRuc())
                    .add("razonSocial", registro.getRazonSocial())
                    .add("telefono", registro.getTelefono())
                    .add("direccion", registro.getDireccion())
                    .add("distritoId", registro.getDistritoId())
                    .add("provinciaId", registro.getProvinciaId())
                    .add("departamentoId", registro.getDepartamentoId());
                
            arrayBuilder.add(objectBuilder);
	    }
	
	    return arrayBuilder.build().toString();
	}

    public ProveedorJpa buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null; 
        }

        ProveedorJpa registro = proveedorDAO.buscarPorId(id);

        return registro;
    }
}
