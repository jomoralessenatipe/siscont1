package com.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import com.jpa.ProveedorJpa;

public class ProveedorJpaDAO {

    // Se recomienda inicializar el EntityManagerFactory a nivel de aplicación, 
    // pero lo definimos aquí usando el nombre de tu unidad de persistencia (persistence-unit)
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("oraclePersistenciaPU");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public ProveedorJpa buscarPorId(String idString) { // Cambiado a Integer para coincidir con la PK típica
    	Integer id = Integer.parseInt(idString);
    	EntityManager em = getEntityManager();
        try {
            return em.find(ProveedorJpa.class, id); // JPA busca directamente por Llave Primaria
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public ProveedorJpa buscarPorRazonSocial(String razonSocial) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT u FROM ProveedorJpa u WHERE u.razon_social like :razonSocial";
            TypedQuery<ProveedorJpa> query = em.createQuery(jpql, ProveedorJpa.class);
            query.setParameter("razonSocial", razonSocial);
            
            return query.getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public boolean insertar(ProveedorJpa nuevoRegistro) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(nuevoRegistro); // Guarda la entidad en la BD
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    public List<ProveedorJpa> listar() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT u FROM ProveedorJpa u";
            TypedQuery<ProveedorJpa> query = em.createQuery(jpql, ProveedorJpa.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    public boolean actualizar(ProveedorJpa registro) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(registro); // Actualiza la entidad modificada en la BD
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
}
