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

import com.impl.ContabilidadJpaImpl;

@WebServlet("/ContabilidadServlet")
public class ContabilidadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 1. LISTAR MOVIMIENTOS CONTABLES (GET)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        ContabilidadJpaImpl contabilidadImpl = new ContabilidadJpaImpl();
        
        try {
            String cuentaParam = request.getParameter("cuenta");
            
            if (cuentaParam != null && !cuentaParam.trim().isEmpty()) {
                out.print(contabilidadImpl.getListaJsonStringPorCuenta(cuentaParam));
            } else {
                out.print(contabilidadImpl.getListaJsonString());
            }
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject errorJson = Json.createObjectBuilder().add("error", e.getMessage()).build();
            out.print(errorJson.toString());
        }
    }

    // 2. REGISTRAR MOVIMIENTO CONTABLE (POST)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        ContabilidadJpaImpl contabilidadImpl = new ContabilidadJpaImpl();
        String mensaje = "";

        JsonObject contabilidadInput;
        try (JsonReader jsonReader = Json.createReader(request.getReader())) {
            contabilidadInput = jsonReader.readObject();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(Json.createObjectBuilder().add("error", "JSON inválido").build().toString());
            return;
        }

        System.out.println("====== JSON CONTABILIDAD RECIBIDO ======");
        System.out.println(contabilidadInput.toString());
        System.out.println("========================================");

        try {
            mensaje = contabilidadImpl.registrar(contabilidadInput);
            JsonObject respuesta = Json.createObjectBuilder().add("mensaje", mensaje).build();
            out.print(respuesta.toString());
            
        } catch (Exception e) {
            e.printStackTrace(); 
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject errorResponse = Json.createObjectBuilder()
                .add("error", e.getMessage() != null ? e.getMessage() : "Error interno al guardar en Contabilidad")
                .build();
            out.print(errorResponse.toString());
        }
    }
}