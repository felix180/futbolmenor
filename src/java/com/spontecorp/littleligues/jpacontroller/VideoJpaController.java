/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller;

import com.spontecorp.littleligues.jpacontroller.exceptions.NonexistentEntityException;
import com.spontecorp.littleligues.model.Video;
import com.spontecorp.littleligues.model.Video_;
import com.spontecorp.littleligues.model.liga.Liga;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Casper
 */
public class VideoJpaController implements Serializable {

    public VideoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Video video) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(video);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Video video) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            video = em.merge(video);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = video.getId();
                if (findVideo(id) == null) {
                    throw new NonexistentEntityException("The video with id " + id + " no longer exists.");
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
            Video video;
            try {
                video = em.getReference(Video.class, id);
                video.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The video with id " + id + " no longer exists.", enfe);
            }
            em.remove(video);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Video> findVideoEntities() {
        return findVideoEntities(true, -1, -1);
    }

    public List<Video> findVideoEntities(int maxResults, int firstResult) {
        return findVideoEntities(false, maxResults, firstResult);
    }
    
    /**
     * Lista de los 3 primeros Videos asociados a la Liga Seleccionada
     *
     * @param liga
     * @return
     */
    public List<Video> findVideosLigaEntities(Liga liga) {
        return findVideosLiga(liga, false, 3, 0);
    }

    private List<Video> findVideoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root video = cq.from(Video.class);
            cq.select(video);
            cq.orderBy(cb.desc(video.get(Video_.fecha)));
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
    
    /**
     * Lista de Videos asociados a la Liga Seleccionada Ordenados por fecha
     * Orden DESC
     *
     * @param liga
     * @param all
     * @param maxResults
     * @param firstResult
     * @return
     */
    private List<Video> findVideosLiga(Liga liga, boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        List<Video> videos = null;
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root video = cq.from(Video.class);
            cq.select(video);
            cq.where(cb.equal(video.get(Video_.ligaId), liga));
            cq.orderBy(cb.desc(video.get(Video_.fecha)));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            videos = q.getResultList();
            return videos;
        } finally {
            em.close();
        }
    }

    public Video findVideo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Video.class, id);
        } finally {
            em.close();
        }
    }

    public int getVideoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Video> rt = cq.from(Video.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
