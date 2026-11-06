package com.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vendedor") // Mapea esta clase a la tabla "proveedor" de tu BD
public class VendedorJpa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indica que el ID es autoincremental
    private int id;
    
    // Si los atributos se llaman igual que las columnas en la BD, no necesitas más anotaciones
    private String nombre;
    private String tipo_vendedor;
    private String tipo_documento;
    private String numero_documento;
    private String telefono;
    private double porcentaje_comision;

    // Constnombretor vacío (Obligatorio para JPA)
    public VendedorJpa() {}

    // Constnombretor con parámetros
    public VendedorJpa(String nombre, String tipo_vendedor, String tipo_documento, String numero_documento, String telefono, double porcentaje_comision) {
    	this.nombre              = nombre;           
    	this.tipo_vendedor       = tipo_vendedor;  
    	this.tipo_documento      = tipo_documento;
    	this.numero_documento    = numero_documento;
    	this.telefono            = telefono;      
    	this.porcentaje_comision = porcentaje_comision;   
    }

    // Getters y Setters (Mantén los mismos que ya tienes)
	public int getId() { return id;	}
	public void setId(int id) { this.id = id; }

	public String getNombre() { return nombre;	}
	public void setNombre(String nombre) { this.nombre = nombre;	}

	public String getTipoVendedor() { return tipo_vendedor;	}
	public void setTipoVendedor(String tipo_vendedor) { this.tipo_vendedor = tipo_vendedor;	}

	public String getTipoDocumento() { return tipo_documento;	}
	public void setTipoDocumento(String tipo_documento) { this.tipo_documento = tipo_documento;	}

	public String getNumeroDocumento() { return numero_documento;	}
	public void setNumeroDocumento(String numero_documento) { this.numero_documento = numero_documento;	}

	public String getTelefono() { return telefono;	}
	public void setTelefono(String telefono) { this.telefono = telefono;	}

	public double getPorcentajeComision() { return porcentaje_comision;	}
	public void setPorcentajeComision(double porcentaje_comision) { this.porcentaje_comision = porcentaje_comision;	}

}
