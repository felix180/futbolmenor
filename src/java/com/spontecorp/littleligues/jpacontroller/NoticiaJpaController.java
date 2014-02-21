/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller;

import com.spontecorp.littleligues.jpacontroller.exceptions.NonexistentEntityException;
import com.spontecorp.littleligues.model.Noticia;
import com.spontecorp.littleligues.model.Noticia_;
import com.spontecorp.littleligues.model.liga.Liga;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class NoticiaJpaController implements Serializable {

    public NoticiaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Crear Noticia
     *
     * @param noticia
     */
    public void create(Noticia noticia) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(noticia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Editar Noticia
     *
     * @param noticia
     * @throws NonexistentEntityException
     * @throws Exception
     */
    public void edit(Noticia noticia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            noticia = em.merge(noticia);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = noticia.getId();
                if (findNoticia(id) == null) {
                    throw new NonexistentEntityException("The noticia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Eliminar Noticia
     *
     * @param id
     * @throws NonexistentEntityException
     */
    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Noticia noticia;
            try {
                noticia = em.getReference(Noticia.class, id);
                noticia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The noticia with id " + id + " no longer exists.", enfe);
            }
            em.remove(noticia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Listar Noticias Administracion
     *
     * @return
     */
    public List<Noticia> findNoticiaEntities() {
        return findNoticiaEntities(true, -1, -1);
    }

    /**
     * Listar Noticias Slide Home
     *
     * @return
     */
    public List<Noticia> findNoticiaSlideEntities() {
        return findNoticiaHomeEntities(false, 4, 0);
    }

    /**
     * Listar Noticias Home
     *
     * @return
     */
    public List<Noticia> findNoticiaHomeEntities() {
        return findNoticiaHomeEntities(false, 8, 4);
    }

    /**
     * Listar Noticias Seccion Noticias
     *
     * @param maxResults
     * @param firstResult
     * @return
     */
    public List<Noticia> findOthersNoticiasEntities(int maxResults, int firstResult) {
        return findOthersNoticiaEntities(false, maxResults, firstResult);
    }

    /**
     *
     * @param maxResults
     * @param firstResult
     * @return
     */
    public List<Noticia> findNoticiaEntities(int maxResults, int firstResult) {
        return findNoticiaEntities(false, maxResults, firstResult);
    }

    /**
     * Listado General de Noticias Ordenados por fecha DESC
     *
     * @param all
     * @param maxResults
     * @param firstResult
     * @return
     */
    private List<Noticia> findNoticiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root noticia = cq.from(Noticia.class);
            cq.select(noticia);
            cq.orderBy(cb.desc(noticia.get(Noticia_.fecha)));
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
     * Listado de Noticias Seccion Noticias Ordenadas por Fecha Orden DESC
     *
     * @param all
     * @param maxResults
     * @param firstResult
     * @return
     */
    private List<Noticia> findOthersNoticiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        Liga liga = null;
        int isPrincipal = 0;
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Noticia> cq = cb.createQuery(Noticia.class);
            Root<Noticia> noticia = cq.from(Noticia.class);
            cq.select(noticia);
            cq.where(cb.and(cb.equal(noticia.get(Noticia_.principal), isPrincipal)),
                    cb.equal(noticia.get(Noticia_.ligaId), liga));
            cq.orderBy(cb.desc(noticia.get(Noticia_.fecha)));
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
     * Listado de Noticias Home Ordenadas por Fecha Orden DESC Marcadas como
     * Principal (principal = 1)
     *
     * @param all
     * @param maxResults
     * @param firstResult
     * @return
     */
    private List<Noticia> findNoticiaHomeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        List<Noticia> noticias = null;
        int isPrincipal = 1;
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root noticia = cq.from(Noticia.class);
            cq.select(noticia);
            cq.where(cb.equal(noticia.get(Noticia_.principal), isPrincipal));
            cq.orderBy(cb.desc(noticia.get(Noticia_.fecha)));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            noticias = q.getResultList();

            return noticias;
        } finally {
            em.close();
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public Noticia findNoticia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Noticia.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Contador Noticias SEccion Noticias No estan marcadas como principal y no
     * pertenecen a ninguna Liga
     *
     * @return
     */
    public int getOthersNoticiaCount() {
        EntityManager em = getEntityManager();
        Liga liga = null;
        int isPrincipal = 0;
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Noticia> noticia = cq.from(Noticia.class);
            cq.select(cb.count(noticia));
            cq.where(cb.and(cb.equal(noticia.get(Noticia_.principal), isPrincipal)),
                    cb.equal(noticia.get(Noticia_.ligaId), liga));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    /**
     * Contador de Noticias Paginador
     *
     * @return
     */
    public int getNoticiaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Noticia> rt = cq.from(Noticia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
