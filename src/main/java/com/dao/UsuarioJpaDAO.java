package com.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import com.jpa.UsuarioJpa;

public class UsuarioJpaDAO {

    // Se recomienda inicializar el EntityManagerFactory a nivel de aplicación, 
    // pero lo definimos aquí usando el nombre de tu unidad de persistencia (persistence-unit)
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("miPersistenciaPU");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public UsuarioJpa validarUsuario(String nombreUsuario, String password) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT u FROM UsuarioJpa u WHERE u.usuario = :usuario AND u.password = :password";
            TypedQuery<UsuarioJpa> query = em.createQuery(jpql, UsuarioJpa.class);
            query.setParameter("usuario", nombreUsuario);
            query.setParameter("password", password);
            
            return query.getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null; // Retorna null si las credenciales no coinciden
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public UsuarioJpa buscarUsuarioPorId(String idString) { // Cambiado a Integer para coincidir con la PK típica
    	Integer id = Integer.parseInt(idString);
    	EntityManager em = getEntityManager();
        try {
            return em.find(UsuarioJpa.class, id); // JPA busca directamente por Llave Primaria
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public UsuarioJpa buscarUsuarioPorUsuario(String usuario) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT u FROM UsuarioJpa u WHERE u.usuario = :usuario";
            TypedQuery<UsuarioJpa> query = em.createQuery(jpql, UsuarioJpa.class);
            query.setParameter("usuario", usuario);
            
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

    public boolean insertarUsuario(UsuarioJpa nuevoUsuario) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(nuevoUsuario); // Guarda la entidad en la BD
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

    public List<UsuarioJpa> listarUsuarios() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT u FROM UsuarioJpa u";
            TypedQuery<UsuarioJpa> query = em.createQuery(jpql, UsuarioJpa.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    public boolean actualizarUsuario(UsuarioJpa usuarioRegistrado) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(usuarioRegistrado); // Actualiza la entidad modificada en la BD
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
