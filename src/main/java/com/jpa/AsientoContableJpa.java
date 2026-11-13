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
@Table(name = "asiento_contable")
public class AsientoContableJpa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    private String glosa;

    public AsientoContableJpa() {}

    public AsientoContableJpa(Date fecha, String glosa) {
        this.fecha = fecha;
        this.glosa = glosa;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getGlosa() { return glosa; }
    public void setGlosa(String glosa) { this.glosa = glosa; }
}