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

import com.impl.ProductoJpaImpl;

@WebServlet("/ProductoJpaServlet")
public class ProductoJpaServlet extends HttpServlet {
    
    // 1. LEER USUARIOS (GET) - Construyendo JSON nativo
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        ProductoJpaImpl productoImpl = new ProductoJpaImpl();
        
        
        try {
            
            out.print(productoImpl.getListaJsonString());
            
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
        ProductoJpaImpl productoImpl = new ProductoJpaImpl();
        String mensaje = "";

        JsonObject productoInput;
        // El JsonReader lee el cuerpo de la petición (request.getReader) de forma directa
        try (JsonReader jsonReader = Json.createReader(request.getReader())) {
        	productoInput = jsonReader.readObject();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(Json.createObjectBuilder().add("error", "JSON inválido").build().toString());
            return;
        }

        System.out.println("====== JSON RECIBIDO DESDE VUE ======");
        System.out.println(productoInput.toString());
        System.out.println("=====================================");

        try {
            mensaje = productoImpl.registrar(productoInput);
            JsonObject respuesta = Json.createObjectBuilder().add("mensaje", mensaje).build();
            out.print(respuesta.toString());
            
        } catch (Exception e) {
            e.printStackTrace(); // Muestra el error completo en la consola del servidor

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            JsonObject errorResponse = Json.createObjectBuilder()
                .add("error", e.getMessage() != null
                    ? e.getMessage()
                    : "Error interno al guardar el producto")
                .build();

            out.print(errorResponse.toString());
        }
    }

}
