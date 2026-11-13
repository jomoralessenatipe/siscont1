package com.impl;

import com.dao.AsientoContableJpaDAO;
import com.dao.MovimientoContableJpaDAO;
import com.jpa.AsientoContableJpa;
import com.jpa.MovimientoContableJpa;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ContabilidadJpaImpl {
    
    private MovimientoContableJpaDAO contabilidadDAO = new MovimientoContableJpaDAO();
    private AsientoContableJpaDAO asientoDAO = new AsientoContableJpaDAO();

    public String registrar(JsonObject jsonInput) {
        String mensaje;
        
        int id = jsonInput.containsKey("id") && !jsonInput.isNull("id") ? jsonInput.getInt("id") : 0;
        
        if (id == 0) {
            try {
                // 1. Creamos y guardamos primero la Cabecera (Asiento Contable) una sola vez
                AsientoContableJpa asiento = new AsientoContableJpa();
                
                // Parsear la fecha que viene del input datetime-local (ej. "2026-11-12T21:46")
                String fechaStr = jsonInput.containsKey("fecha") ? jsonInput.getString("fecha") : "";
                if (!fechaStr.isEmpty()) {
                    // Si viene con formato HTML datetime-local, lo convertimos a Date
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    try {
                        asiento.setFecha(sdf.parse(fechaStr));
                    } catch (Exception e) {
                        asiento.setFecha(new Date());
                    }
                } else {
                    asiento.setFecha(new Date());
                }

                asiento.setGlosa(jsonInput.getString("glosa", ""));
                
                boolean exitoAsiento = asientoDAO.insertar(asiento);
                if (!exitoAsiento) {
                    return "Error crítico al guardar el Asiento Contable.";
                }

                // 2. Recorremos la lista de movimientos (Detalle) que vienen en el JSON
                if (jsonInput.containsKey("movimientos")) {
                    JsonArray movimientosArray = jsonInput.getJsonArray("movimientos");
                    
                    for (JsonValue val : movimientosArray) {
                        JsonObject movObj = val.asJsonObject();
                        
                        MovimientoContableJpa registro = new MovimientoContableJpa();
                        registro.setAsiento(asiento); // Relación @ManyToOne con la cabecera
                        registro.setCuenta(movObj.getString("cuenta", ""));
                        
                        double debe = movObj.containsKey("debe") && !movObj.isNull("debe") ? movObj.getJsonNumber("debe").doubleValue() : 0.0;
                        double haber = movObj.containsKey("haber") && !movObj.isNull("haber") ? movObj.getJsonNumber("haber").doubleValue() : 0.0;
                        
                        registro.setDebe(debe);
                        registro.setHaber(haber);

                        contabilidadDAO.insertar(registro);
                    }
                }

                mensaje = "Asiento contable y movimientos creados con éxito";
            } catch (Exception e) {
                e.printStackTrace();
                return "Error crítico al procesar el asiento y sus movimientos.";
            }
        } else {
            // Actualización individual por ID si se requiere
            MovimientoContableJpa registro = contabilidadDAO.buscarPorId(String.valueOf(id));
            if (registro != null) {
                if (jsonInput.containsKey("cuenta")) {
                    registro.setCuenta(jsonInput.getString("cuenta"));
                }
                registro.setDebe(jsonInput.containsKey("debe") ? jsonInput.getJsonNumber("debe").doubleValue() : 0.0);
                registro.setHaber(jsonInput.containsKey("haber") ? jsonInput.getJsonNumber("haber").doubleValue() : 0.0);
                
                if (registro.getAsiento() != null && jsonInput.containsKey("glosa")) {
                    registro.getAsiento().setGlosa(jsonInput.getString("glosa"));
                    asientoDAO.actualizar(registro.getAsiento());
                }

                boolean exito = contabilidadDAO.actualizar(registro);
                mensaje = exito ? "Movimiento contable actualizado con éxito" : "Error crítico al guardar en la base de datos.";
            } else {
                mensaje = "No se encontró el registro a actualizar.";
            }
        }
        return mensaje;
    }

    public String getListaJsonString() {
        List<MovimientoContableJpa> lista = contabilidadDAO.listar();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        for (MovimientoContableJpa registro : lista) {
            String fechaStr = (registro.getAsiento() != null && registro.getAsiento().getFecha() != null) ? registro.getAsiento().getFecha().toString() : "";
            String glosaStr = (registro.getAsiento() != null && registro.getAsiento().getGlosa() != null) ? registro.getAsiento().getGlosa() : "";

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                    .add("id", registro.getId())
                    .add("fecha", fechaStr)
                    .add("glosa", glosaStr)
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
            String fechaStr = (registro.getAsiento() != null && registro.getAsiento().getFecha() != null) ? registro.getAsiento().getFecha().toString() : "";
            String glosaStr = (registro.getAsiento() != null && registro.getAsiento().getGlosa() != null) ? registro.getAsiento().getGlosa() : "";

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                    .add("id", registro.getId())
                    .add("fecha", fechaStr)
                    .add("glosa", glosaStr)
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