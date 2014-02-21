/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller;

import com.spontecorp.littleligues.jpacontroller.exceptions.NonexistentEntityException;
import com.spontecorp.littleligues.model.Patrocinante;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Casper
 */
public class PatrocinanteJpaController implements Serializable {

    public PatrocinanteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Patrocinante patrocinante) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(patrocinante);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Patrocinante patrocinante) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            patrocinante = em.merge(patrocinante);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = patrocinante.getId();
                if (findPatrocinante(id) == null) {
                    throw new NonexistentEntityException("The patrocinante with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Patrocinante patrocinante;
            try {
                patrocinante = em.getReference(Patrocinante.class, id);
                patrocinante.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The patrocinante with id " + id + " no longer exists.", enfe);
            }
            em.remove(patrocinante);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Patrocinante> findPatrocinanteEntities() {
        return findPatrocinanteEntities(true, -1, -1);
    }

    public List<Patrocinante> findPatrocinanteEntities(int maxResults, int firstResult) {
        return findPatrocinanteEntities(false, maxResults, firstResult);
    }

    private List<Patrocinante> findPatrocinanteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Patrocinante.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Patrocinante findPatrocinante(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Patrocinante.class, id);
        } finally {
            em.close();
        }
    }

    public int getPatrocinanteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Patrocinante> rt = cq.from(Patrocinante.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
