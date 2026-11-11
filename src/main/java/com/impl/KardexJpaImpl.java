package com.impl;

import com.dao.KardexJpaDAO;
import com.jpa.KardexJpa;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import java.util.List;

public class KardexJpaImpl {
    
    private KardexJpaDAO kardexDAO = new KardexJpaDAO();
    
    public String registrar(String id, 
                            String stockId, 
                            String tipoMovimiento, 
                            String cantidad, 
                            String comprobanteDetId, 
                            String motivoMovimiento) {
        // 1. Regla de validación: Que los campos no estén vacíos
        if (    id               == null || id              .trim().isEmpty() ||
                stockId          == null || stockId         .trim().isEmpty() ||
                tipoMovimiento   == null || tipoMovimiento  .trim().isEmpty() ||
                cantidad         == null || cantidad        .trim().isEmpty() ||
                comprobanteDetId == null || comprobanteDetId.trim().isEmpty() ||
                motivoMovimiento == null || motivoMovimiento.trim().isEmpty() ) {
            return "Todos los campos son obligatorios."; 
        }

        KardexJpa registroExistente = kardexDAO.buscarPorId(id);
        
        if (registroExistente == null) {
            KardexJpa nuevo = new KardexJpa();
            nuevo.setStockId(Integer.parseInt(stockId));
            nuevo.setTipoMovimiento(tipoMovimiento);
            nuevo.setCantidad(Integer.parseInt(cantidad));
            nuevo.setComprobanteDetId(Integer.parseInt(comprobanteDetId));
            nuevo.setMotivoMovimiento(motivoMovimiento);
            
            boolean exito = kardexDAO.insertar(nuevo);
    
            if (exito) {
                return "OK"; 
            } else {
                return "Error crítico al guardar en la base de datos.";
            }
        } else {
            registroExistente.setId(Integer.parseInt(id));            
            registroExistente.setStockId(Integer.parseInt(stockId));
            registroExistente.setTipoMovimiento(tipoMovimiento);
            registroExistente.setCantidad(Integer.parseInt(cantidad));
            registroExistente.setComprobanteDetId(Integer.parseInt(comprobanteDetId));
            registroExistente.setMotivoMovimiento(motivoMovimiento);

            boolean exito = kardexDAO.actualizar(registroExistente);
    
            if (exito) {
                return "OK"; 
            } else {
                return "Error crítico al guardar en la base de datos.";
            }
        }
    }

    public String registrar(JsonObject kardexInput) {
        String mensaje;
        
        KardexJpa registro = new KardexJpa();
        registro.setStockId(kardexInput.getInt("stockId"));
        registro.setTipoMovimiento(kardexInput.getString("tipoMovimiento"));
        registro.setCantidad(kardexInput.getInt("cantidad"));
        registro.setComprobanteDetId(kardexInput.getInt("comprobanteDetId"));
        registro.setMotivoMovimiento(kardexInput.getString("motivoMovimiento"));
        
        int id = kardexInput.containsKey("id") ? kardexInput.getInt("id") : 0;
        
        System.out.println("KardexJpaImpl - id = " + id);

        if (id == 0) {
            boolean exito = kardexDAO.insertar(registro);
            if (exito) {
                mensaje = "Movimiento de Kardex creado con éxito";
            } else {
                mensaje = "Error crítico al guardar en la base de datos.";
            }
        } else {
            registro.setId(id);
            boolean exito = kardexDAO.actualizar(registro);
            if (exito) {
                mensaje = "Movimiento de Kardex actualizado con éxito";
            } else {
                mensaje = "Error crítico al guardar en la base de datos.";
            }
        }
        return mensaje;
    }

    public String getListaJsonString() {
        List<KardexJpa> lista = kardexDAO.listar();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        for (KardexJpa registro : lista) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                    .add("id", registro.getId())
                    .add("stockId", registro.getStockId())
                    .add("tipoMovimiento", registro.getTipoMovimiento())
                    .add("cantidad", registro.getCantidad())
                    .add("comprobanteDetId", registro.getComprobanteDetId())
                    .add("motivoMovimiento", registro.getMotivoMovimiento());
                
            arrayBuilder.add(objectBuilder);
        }
        return arrayBuilder.build().toString();
    }

    public String getListaJsonStringPorStock(int stockId) {
        List<KardexJpa> lista = kardexDAO.listarPorStock(stockId);
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        for (KardexJpa registro : lista) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                    .add("id", registro.getId())
                    .add("stockId", registro.getStockId())
                    .add("tipoMovimiento", registro.getTipoMovimiento())
                    .add("cantidad", registro.getCantidad())
                    .add("comprobanteDetId", registro.getComprobanteDetId())
                    .add("motivoMovimiento", registro.getMotivoMovimiento());
                
            arrayBuilder.add(objectBuilder);
        }
        return arrayBuilder.build().toString();
    }

    public KardexJpa buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null; 
        }
        return kardexDAO.buscarPorId(id);
    }
}