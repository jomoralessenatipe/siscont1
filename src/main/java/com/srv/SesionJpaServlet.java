package com.srv;

import java.io.IOException;
import jakarta.json.Json;
import jakarta.json.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/SesionJpaServlet")
public class SesionJpaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("usuarioLogueado") == null) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            JsonObject error = Json.createObjectBuilder()
                    .add("error", "Sesión no válida")
                    .build();

            response.getWriter().write(error.toString());
            return;
        }

        String usuario = (String) session.getAttribute("usuarioLogueado");

        JsonObject resultado = Json.createObjectBuilder()
                .add("usuario", usuario)
                .build();

        response.getWriter().write(resultado.toString());
    }
}
