package com.impl;

import com.dao.UsuarioJpaDAO;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import com.jpa.UsuarioJpa;
import java.util.List;

public class UsuarioJpaImpl {
    
    // Instanciamos el DAO para poder usarlo
    private UsuarioJpaDAO usuarioDAO = new UsuarioJpaDAO();

    public boolean validarLogin(String usuario, String password) {
        // Regla de validación básica: Que los campos no vengan vacíos o en blanco
        if (usuario == null || usuario.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return false; 
        }

        // Llamamos al DAO para buscar al usuario en la base de datos
        UsuarioJpa user = usuarioDAO.validarUsuario(usuario, password);

        // Si el objeto 'user' no es nulo, significa que las credenciales son correctas
        return user != null;
    }
 // ... (Mantén tu código actual y el método validarLogin) ...

    public String registrarUsuario(String id, String usuario, String password) {
        // 1. Regla de validación: Que los campos no estén vacíos
        if (usuario == null || usuario.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return "Todos los campos son obligatorios."; 
        }

        // 2. Regla de validación: Verificar que el nombre de usuario esté disponible
        // Usamos el buscarUsuario que creamos al principio, pasándole cualquier password ("") 
        // o mejor modificamos para validar disponibilidad. En este caso, buscaremos si existe exactamente.
        UsuarioJpa usuarioExistente = usuarioDAO.buscarUsuarioPorUsuario(usuario);
        
        // NOTA: Para ser más precisos con la disponibilidad, lo ideal sería buscar solo por nombre. 
        // Pero usando tus métodos actuales, si el usuario ya existe con cualquier combinación, evitamos duplicar.
        if (usuarioExistente != null) {
            return "El nombre de usuario ya está registrado.";
        }

        usuarioExistente = usuarioDAO.buscarUsuarioPorId(id);
        
        if (usuarioExistente == null) {
	        UsuarioJpa nuevo = new UsuarioJpa(usuario, password);
	        boolean exito = usuarioDAO.insertarUsuario(nuevo);
	
	        if (exito) {
	            return "OK"; // Usamos "OK" como señal de que todo salió perfecto
	        } else {
	            return "Error crítico al guardar en la base de datos.";
	        }
        } else {
        	usuarioExistente.setId(Integer.parseInt(id));
        	usuarioExistente.setUsuario(usuario);
        	usuarioExistente.setPassword(password);
	        boolean exito = usuarioDAO.actualizarUsuario(usuarioExistente);
	
	        if (exito) {
	            return "OK"; // Usamos "OK" como señal de que todo salió perfecto
	        } else {
	            return "Error crítico al guardar en la base de datos.";
	        }
        }
    }
    public String registrarUsuario(JsonObject usuarioInput) {
    	String mensaje;
        // Extraemos las propiedades del objeto JSON recibido
        String usuario = usuarioInput.getString("usuario");
        String password = usuarioInput.getString("password");
        UsuarioJpa usuarioRegistro = new UsuarioJpa(usuario, password);
        
        // Manejo seguro del ID si viene o no en el JSON (es opcional al crear)
        int id = usuarioInput.containsKey("id") ? usuarioInput.getInt("id") : 0;

        if (id == 0) {
	        boolean exito = usuarioDAO.insertarUsuario(usuarioRegistro);
	        if (exito) {
                mensaje = "Usuario creado con éxito";
	        } else {
	            mensaje = "Error crítico al guardar en la base de datos.";
	        }
        } else {
        	usuarioRegistro.setId(id);
	        boolean exito = usuarioDAO.actualizarUsuario(usuarioRegistro);
	
	        if (exito) {
                mensaje = "Usuario actualizado con éxito";
	        } else {
	            mensaje = "Error crítico al guardar en la base de datos.";
	        }
        }
        return mensaje;
    }
    public String listarUsuarios() {

        List<UsuarioJpa> listaUsuarios = usuarioDAO.listarUsuarios();
        String vcHtml = "";

        vcHtml="";
        vcHtml+="<table border=1>";
        vcHtml+="<tr>";
        vcHtml+="<td>Id</td>";
        vcHtml+="<td>Usuario</td>";
        vcHtml+="</tr>";
        
        for (UsuarioJpa usuario : listaUsuarios) {
        	vcHtml+="<tr>";
        	vcHtml+="<td>";
        	vcHtml+=usuario.getId();
        	vcHtml+="</td>";
        	vcHtml+="<td> <a href=UsuarioServlet?Id="+usuario.getId()+">";
        	vcHtml+=usuario.getUsuario();
        	vcHtml+="</a> </td>";
        	vcHtml+="</tr>";
        	vcHtml+="<br>";
        }
        vcHtml+="</table>";
        vcHtml+="<br>";
        vcHtml+="<br>";

        return vcHtml;
    }
    public String getListaUsuariosJsonString () {
	    List<UsuarioJpa> listaUsuarios = usuarioDAO.listarUsuarios();

        // El constructor de arreglos JSON reemplaza a la lista clásica
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

	    
	    for (UsuarioJpa usuario : listaUsuarios) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                    .add("id", usuario.getId())
                    .add("usuario", usuario.getUsuario());
                
            arrayBuilder.add(objectBuilder);
	    }
	
	    return arrayBuilder.build().toString();
	}
    public UsuarioJpa buscarUsuarioPorId(String id) {
        // Regla de validación básica: Que los campos no vengan vacíos o en blanco
        if (id == null || id.trim().isEmpty()) {
            return null; 
        }

        // Llamamos al DAO para buscar al usuario en la base de datos
        UsuarioJpa user = usuarioDAO.buscarUsuarioPorId(id);

        // Si el objeto 'user' no es nulo, significa que las credenciales son correctas
        return user;
    }
}
