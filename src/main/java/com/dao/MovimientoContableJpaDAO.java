package com.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import com.jpa.MovimientoContableJpa;

public class MovimientoContableJpaDAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("miPersistenciaPU");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public MovimientoContableJpa buscarPorId(String idString) { 
        Integer id = Integer.parseInt(idString);
        EntityManager em = getEntityManager();
        try {
            return em.find(MovimientoContableJpa.class, id); 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public boolean insertar(MovimientoContableJpa nuevoRegistro) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(nuevoRegistro); 
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

    public boolean actualizar(MovimientoContableJpa registro) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(registro); 
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

    public List<MovimientoContableJpa> listar() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT m FROM MovimientoContableJpa m";
            TypedQuery<MovimientoContableJpa> query = em.createQuery(jpql, MovimientoContableJpa.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    // Método para filtrar movimientos por código de cuenta (similar a listarPorStock)
    public List<MovimientoContableJpa> listarPorCuenta(String cuenta) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT m FROM MovimientoContableJpa m WHERE m.cuenta = :cuenta";
            TypedQuery<MovimientoContableJpa> query = em.createQuery(jpql, MovimientoContableJpa.class);
            query.setParameter("cuenta", cuenta);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
}