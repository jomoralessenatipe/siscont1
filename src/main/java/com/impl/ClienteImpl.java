package com.impl;

import com.dao.ClienteDAO;
import com.util.Util;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import com.beans.Cliente;
import com.util.Util;
import java.util.List;

public class ClienteImpl {
    
    // Instanciamos el DAO para poder usarlo
    private ClienteDAO clienteDAO = new ClienteDAO();
    private Util util = new Util(); 

    public String registrarCliente(JsonObject clienteInput) {
    	String mensaje;
        // Extraemos las propiedades del objeto JSON recibido
        Cliente clienteRegistro = new Cliente();
        clienteRegistro.setTipoDocumento(clienteInput.getString("tipoDocumento"));
        clienteRegistro.setNumeroDocumento(clienteInput.getString("numeroDocumento"));
        clienteRegistro.setNombreRazonSocial(clienteInput.getString("nombreRazonSocial"));
        clienteRegistro.setDireccion(clienteInput.getString("direccion"));
        clienteRegistro.setTelefono(clienteInput.getString("telefono"));
        clienteRegistro.setCorreo(clienteInput.getString("correo"));
        clienteRegistro.setFechaIngreso(util.stringToDate(clienteInput.getString("fechaIngreso")));
        clienteRegistro.setCategoria(clienteInput.getString("categoria"));
        clienteRegistro.setEstado(clienteInput.getString("estado"));
        
        
        // Manejo seguro del ID si viene o no en el JSON (es opcional al crear)
        int id = clienteInput.containsKey("id") ? clienteInput.getInt("id") : 0;

        if (id == 0) {
	        boolean exito = clienteDAO.insertarCliente(clienteRegistro);
	        if (exito) {
                mensaje = "Cliente creado con éxito";
	        } else {
	            mensaje = "Error crítico al guardar en la base de datos.";
	        }
        } else {
        	clienteRegistro.setId(id);
	        boolean exito = clienteDAO.actualizarCliente(clienteRegistro);
	
	        if (exito) {
                mensaje = "Cliente actualizado con éxito";
	        } else {
	            mensaje = "Error crítico al guardar en la base de datos.";
	        }
        }
        return mensaje;
    }
    public String getListaClientesJsonString () {
	    List<Cliente> listaClientes= clienteDAO.listarClientes();

        // El constructor de arreglos JSON reemplaza a la lista clásica
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

	    
	    for (Cliente cliente : listaClientes) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                    .add("id", cliente.getId())
                    .add("tipoDocumento", cliente.getTipoDocumento())
            		.add("numeroDocumento", cliente.getNumeroDocumento())
                    .add("nombreRazonSocial", cliente.getNombreRazonSocial())
		            .add("direccion", cliente.getDireccion())
		            .add("telefono", cliente.getTelefono())
		            .add("correo", cliente.getCorreo())
            		.add("fechaIngreso", util.dateToString(cliente.getFechaIngreso()))
            		.add("categoria", cliente.getCategoria())
            		.add("estado", cliente.getEstado());
                
            arrayBuilder.add(objectBuilder);
	    }
	
	    return arrayBuilder.build().toString();
	}
    public Cliente buscarClientePorId(String id) {
        // Regla de validación básica: Que los campos no vengan vacíos o en blanco
        if (id == null || id.trim().isEmpty()) {
            return null; 
        }

        // Llamamos al DAO para buscar al usuario en la base de datos
        Cliente cliente = clienteDAO.buscarClientePorId(id);

        // Si el objeto 'user' no es nulo, significa que las credenciales son correctas
        return cliente;
    }
}
