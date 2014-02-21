/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller.torneo;

import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.littleligues.model.torneo.TipoArbitro;
import com.spontecorp.littleligues.model.torneo.Partido;
import com.spontecorp.littleligues.model.torneo.Arbitro;
import com.spontecorp.littleligues.model.torneo.ArbitroPartido;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class ArbitroPartidoJpaController implements Serializable {

    public ArbitroPartidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ArbitroPartido arbitroPartido) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoArbitro tipoArbitroId = arbitroPartido.getTipoArbitroId();
            if (tipoArbitroId != null) {
                tipoArbitroId = em.getReference(tipoArbitroId.getClass(), tipoArbitroId.getId());
                arbitroPartido.setTipoArbitroId(tipoArbitroId);
            }
            Partido partidoId = arbitroPartido.getPartidoId();
            if (partidoId != null) {
                partidoId = em.getReference(partidoId.getClass(), partidoId.getId());
                arbitroPartido.setPartidoId(partidoId);
            }
            Arbitro arbitroId = arbitroPartido.getArbitroId();
            if (arbitroId != null) {
                arbitroId = em.getReference(arbitroId.getClass(), arbitroId.getId());
                arbitroPartido.setArbitroId(arbitroId);
            }
            em.persist(arbitroPartido);
            if (tipoArbitroId != null) {
                tipoArbitroId.getArbitroPartidoList().add(arbitroPartido);
                tipoArbitroId = em.merge(tipoArbitroId);
            }
            if (partidoId != null) {
                partidoId.getArbitroPartidoList().add(arbitroPartido);
                partidoId = em.merge(partidoId);
            }
            if (arbitroId != null) {
                arbitroId.getArbitroPartidoList().add(arbitroPartido);
                arbitroId = em.merge(arbitroId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ArbitroPartido arbitroPartido) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ArbitroPartido persistentArbitroPartido = em.find(ArbitroPartido.class, arbitroPartido.getId());
            TipoArbitro tipoArbitroIdOld = persistentArbitroPartido.getTipoArbitroId();
            TipoArbitro tipoArbitroIdNew = arbitroPartido.getTipoArbitroId();
            Partido partidoIdOld = persistentArbitroPartido.getPartidoId();
            Partido partidoIdNew = arbitroPartido.getPartidoId();
            Arbitro arbitroIdOld = persistentArbitroPartido.getArbitroId();
            Arbitro arbitroIdNew = arbitroPartido.getArbitroId();
            if (tipoArbitroIdNew != null) {
                tipoArbitroIdNew = em.getReference(tipoArbitroIdNew.getClass(), tipoArbitroIdNew.getId());
                arbitroPartido.setTipoArbitroId(tipoArbitroIdNew);
            }
            if (partidoIdNew != null) {
                partidoIdNew = em.getReference(partidoIdNew.getClass(), partidoIdNew.getId());
                arbitroPartido.setPartidoId(partidoIdNew);
            }
            if (arbitroIdNew != null) {
                arbitroIdNew = em.getReference(arbitroIdNew.getClass(), arbitroIdNew.getId());
                arbitroPartido.setArbitroId(arbitroIdNew);
            }
            arbitroPartido = em.merge(arbitroPartido);
            if (tipoArbitroIdOld != null && !tipoArbitroIdOld.equals(tipoArbitroIdNew)) {
                tipoArbitroIdOld.getArbitroPartidoList().remove(arbitroPartido);
                tipoArbitroIdOld = em.merge(tipoArbitroIdOld);
            }
            if (tipoArbitroIdNew != null && !tipoArbitroIdNew.equals(tipoArbitroIdOld)) {
                tipoArbitroIdNew.getArbitroPartidoList().add(arbitroPartido);
                tipoArbitroIdNew = em.merge(tipoArbitroIdNew);
            }
            if (partidoIdOld != null && !partidoIdOld.equals(partidoIdNew)) {
                partidoIdOld.getArbitroPartidoList().remove(arbitroPartido);
                partidoIdOld = em.merge(partidoIdOld);
            }
            if (partidoIdNew != null && !partidoIdNew.equals(partidoIdOld)) {
                partidoIdNew.getArbitroPartidoList().add(arbitroPartido);
                partidoIdNew = em.merge(partidoIdNew);
            }
            if (arbitroIdOld != null && !arbitroIdOld.equals(arbitroIdNew)) {
                arbitroIdOld.getArbitroPartidoList().remove(arbitroPartido);
                arbitroIdOld = em.merge(arbitroIdOld);
            }
            if (arbitroIdNew != null && !arbitroIdNew.equals(arbitroIdOld)) {
                arbitroIdNew.getArbitroPartidoList().add(arbitroPartido);
                arbitroIdNew = em.merge(arbitroIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = arbitroPartido.getId();
                if (findArbitroPartido(id) == null) {
                    throw new NonexistentEntityException("The arbitroPartido with id " + id + " no longer exists.");
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
            ArbitroPartido arbitroPartido;
            try {
                arbitroPartido = em.getReference(ArbitroPartido.class, id);
                arbitroPartido.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The arbitroPartido with id " + id + " no longer exists.", enfe);
            }
            TipoArbitro tipoArbitroId = arbitroPartido.getTipoArbitroId();
            if (tipoArbitroId != null) {
                tipoArbitroId.getArbitroPartidoList().remove(arbitroPartido);
                tipoArbitroId = em.merge(tipoArbitroId);
            }
            Partido partidoId = arbitroPartido.getPartidoId();
            if (partidoId != null) {
                partidoId.getArbitroPartidoList().remove(arbitroPartido);
                partidoId = em.merge(partidoId);
            }
            Arbitro arbitroId = arbitroPartido.getArbitroId();
            if (arbitroId != null) {
                arbitroId.getArbitroPartidoList().remove(arbitroPartido);
                arbitroId = em.merge(arbitroId);
            }
            em.remove(arbitroPartido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ArbitroPartido> findArbitroPartidoEntities() {
        return findArbitroPartidoEntities(true, -1, -1);
    }

    public List<ArbitroPartido> findArbitroPartidoEntities(int maxResults, int firstResult) {
        return findArbitroPartidoEntities(false, maxResults, firstResult);
    }

    private List<ArbitroPartido> findArbitroPartidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ArbitroPartido.class));
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

    public ArbitroPartido findArbitroPartido(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ArbitroPartido.class, id);
        } finally {
            em.close();
        }
    }

    public int getArbitroPartidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ArbitroPartido> rt = cq.from(ArbitroPartido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
