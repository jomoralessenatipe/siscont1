package com.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "movimiento_contable")
public class MovimientoContableJpa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Temporal(TemporalType.DATE)
    private Date fecha;
    private String glosa;
    private String cuenta;
    private double debe;
    private double haber;

    // Constructor vacío (Obligatorio para JPA)
    public MovimientoContableJpa() {}

    // Constructor con parámetros
    public MovimientoContableJpa(Date fecha, String glosa, String cuenta, double debe, double haber) {
        this.fecha = fecha;
        this.glosa = glosa;
        this.cuenta = cuenta;
        this.debe = debe;
        this.haber = haber;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getGlosa() { return glosa; }
    public void setGlosa(String glosa) { this.glosa = glosa; }

    public String getCuenta() { return cuenta; }
    public void setCuenta(String cuenta) { this.cuenta = cuenta; }

    public double getDebe() { return debe; }
    public void setDebe(double debe) { this.debe = debe; }

    public double getHaber() { return haber; }
    public void setHaber(double haber) { this.haber = haber; }
}