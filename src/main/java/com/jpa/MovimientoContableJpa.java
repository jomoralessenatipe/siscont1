package com.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "movimiento_contable")
public class MovimientoContableJpa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String cuenta;
    private double debe;
    private double haber;

    // Relación de Detalle a Cabecera (3FN)
    @ManyToOne
    @JoinColumn(name = "asiento_contable_id")
    private AsientoContableJpa asiento;

    public MovimientoContableJpa() {}

    public MovimientoContableJpa(String cuenta, double debe, double haber, AsientoContableJpa asiento) {
        this.cuenta = cuenta;
        this.debe = debe;
        this.haber = haber;
        this.asiento = asiento;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCuenta() { return cuenta; }
    public void setCuenta(String cuenta) { this.cuenta = cuenta; }

    public double getDebe() { return debe; }
    public void setDebe(double debe) { this.debe = debe; }

    public double getHaber() { return haber; }
    public void setHaber(double haber) { this.haber = haber; }

    public AsientoContableJpa getAsiento() { return asiento; }
    public void setAsiento(AsientoContableJpa asiento) { this.asiento = asiento; }
}