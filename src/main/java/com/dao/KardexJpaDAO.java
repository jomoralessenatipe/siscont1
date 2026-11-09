package com.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import com.jpa.KardexJpa; // Importamos la entidad JPA correcta

public class KardexJpaDAO {

	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("miPersistenciaPU");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public KardexJpa buscarPorId(String idString) { 
        Integer id = Integer.parseInt(idString);
        EntityManager em = getEntityManager();
        try {
            return em.find(KardexJpa.class, id); 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public boolean insertar(KardexJpa nuevoRegistro) {
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

    public boolean actualizar(KardexJpa registro) {
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

    public List<KardexJpa> listar() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT u FROM KardexJpa u";
            TypedQuery<KardexJpa> query = em.createQuery(jpql, KardexJpa.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    // Método para filtrar los movimientos por un STOCK_ID específico
    public List<KardexJpa> listarPorStock(int stockId) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT u FROM KardexJpa u WHERE u.stockId = :stockId";
            TypedQuery<KardexJpa> query = em.createQuery(jpql, KardexJpa.class);
            query.setParameter("stockId", stockId);
            
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
}