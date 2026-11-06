package com.beans;

import java.sql.Date;

public class Cliente {

	
    protected int id; 
    protected String tipoDocumento;                
    protected String numeroDocumento;
    protected String nombreRazonSocial;
    protected String direccion;
    protected String telefono;
    protected String correo;
    protected Date fecha_ingreso;                        
    protected String categoria;                               
    protected String estado;                
    protected int usuario_registro;
    protected Date fecha_registro;
    protected int usuario_modifico;
    protected Date fecha_modifico;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipo_documento) {
		this.tipoDocumento = tipo_documento;
	}
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numero_documento) {
		this.numeroDocumento = numero_documento;
	}
	public String getNombreRazonSocial() {
		return nombreRazonSocial;
	}
	public void setNombreRazonSocial(String nombre_razon_social) {
		this.nombreRazonSocial = nombre_razon_social;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public Date getFechaIngreso() {
		return fecha_ingreso;
	}
	public void setFechaIngreso(Date fecha_ingreso) {
		this.fecha_ingreso = fecha_ingreso;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public int getUsuarioRegistro() {
		return usuario_registro;
	}
	public void setUsuarioRegistro(int usuario_registro) {
		this.usuario_registro = usuario_registro;
	}
	public Date getFechaRegistro() {
		return fecha_registro;
	}
	public void setFechaRegistro(Date fecha_registro) {
		this.fecha_registro = fecha_registro;
	}
	public int getUsuarioModifico() {
		return usuario_modifico;
	}
	public void setUsuarioModifico(int usuario_modifico) {
		this.usuario_modifico = usuario_modifico;
	}
	public Date getFechaModifico() {
		return fecha_modifico;
	}
	public void setFechaModifico(Date fecha_modifico) {
		this.fecha_modifico = fecha_modifico;
	}

    
}
