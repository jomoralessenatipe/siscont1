package com.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import com.jpa.PlanContableJpa;

public class PlanContableJpaDAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("miPersistenciaPU");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public PlanContableJpa buscarPorId(String idString) { 
        Integer id = Integer.parseInt(idString);
        EntityManager em = getEntityManager();
        try {
            return em.find(PlanContableJpa.class, id); 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public boolean insertar(PlanContableJpa nuevoRegistro) {
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

    public boolean actualizar(PlanContableJpa registro) {
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

    public List<PlanContableJpa> listar() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT p FROM PlanContableJpa p";
            TypedQuery<PlanContableJpa> query = em.createQuery(jpql, PlanContableJpa.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
}