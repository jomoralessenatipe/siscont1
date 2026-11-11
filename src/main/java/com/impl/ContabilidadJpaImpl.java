package com.impl;

import com.dao.MovimientoContableJpaDAO;
import com.jpa.MovimientoContableJpa;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import java.util.Date;
import java.util.List;

public class ContabilidadJpaImpl {
    
    private MovimientoContableJpaDAO contabilidadDAO = new MovimientoContableJpaDAO();

    public String registrar(JsonObject jsonInput) {
        String mensaje;
        
        MovimientoContableJpa registro = new MovimientoContableJpa();
        registro.setFecha(new Date());
        registro.setGlosa(jsonInput.getString("glosa"));
        registro.setCuenta(jsonInput.getString("cuenta"));
        registro.setDebe(jsonInput.containsKey("debe") ? jsonInput.getJsonNumber("debe").doubleValue() : 0.0);
        registro.setHaber(jsonInput.containsKey("haber") ? jsonInput.getJsonNumber("haber").doubleValue() : 0.0);
        
        int id = jsonInput.containsKey("id") ? jsonInput.getInt("id") : 0;
        
        if (id == 0) {
            boolean exito = contabilidadDAO.insertar(registro);
            if (exito) {
                mensaje = "Movimiento contable creado con éxito";
            } else {
                mensaje = "Error crítico al guardar en la base de datos.";
            }
        } else {
            registro.setId(id);
            boolean exito = contabilidadDAO.actualizar(registro);
            if (exito) {
                mensaje = "Movimiento contable actualizado con éxito";
            } else {
                mensaje = "Error crítico al guardar en la base de datos.";
            }
        }
        return mensaje;
    }

    public String getListaJsonString() {
        List<MovimientoContableJpa> lista = contabilidadDAO.listar();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        for (MovimientoContableJpa registro : lista) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                    .add("id", registro.getId())
                    .add("fecha", registro.getFecha() != null ? registro.getFecha().toString() : "")
                    .add("glosa", registro.getGlosa() != null ? registro.getGlosa() : "")
                    .add("cuenta", registro.getCuenta() != null ? registro.getCuenta() : "")
                    .add("debe", registro.getDebe())
                    .add("haber", registro.getHaber());
                
            arrayBuilder.add(objectBuilder);
        }
        return arrayBuilder.build().toString();
    }

    public String getListaJsonStringPorCuenta(String cuenta) {
        List<MovimientoContableJpa> lista = contabilidadDAO.listarPorCuenta(cuenta);
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        for (MovimientoContableJpa registro : lista) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                    .add("id", registro.getId())
                    .add("fecha", registro.getFecha() != null ? registro.getFecha().toString() : "")
                    .add("glosa", registro.getGlosa() != null ? registro.getGlosa() : "")
                    .add("cuenta", registro.getCuenta() != null ? registro.getCuenta() : "")
                    .add("debe", registro.getDebe())
                    .add("haber", registro.getHaber());
                
            arrayBuilder.add(objectBuilder);
        }
        return arrayBuilder.build().toString();
    }

    public MovimientoContableJpa buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null; 
        }
        return contabilidadDAO.buscarPorId(id);
    }
}