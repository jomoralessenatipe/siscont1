package com.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import com.jpa.AsientoContableJpa;

public class AsientoContableJpaDAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("miPersistenciaPU");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public AsientoContableJpa buscarPorId(String idString) { 
        Integer id = Integer.parseInt(idString);
        EntityManager em = getEntityManager();
        try {
            return em.find(AsientoContableJpa.class, id); 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public boolean insertar(AsientoContableJpa nuevoAsiento) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(nuevoAsiento); 
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

    public boolean actualizar(AsientoContableJpa asiento) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(asiento); 
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

    public boolean eliminar(String idString) {
        Integer id = Integer.parseInt(idString);
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            AsientoContableJpa asiento = em.find(AsientoContableJpa.class, id);
            if (asiento != null) {
                em.remove(asiento);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().rollback();
            return false;
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

    public List<AsientoContableJpa> listar() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT a FROM AsientoContableJpa a";
            TypedQuery<AsientoContableJpa> query = em.createQuery(jpql, AsientoContableJpa.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
}