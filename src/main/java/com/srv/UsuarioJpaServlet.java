package com.srv;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Importaciones nativas de Java / Jakarta para manejo de JSON
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;

import com.impl.UsuarioJpaImpl;

@WebServlet("/UsuarioJpaServlet")
public class UsuarioJpaServlet extends HttpServlet {
    
    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3308/mi_sistema", "root", "1234");
    }

    // 1. LEER USUARIOS (GET) - Construyendo JSON nativo
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        UsuarioJpaImpl usuarioImpl = new UsuarioJpaImpl();
        
        
        try {
            
            out.print(usuarioImpl.getListaUsuariosJsonString());
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject errorJson = Json.createObjectBuilder().add("error", e.getMessage()).build();
            out.print(errorJson.toString());
        }
    }

    // 2. CREAR Y ACTUALIZAR (POST) - Leyendo y respondiendo JSON nativo
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        UsuarioJpaImpl usuarioImpl = new UsuarioJpaImpl();
        String mensaje = "";

        JsonObject usuarioInput;
        // El JsonReader lee el cuerpo de la petición (request.getReader) de forma directa
        try (JsonReader jsonReader = Json.createReader(request.getReader())) {
            usuarioInput = jsonReader.readObject();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(Json.createObjectBuilder().add("error", "JSON inválido").build().toString());
            return;
        }

        try {
            mensaje = usuarioImpl.registrarUsuario(usuarioInput);
            
            // Construimos la respuesta exitosa en JSON
            JsonObject respuesta = Json.createObjectBuilder().add("mensaje", mensaje).build();
            out.print(respuesta.toString());
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject errorResponse = Json.createObjectBuilder().add("error", e.getMessage()).build();
            out.print(errorResponse.toString());
        }
    }

}
