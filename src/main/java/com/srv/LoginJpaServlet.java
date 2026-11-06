package com.srv;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.impl.UsuarioJpaImpl;

@WebServlet("/LoginJpaServlet")
public class LoginJpaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UsuarioJpaImpl usuarioImpl = new UsuarioJpaImpl();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String userForm = request.getParameter("txtUsuario");
        String passForm = request.getParameter("txtPassword");

        // Validación básica de datos recibidos
        if (userForm == null || userForm.trim().isEmpty()
                || passForm == null || passForm.isEmpty()) {

            response.sendRedirect(
                request.getContextPath() + "/index.html?error=datos"
            );
            return;
        }

        boolean esValido = usuarioImpl.validarLogin(
            userForm.trim(), passForm
        );

        if (esValido) {

            // Evitar reutilizar una sesión anterior
            HttpSession sesionAnterior = request.getSession(false);

            if (sesionAnterior != null) {
                sesionAnterior.invalidate();
            }

            // Crear una nueva sesión autenticada
            HttpSession session = request.getSession(true);

            // Guardar el usuario autenticado
            session.setAttribute("usuarioLogueado", userForm.trim());

            // Tiempo máximo de inactividad: 30 minutos
            session.setMaxInactiveInterval(30 * 60);

            // Ingresar al sistema
            response.sendRedirect(
                request.getContextPath() + "/dashboard.html"
            );

        } else {

            response.sendRedirect(
                request.getContextPath() + "/index.html?error=credenciales"
            );
        }
    }
}
