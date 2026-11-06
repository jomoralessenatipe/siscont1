package com.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios") // Mapea esta clase a la tabla "usuarios" de tu BD
public class UsuarioJpa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indica que el ID es autoincremental
    private int id;
    
    // Si los atributos se llaman igual que las columnas en la BD, no necesitas más anotaciones
    private String usuario;
    private String password;

    // Constructor vacío (Obligatorio para JPA)
    public UsuarioJpa() {}

    // Constructor con parámetros
    public UsuarioJpa(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
    }

    // Getters y Setters (Mantén los mismos que ya tienes)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
