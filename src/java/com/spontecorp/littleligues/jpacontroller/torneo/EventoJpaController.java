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
import com.spontecorp.littleligues.model.torneo.TipoEvento;
import com.spontecorp.littleligues.model.torneo.Partido;
import com.spontecorp.littleligues.model.torneo.Convocado;
import com.spontecorp.littleligues.model.torneo.Evento;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class EventoJpaController implements Serializable {

    public EventoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Evento evento) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoEvento tipoEventoId = evento.getTipoEventoId();
            if (tipoEventoId != null) {
                tipoEventoId = em.getReference(tipoEventoId.getClass(), tipoEventoId.getId());
                evento.setTipoEventoId(tipoEventoId);
            }
            Partido partidoId = evento.getPartidoId();
            if (partidoId != null) {
                partidoId = em.getReference(partidoId.getClass(), partidoId.getId());
                evento.setPartidoId(partidoId);
            }
            Convocado convocadoId = evento.getConvocadoId();
            if (convocadoId != null) {
                convocadoId = em.getReference(convocadoId.getClass(), convocadoId.getId());
                evento.setConvocadoId(convocadoId);
            }
            em.persist(evento);
            if (tipoEventoId != null) {
                tipoEventoId.getEventoList().add(evento);
                tipoEventoId = em.merge(tipoEventoId);
            }
            if (partidoId != null) {
                partidoId.getEventoList().add(evento);
                partidoId = em.merge(partidoId);
            }
            if (convocadoId != null) {
                convocadoId.getEventoList().add(evento);
                convocadoId = em.merge(convocadoId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Evento evento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Evento persistentEvento = em.find(Evento.class, evento.getId());
            TipoEvento tipoEventoIdOld = persistentEvento.getTipoEventoId();
            TipoEvento tipoEventoIdNew = evento.getTipoEventoId();
            Partido partidoIdOld = persistentEvento.getPartidoId();
            Partido partidoIdNew = evento.getPartidoId();
            Convocado convocadoIdOld = persistentEvento.getConvocadoId();
            Convocado convocadoIdNew = evento.getConvocadoId();
            if (tipoEventoIdNew != null) {
                tipoEventoIdNew = em.getReference(tipoEventoIdNew.getClass(), tipoEventoIdNew.getId());
                evento.setTipoEventoId(tipoEventoIdNew);
            }
            if (partidoIdNew != null) {
                partidoIdNew = em.getReference(partidoIdNew.getClass(), partidoIdNew.getId());
                evento.setPartidoId(partidoIdNew);
            }
            if (convocadoIdNew != null) {
                convocadoIdNew = em.getReference(convocadoIdNew.getClass(), convocadoIdNew.getId());
                evento.setConvocadoId(convocadoIdNew);
            }
            evento = em.merge(evento);
            if (tipoEventoIdOld != null && !tipoEventoIdOld.equals(tipoEventoIdNew)) {
                tipoEventoIdOld.getEventoList().remove(evento);
                tipoEventoIdOld = em.merge(tipoEventoIdOld);
            }
            if (tipoEventoIdNew != null && !tipoEventoIdNew.equals(tipoEventoIdOld)) {
                tipoEventoIdNew.getEventoList().add(evento);
                tipoEventoIdNew = em.merge(tipoEventoIdNew);
            }
            if (partidoIdOld != null && !partidoIdOld.equals(partidoIdNew)) {
                partidoIdOld.getEventoList().remove(evento);
                partidoIdOld = em.merge(partidoIdOld);
            }
            if (partidoIdNew != null && !partidoIdNew.equals(partidoIdOld)) {
                partidoIdNew.getEventoList().add(evento);
                partidoIdNew = em.merge(partidoIdNew);
            }
            if (convocadoIdOld != null && !convocadoIdOld.equals(convocadoIdNew)) {
                convocadoIdOld.getEventoList().remove(evento);
                convocadoIdOld = em.merge(convocadoIdOld);
            }
            if (convocadoIdNew != null && !convocadoIdNew.equals(convocadoIdOld)) {
                convocadoIdNew.getEventoList().add(evento);
                convocadoIdNew = em.merge(convocadoIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = evento.getId();
                if (findEvento(id) == null) {
                    throw new NonexistentEntityException("The evento with id " + id + " no longer exists.");
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
            Evento evento;
            try {
                evento = em.getReference(Evento.class, id);
                evento.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The evento with id " + id + " no longer exists.", enfe);
            }
            TipoEvento tipoEventoId = evento.getTipoEventoId();
            if (tipoEventoId != null) {
                tipoEventoId.getEventoList().remove(evento);
                tipoEventoId = em.merge(tipoEventoId);
            }
            Partido partidoId = evento.getPartidoId();
            if (partidoId != null) {
                partidoId.getEventoList().remove(evento);
                partidoId = em.merge(partidoId);
            }
            Convocado convocadoId = evento.getConvocadoId();
            if (convocadoId != null) {
                convocadoId.getEventoList().remove(evento);
                convocadoId = em.merge(convocadoId);
            }
            em.remove(evento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Evento> findEventoEntities() {
        return findEventoEntities(true, -1, -1);
    }

    public List<Evento> findEventoEntities(int maxResults, int firstResult) {
        return findEventoEntities(false, maxResults, firstResult);
    }

    private List<Evento> findEventoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Evento.class));
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

    public Evento findEvento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Evento.class, id);
        } finally {
            em.close();
        }
    }

    public int getEventoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Evento> rt = cq.from(Evento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
