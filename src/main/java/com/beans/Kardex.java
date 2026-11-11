package com.beans;

public class Kardex {

    protected int id;
    protected int stockId;
    protected String tipoMovimiento; 
    protected int cantidad;
    protected int comprobanteDetId;
    protected String motivoMovimiento;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stock_id) {
        this.stockId = stock_id;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipo_movimiento) {
        this.tipoMovimiento = tipo_movimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getComprobanteDetId() {
        return comprobanteDetId;
    }

    public void setComprobanteDetId(int comprobante_det_id) {
        this.comprobanteDetId = comprobante_det_id;
    }

    public String getMotivoMovimiento() {
        return motivoMovimiento;
    }

    public void setMotivoMovimiento(String motivo_movimiento) {
        this.motivoMovimiento = motivo_movimiento;
    }
}
