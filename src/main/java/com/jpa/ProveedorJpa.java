package com.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "proveedor") // Mapea esta clase a la tabla "proveedor" de tu BD
public class ProveedorJpa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indica que el ID es autoincremental
    private int id;
    
    // Si los atributos se llaman igual que las columnas en la BD, no necesitas más anotaciones
    private String ruc;
    private String razon_social;
    private String telefono;
    private String direccion;
    private int    distrito_id;
    private int    provincia_id;
    private int    departamento_id;

    // Constructor vacío (Obligatorio para JPA)
    public ProveedorJpa() {}

    // Constructor con parámetros
    public ProveedorJpa(String ruc, String razon_social, String telefono, String direccion, int distrito_id, int provincia_id, int departamento_id) {
    	this.ruc             = ruc;           
    	this.razon_social    = razon_social;  
    	this.telefono        = telefono;      
    	this.direccion       = direccion;     
    	this.distrito_id     = distrito_id;   
    	this.provincia_id    = provincia_id;  
    	this.departamento_id = departamento_id;
    }

    // Getters y Setters (Mantén los mismos que ya tienes)
	public int getId() { return id;	}
	public void setId(int id) { this.id = id; }

	public String getRuc() { return ruc;	}

	public void setRuc(String ruc) { this.ruc = ruc;	}

	public String getRazonSocial() { return razon_social;	}

	public void setRazonSocial(String razon_social) { this.razon_social = razon_social;	}

	public String getTelefono() { return telefono;	}

	public void setTelefono(String telefono) { this.telefono = telefono;	}

	public String getDireccion() { return direccion;	}

	public void setDireccion(String direccion) { this.direccion = direccion;	}

	public int getDistritoId() { return distrito_id;	}

	public void setDistritoId(int distrito_id) { this.distrito_id = distrito_id;	}

	public int getProvinciaId() { return provincia_id;	}

	public void setProvinciaId(int provincia_id) { this.provincia_id = provincia_id;	}

	public int getDepartamentoId() { return departamento_id;	}

	public void setDepartamentoId(int departamento_id) { this.departamento_id = departamento_id;	}
}
