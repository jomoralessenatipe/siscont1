package com.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "kardex") // Mapea esta clase a la tabla "kardex" de tu BD
public class KardexJpa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indica que el ID es autoincremental
    private int id;
    
    // Atributos con el estilo exacto del profesor (snake_case para empalmar con Oracle sin @Column)
    private int stock_id;
    private String tipo_movimiento;
    private int cantidad;
    private int comprobante_det_id;
    private String motivo_movimiento;

    // Constructor vacío (Obligatorio para JPA)
    public KardexJpa() {}

    // Constructor con parámetros
    public KardexJpa(int stock_id, String tipo_movimiento, int cantidad, int comprobante_det_id, String motivo_movimiento) {
        this.stock_id = stock_id;
        this.tipo_movimiento = tipo_movimiento;
        this.cantidad = cantidad;
        this.comprobante_det_id = comprobante_det_id;
        this.motivo_movimiento = motivo_movimiento;
    }

    // Getters y Setters (Mantenidos con el estándar del profesor para no romper el Impl)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStockId() { return stock_id; }
    public void setStockId(int stock_id) { this.stock_id = stock_id; }

    public String getTipoMovimiento() { return tipo_movimiento; }
    public void setTipoMovimiento(String tipo_movimiento) { this.tipo_movimiento = tipo_movimiento; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public int getComprobanteDetId() { return comprobante_det_id; }
    public void setComprobanteDetId(int comprobante_det_id) { this.comprobante_det_id = comprobante_det_id; }

    public String getMotivoMovimiento() { return motivo_movimiento; }
    public void setMotivoMovimiento(String motivo_movimiento) { this.motivo_movimiento = motivo_movimiento; }
}