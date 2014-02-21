/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller.torneo;

import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.IllegalOrphanException;
import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.littleligues.model.torneo.Evento;
import com.spontecorp.littleligues.model.torneo.TipoEvento;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class TipoEventoJpaController implements Serializable {

    public TipoEventoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoEvento tipoEvento) {
        if (tipoEvento.getEventoList() == null) {
            tipoEvento.setEventoList(new ArrayList<Evento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Evento> attachedEventoList = new ArrayList<Evento>();
            for (Evento eventoListEventoToAttach : tipoEvento.getEventoList()) {
                eventoListEventoToAttach = em.getReference(eventoListEventoToAttach.getClass(), eventoListEventoToAttach.getId());
                attachedEventoList.add(eventoListEventoToAttach);
            }
            tipoEvento.setEventoList(attachedEventoList);
            em.persist(tipoEvento);
            for (Evento eventoListEvento : tipoEvento.getEventoList()) {
                TipoEvento oldTipoEventoIdOfEventoListEvento = eventoListEvento.getTipoEventoId();
                eventoListEvento.setTipoEventoId(tipoEvento);
                eventoListEvento = em.merge(eventoListEvento);
                if (oldTipoEventoIdOfEventoListEvento != null) {
                    oldTipoEventoIdOfEventoListEvento.getEventoList().remove(eventoListEvento);
                    oldTipoEventoIdOfEventoListEvento = em.merge(oldTipoEventoIdOfEventoListEvento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoEvento tipoEvento) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoEvento persistentTipoEvento = em.find(TipoEvento.class, tipoEvento.getId());
            List<Evento> eventoListOld = persistentTipoEvento.getEventoList();
            List<Evento> eventoListNew = tipoEvento.getEventoList();
            List<String> illegalOrphanMessages = null;
            for (Evento eventoListOldEvento : eventoListOld) {
                if (!eventoListNew.contains(eventoListOldEvento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Evento " + eventoListOldEvento + " since its tipoEventoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Evento> attachedEventoListNew = new ArrayList<Evento>();
            for (Evento eventoListNewEventoToAttach : eventoListNew) {
                eventoListNewEventoToAttach = em.getReference(eventoListNewEventoToAttach.getClass(), eventoListNewEventoToAttach.getId());
                attachedEventoListNew.add(eventoListNewEventoToAttach);
            }
            eventoListNew = attachedEventoListNew;
            tipoEvento.setEventoList(eventoListNew);
            tipoEvento = em.merge(tipoEvento);
            for (Evento eventoListNewEvento : eventoListNew) {
                if (!eventoListOld.contains(eventoListNewEvento)) {
                    TipoEvento oldTipoEventoIdOfEventoListNewEvento = eventoListNewEvento.getTipoEventoId();
                    eventoListNewEvento.setTipoEventoId(tipoEvento);
                    eventoListNewEvento = em.merge(eventoListNewEvento);
                    if (oldTipoEventoIdOfEventoListNewEvento != null && !oldTipoEventoIdOfEventoListNewEvento.equals(tipoEvento)) {
                        oldTipoEventoIdOfEventoListNewEvento.getEventoList().remove(eventoListNewEvento);
                        oldTipoEventoIdOfEventoListNewEvento = em.merge(oldTipoEventoIdOfEventoListNewEvento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoEvento.getId();
                if (findTipoEvento(id) == null) {
                    throw new NonexistentEntityException("The tipoEvento with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoEvento tipoEvento;
            try {
                tipoEvento = em.getReference(TipoEvento.class, id);
                tipoEvento.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoEvento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Evento> eventoListOrphanCheck = tipoEvento.getEventoList();
            for (Evento eventoListOrphanCheckEvento : eventoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoEvento (" + tipoEvento + ") cannot be destroyed since the Evento " + eventoListOrphanCheckEvento + " in its eventoList field has a non-nullable tipoEventoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoEvento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoEvento> findTipoEventoEntities() {
        return findTipoEventoEntities(true, -1, -1);
    }

    public List<TipoEvento> findTipoEventoEntities(int maxResults, int firstResult) {
        return findTipoEventoEntities(false, maxResults, firstResult);
    }

    private List<TipoEvento> findTipoEventoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoEvento.class));
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

    public TipoEvento findTipoEvento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoEvento.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoEventoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoEvento> rt = cq.from(TipoEvento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
