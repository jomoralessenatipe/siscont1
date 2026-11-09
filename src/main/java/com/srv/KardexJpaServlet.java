package com.srv;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import com.impl.KardexJpaImpl; // Esta clase la crearemos en el siguiente paso

@WebServlet("/KardexJpaServlet")
public class KardexJpaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 1. LEER MOVIMIENTOS DEL KARDEX (GET)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        KardexJpaImpl kardexImpl = new KardexJpaImpl();
        
        try {
            // Leemos el parámetro stockId enviado desde la URL (ej: KardexJpaServlet?stockId=5)
            String stockIdParam = request.getParameter("stockId");
            
            if (stockIdParam != null && !stockIdParam.trim().isEmpty()) {
                int stockId = Integer.parseInt(stockIdParam);
                // Retorna los movimientos de ese stock específico en JSON
                out.print(kardexImpl.getListaJsonStringPorStock(stockId));
            } else {
                // Si no se envía parámetro, retorna todos los movimientos generales
                out.print(kardexImpl.getListaJsonString());
            }
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject errorJson = Json.createObjectBuilder().add("error", e.getMessage()).build();
            out.print(errorJson.toString());
        }
    }

    // 2. REGISTRAR ENTRADA/SALIDA EN EL KARDEX (POST)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        KardexJpaImpl kardexImpl = new KardexJpaImpl();
        String mensaje = "";

        JsonObject kardexInput;
        try (JsonReader jsonReader = Json.createReader(request.getReader())) {
            kardexInput = jsonReader.readObject();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(Json.createObjectBuilder().add("error", "JSON inválido").build().toString());
            return;
        }

        System.out.println("====== JSON KARDEX RECIBIDO ======");
        System.out.println(kardexInput.toString());
        System.out.println("==================================");

        try {
            mensaje = kardexImpl.registrar(kardexInput);
            JsonObject respuesta = Json.createObjectBuilder().add("mensaje", mensaje).build();
            out.print(respuesta.toString());
            
        } catch (Exception e) {
            e.printStackTrace(); 
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject errorResponse = Json.createObjectBuilder()
                .add("error", e.getMessage() != null ? e.getMessage() : "Error interno al guardar en el Kardex")
                .build();
            out.print(errorResponse.toString());
        }
    }
}
