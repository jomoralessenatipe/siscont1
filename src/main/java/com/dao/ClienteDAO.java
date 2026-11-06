package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.beans.Cliente;

public class ClienteDAO {
    // Datos de conexión
    private final String url = "jdbc:mysql://localhost:3308/mi_sistema?useSSL=false&serverTimezone=UTC";
    private final String dbUser = "root";
    private final String dbPass = "1234";

    public Cliente buscarClientePorId(String id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Cliente clienteEncontrado = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, dbUser, dbPass);

            String sql = "SELECT * FROM clientes WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);

            rs = ps.executeQuery();

            // Si encuentra el registro, llena el objeto Usuario
            if (rs.next()) {
                clienteEncontrado = new Cliente();
                clienteEncontrado.setId(rs.getInt("id"));
                clienteEncontrado.setTipoDocumento(rs.getString("tipo_documento"));
                clienteEncontrado.setNumeroDocumento(rs.getString("numero_doumento"));
                clienteEncontrado.setDireccion(rs.getString("direccion"));
                clienteEncontrado.setTelefono(rs.getString("telefono"));
                clienteEncontrado.setCorreo(rs.getString("correo"));
                clienteEncontrado.setFechaIngreso(rs.getDate("fecha_ingreso"));
                clienteEncontrado.setCategoria(rs.getString("cateogria"));
                clienteEncontrado.setEstado(rs.getString("estado"));
                clienteEncontrado.setUsuarioRegistro(1);
                clienteEncontrado.setFechaRegistro(rs.getDate("fecha_registro"));
                clienteEncontrado.setUsuarioModifico(1);
                clienteEncontrado.setFechaModifico(rs.getDate("fecha_modifico"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Cerramos recursos de forma segura
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return clienteEncontrado; // Retorna el usuario o null si no existía
    }

    public boolean insertarCliente(Cliente nuevoCliente) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean insertadoExitosamente = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, dbUser, dbPass);

            // Sentencia SQL para insertar el nuevo registro
            String sql = "INSERT INTO clientes (tipo_documento, numero_documento, nombre_razon_social, direccion, telefono, correo, fecha_ingreso, categoria, estado, usuario_registro, usuario_modifico)";
            sql+= "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'A', 1, 1)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, nuevoCliente.getTipoDocumento());
            ps.setString(2, nuevoCliente.getNumeroDocumento());
            ps.setString(3, nuevoCliente.getNombreRazonSocial());
            ps.setString(4, nuevoCliente.getDireccion());
            ps.setString(5, nuevoCliente.getTelefono());
            ps.setString(6, nuevoCliente.getCorreo());
            ps.setDate(7, nuevoCliente.getFechaIngreso());
            ps.setString(8, nuevoCliente.getCategoria());
            

            // executeUpdate devuelve el número de filas afectadas. Si es mayor a 0, se guardó.
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                insertadoExitosamente = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return insertadoExitosamente;
    }
    public List<Cliente> listarClientes() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Cliente clienteEncontrado = null;
        List<Cliente> listaClientes = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, dbUser, dbPass);

            String sql = "SELECT * FROM clientes";
            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                clienteEncontrado = new Cliente();
                clienteEncontrado.setId(rs.getInt("id"));
                clienteEncontrado.setTipoDocumento(rs.getString("tipo_documento"));
                clienteEncontrado.setNumeroDocumento(rs.getString("numero_documento"));
                clienteEncontrado.setNombreRazonSocial(rs.getString("nombre_razon_social"));
                clienteEncontrado.setDireccion(rs.getString("direccion"));
                clienteEncontrado.setTelefono(rs.getString("telefono"));
                clienteEncontrado.setCorreo(rs.getString("correo"));
                clienteEncontrado.setFechaIngreso(rs.getDate("fecha_ingreso"));
                clienteEncontrado.setCategoria(rs.getString("categoria"));
                clienteEncontrado.setEstado(rs.getString("estado"));
                clienteEncontrado.setUsuarioRegistro(1);
                clienteEncontrado.setFechaRegistro(rs.getDate("fecha_registro"));
                clienteEncontrado.setUsuarioModifico(1);
                clienteEncontrado.setFechaModifico(rs.getDate("fecha_modifico"));
                listaClientes.add(clienteEncontrado);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return listaClientes;
    }
    public boolean actualizarCliente(Cliente clienteRegistrado) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean actualizadoExitosamente = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, dbUser, dbPass);

            // Sentencia SQL para insertar el nuevo registro
            String sql = "UPDATE clientes SET tipo_documento=?, numero_documento=?, nombre_razon_social=?, direccion=?, telefono=?, correo=?, fecha_ingreso=?, categoria=?, estado=? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, clienteRegistrado.getTipoDocumento());
            ps.setString(2, clienteRegistrado.getNumeroDocumento());
            ps.setString(3, clienteRegistrado.getNombreRazonSocial());
            ps.setString(4, clienteRegistrado.getDireccion());
            ps.setString(5, clienteRegistrado.getTelefono());
            ps.setString(6, clienteRegistrado.getCorreo());
            ps.setDate(7, clienteRegistrado.getFechaIngreso());
            ps.setString(8, clienteRegistrado.getCategoria());
            ps.setString(9, clienteRegistrado.getEstado());
            ps.setInt(10, clienteRegistrado.getId());

            // executeUpdate devuelve el número de filas afectadas. Si es mayor a 0, se guardó.
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
            	actualizadoExitosamente = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return actualizadoExitosamente;
    }
}
