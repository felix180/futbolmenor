/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller;

import com.spontecorp.littleligues.jpacontroller.exceptions.NonexistentEntityException;
import com.spontecorp.littleligues.model.Cronica;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.littleligues.model.Cronista;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Casper
 */
public class CronicaJpaController implements Serializable {

    public CronicaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cronica cronica) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cronista cronistaId = cronica.getCronistaId();
            if (cronistaId != null) {
                cronistaId = em.getReference(cronistaId.getClass(), cronistaId.getId());
                cronica.setCronistaId(cronistaId);
            }
            em.persist(cronica);
            if (cronistaId != null) {
                cronistaId.getCronicaList().add(cronica);
                cronistaId = em.merge(cronistaId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cronica cronica) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cronica persistentCronica = em.find(Cronica.class, cronica.getId());
            Cronista cronistaIdOld = persistentCronica.getCronistaId();
            Cronista cronistaIdNew = cronica.getCronistaId();
            if (cronistaIdNew != null) {
                cronistaIdNew = em.getReference(cronistaIdNew.getClass(), cronistaIdNew.getId());
                cronica.setCronistaId(cronistaIdNew);
            }
            cronica = em.merge(cronica);
            if (cronistaIdOld != null && !cronistaIdOld.equals(cronistaIdNew)) {
                cronistaIdOld.getCronicaList().remove(cronica);
                cronistaIdOld = em.merge(cronistaIdOld);
            }
            if (cronistaIdNew != null && !cronistaIdNew.equals(cronistaIdOld)) {
                cronistaIdNew.getCronicaList().add(cronica);
                cronistaIdNew = em.merge(cronistaIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cronica.getId();
                if (findCronica(id) == null) {
                    throw new NonexistentEntityException("The cronica with id " + id + " no longer exists.");
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
            Cronica cronica;
            try {
                cronica = em.getReference(Cronica.class, id);
                cronica.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cronica with id " + id + " no longer exists.", enfe);
            }
            Cronista cronistaId = cronica.getCronistaId();
            if (cronistaId != null) {
                cronistaId.getCronicaList().remove(cronica);
                cronistaId = em.merge(cronistaId);
            }
            em.remove(cronica);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cronica> findCronicaEntities() {
        return findCronicaEntities(true, -1, -1);
    }

    public List<Cronica> findCronicaEntities(int maxResults, int firstResult) {
        return findCronicaEntities(false, maxResults, firstResult);
    }

    private List<Cronica> findCronicaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cronica.class));
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

    public Cronica findCronica(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cronica.class, id);
        } finally {
            em.close();
        }
    }

    public int getCronicaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cronica> rt = cq.from(Cronica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
