package com.impl;

import com.dao.ProductoJpaDAO;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import com.jpa.ProductoJpa;
import java.util.List;

public class ProductoJpaImpl {
    
    // Instanciamos el DAO para poder usarlo
    private ProductoJpaDAO productoDAO = new ProductoJpaDAO();

    public String registrar(String id, 
    						String nombre, 
    						int    proveedorId, 
    						String tipoProducto, 
    						int    stockMinimo, 
    						int    stockMaximo) {
        // 1. Regla de validación: Que los campos no estén vacíos
        if (	id           == null || id          .trim().isEmpty() ||
        		nombre       == null || nombre      .trim().isEmpty() ||
        		proveedorId  == 0    ||
        		tipoProducto == null || tipoProducto.trim().isEmpty() ||
        		stockMinimo  == 0    ||
        		stockMaximo  == 0    ) {
            return "Todos los campos son obligatorios."; 
        }

       ProductoJpa registroExistente = productoDAO.buscarPorId(id);
        
        if (registroExistente == null) {
	        ProductoJpa nuevo = new ProductoJpa(nombre, proveedorId, tipoProducto, stockMinimo, stockMaximo);
	        boolean exito = productoDAO.insertar(nuevo);
	
	        if (exito) {
	            return "OK"; // Usamos "OK" como señal de que todo salió perfecto
	        } else {
	            return "Error crítico al guardar en la base de datos.";
	        }
        } else {
        	registroExistente.setId             (Integer.parseInt(id));            
        	registroExistente.setNombre         (nombre      );
        	registroExistente.setProveedorId    (proveedorId );
        	registroExistente.setTipoProducto   (tipoProducto);
        	registroExistente.setStockMinimo    (stockMinimo);
        	registroExistente.setStockMaximo    (stockMaximo);

        	boolean exito = productoDAO.actualizar(registroExistente);
	
	        if (exito) {
	            return "OK"; // Usamos "OK" como señal de que todo salió perfecto
	        } else {
	            return "Error crítico al guardar en la base de datos.";
	        }
        }
    }
    public String registrar(JsonObject productoInput) {
    	String mensaje;
        // Extraemos las propiedades del objeto JSON recibido
		String nombre       = productoInput.getString("nombre");
		int    proveedorId  = productoInput.getInt   ("proveedorId");
		String tipoProducto = productoInput.getString("tipoProducto");
		int    stockMinimo  = productoInput.getInt   ("stockMinimo");
		int    stockMaximo  = productoInput.getInt   ("stockMaximo");
        
        ProductoJpa registro = new ProductoJpa(nombre, proveedorId, tipoProducto, stockMinimo, stockMaximo);
        
        // Manejo seguro del ID si viene o no en el JSON (es opcional al crear)
        int id = productoInput.containsKey("id") ? productoInput.getInt("id") : 0;
        
        System.out.println("ProductoJpaImpl - id = " + id);

        if (id == 0) {
	        boolean exito = productoDAO.insertar(registro);
	        if (exito) {
                mensaje = "Proveedor creado con éxito";
	        } else {
	            mensaje = "Error crítico al guardar en la base de datos.";
	        }
        } else {
        	registro.setId(id);
	        boolean exito = productoDAO.actualizar(registro);
	
	        if (exito) {
                mensaje = "Producto actualizado con éxito";
	        } else {
	            mensaje = "Error crítico al guardar en la base de datos.";
	        }
        }
        return mensaje;
    }

    public String getListaJsonString () {
	    List<ProductoJpa> lista = productoDAO.listar();

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
	    
	    for (ProductoJpa registro : lista) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                    .add("id",             registro.getId())
                    .add("nombre",         registro.getNombre())
                    .add("proveedorId",    registro.getProveedorId())
                    .add("tipoProducto",   registro.getTipoProducto())
                    .add("stockMinimo",    registro.getStockMinimo())
                    .add("stockMaximo",    registro.getStockMaximo());
                
            arrayBuilder.add(objectBuilder);
	    }
	
	    return arrayBuilder.build().toString();
	}

    public ProductoJpa buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null; 
        }

        ProductoJpa registro = productoDAO.buscarPorId(id);

        return registro;
    }
}
