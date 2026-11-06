package com.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "producto") // Mapea esta clase a la tabla "proveedor" de tu BD
public class ProductoJpa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indica que el ID es autoincremental
    private int id;
    
    // Si los atributos se llaman igual que las columnas en la BD, no necesitas más anotaciones
    private String nombre;
    private int    proveedor_id;
    private String tipo_producto;
    private int    stock_minimo;
    private int    stock_maximo;

    // Constructor vacío (Obligatorio para JPA)
    public ProductoJpa() {}

    // Constructor con parámetros
    public ProductoJpa(String nombre, int proveedor_id, String tipo_producto, int stock_minimo, int stock_maximo) {
    	this.nombre        = nombre;         
    	this.proveedor_id  = proveedor_id;   
    	this.tipo_producto = tipo_producto;  
    	this.stock_minimo  = stock_minimo;   
    	this.stock_maximo  = stock_maximo;   
    }

	public int    getId() 								{		return id;	}
	public void   setId(int id) 						{		this.id = id;	}
	public String getNombre() 							{		return nombre;	}
	public void   setNombre(String nombre) 				{		this.nombre = nombre;	}
	public int    getProveedorId() 					    {		return proveedor_id;	}
	public void   setProveedorId(int proveedor_id) 	    {		this.proveedor_id = proveedor_id;	}
	public String getTipoProducto() 					{		return tipo_producto;	}
	public void   setTipoProducto(String tipo_producto) {		this.tipo_producto = tipo_producto;	}
	public int    getStockMinimo() 						{		return stock_minimo;	}
	public void   setStockMinimo(int stock_minimo) 		{		this.stock_minimo = stock_minimo;	}
	public int    getStockMaximo() 						{		return stock_maximo;	}
	public void   setStockMaximo(int stock_maximo) 		{		this.stock_maximo = stock_maximo;	}

}
