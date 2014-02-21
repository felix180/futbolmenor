/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller.torneo;

import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.IllegalOrphanException;
import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.NonexistentEntityException;
import com.spontecorp.littleligues.model.torneo.Convocado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.littleligues.model.torneo.Staff;
import com.spontecorp.littleligues.model.torneo.Jugador;
import com.spontecorp.littleligues.model.torneo.Evento;
import java.util.ArrayList;
import java.util.List;
import com.spontecorp.littleligues.model.torneo.ConvocatoriaEq;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class ConvocadoJpaController implements Serializable {

    public ConvocadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Convocado convocado) {
        if (convocado.getEventoList() == null) {
            convocado.setEventoList(new ArrayList<Evento>());
        }
        if (convocado.getConvocatoriaEqList() == null) {
            convocado.setConvocatoriaEqList(new ArrayList<ConvocatoriaEq>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Staff staffId = convocado.getStaffId();
            if (staffId != null) {
                staffId = em.getReference(staffId.getClass(), staffId.getId());
                convocado.setStaffId(staffId);
            }
            Jugador jugadorId = convocado.getJugadorId();
            if (jugadorId != null) {
                jugadorId = em.getReference(jugadorId.getClass(), jugadorId.getId());
                convocado.setJugadorId(jugadorId);
            }
            List<Evento> attachedEventoList = new ArrayList<Evento>();
            for (Evento eventoListEventoToAttach : convocado.getEventoList()) {
                eventoListEventoToAttach = em.getReference(eventoListEventoToAttach.getClass(), eventoListEventoToAttach.getId());
                attachedEventoList.add(eventoListEventoToAttach);
            }
            convocado.setEventoList(attachedEventoList);
            List<ConvocatoriaEq> attachedConvocatoriaEqList = new ArrayList<ConvocatoriaEq>();
            for (ConvocatoriaEq convocatoriaEqListConvocatoriaEqToAttach : convocado.getConvocatoriaEqList()) {
                convocatoriaEqListConvocatoriaEqToAttach = em.getReference(convocatoriaEqListConvocatoriaEqToAttach.getClass(), convocatoriaEqListConvocatoriaEqToAttach.getId());
                attachedConvocatoriaEqList.add(convocatoriaEqListConvocatoriaEqToAttach);
            }
            convocado.setConvocatoriaEqList(attachedConvocatoriaEqList);
            em.persist(convocado);
            if (staffId != null) {
                staffId.getConvocadoList().add(convocado);
                staffId = em.merge(staffId);
            }
            if (jugadorId != null) {
                jugadorId.getConvocadoList().add(convocado);
                jugadorId = em.merge(jugadorId);
            }
            for (Evento eventoListEvento : convocado.getEventoList()) {
                Convocado oldConvocadoIdOfEventoListEvento = eventoListEvento.getConvocadoId();
                eventoListEvento.setConvocadoId(convocado);
                eventoListEvento = em.merge(eventoListEvento);
                if (oldConvocadoIdOfEventoListEvento != null) {
                    oldConvocadoIdOfEventoListEvento.getEventoList().remove(eventoListEvento);
                    oldConvocadoIdOfEventoListEvento = em.merge(oldConvocadoIdOfEventoListEvento);
                }
            }
            for (ConvocatoriaEq convocatoriaEqListConvocatoriaEq : convocado.getConvocatoriaEqList()) {
                Convocado oldConvocadosIdOfConvocatoriaEqListConvocatoriaEq = convocatoriaEqListConvocatoriaEq.getConvocadosId();
                convocatoriaEqListConvocatoriaEq.setConvocadosId(convocado);
                convocatoriaEqListConvocatoriaEq = em.merge(convocatoriaEqListConvocatoriaEq);
                if (oldConvocadosIdOfConvocatoriaEqListConvocatoriaEq != null) {
                    oldConvocadosIdOfConvocatoriaEqListConvocatoriaEq.getConvocatoriaEqList().remove(convocatoriaEqListConvocatoriaEq);
                    oldConvocadosIdOfConvocatoriaEqListConvocatoriaEq = em.merge(oldConvocadosIdOfConvocatoriaEqListConvocatoriaEq);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Convocado convocado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Convocado persistentConvocado = em.find(Convocado.class, convocado.getId());
            Staff staffIdOld = persistentConvocado.getStaffId();
            Staff staffIdNew = convocado.getStaffId();
            Jugador jugadorIdOld = persistentConvocado.getJugadorId();
            Jugador jugadorIdNew = convocado.getJugadorId();
            List<Evento> eventoListOld = persistentConvocado.getEventoList();
            List<Evento> eventoListNew = convocado.getEventoList();
            List<ConvocatoriaEq> convocatoriaEqListOld = persistentConvocado.getConvocatoriaEqList();
            List<ConvocatoriaEq> convocatoriaEqListNew = convocado.getConvocatoriaEqList();
            List<String> illegalOrphanMessages = null;
            for (Evento eventoListOldEvento : eventoListOld) {
                if (!eventoListNew.contains(eventoListOldEvento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Evento " + eventoListOldEvento + " since its convocadoId field is not nullable.");
                }
            }
            for (ConvocatoriaEq convocatoriaEqListOldConvocatoriaEq : convocatoriaEqListOld) {
                if (!convocatoriaEqListNew.contains(convocatoriaEqListOldConvocatoriaEq)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ConvocatoriaEq " + convocatoriaEqListOldConvocatoriaEq + " since its convocadosId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (staffIdNew != null) {
                staffIdNew = em.getReference(staffIdNew.getClass(), staffIdNew.getId());
                convocado.setStaffId(staffIdNew);
            }
            if (jugadorIdNew != null) {
                jugadorIdNew = em.getReference(jugadorIdNew.getClass(), jugadorIdNew.getId());
                convocado.setJugadorId(jugadorIdNew);
            }
            List<Evento> attachedEventoListNew = new ArrayList<Evento>();
            for (Evento eventoListNewEventoToAttach : eventoListNew) {
                eventoListNewEventoToAttach = em.getReference(eventoListNewEventoToAttach.getClass(), eventoListNewEventoToAttach.getId());
                attachedEventoListNew.add(eventoListNewEventoToAttach);
            }
            eventoListNew = attachedEventoListNew;
            convocado.setEventoList(eventoListNew);
            List<ConvocatoriaEq> attachedConvocatoriaEqListNew = new ArrayList<ConvocatoriaEq>();
            for (ConvocatoriaEq convocatoriaEqListNewConvocatoriaEqToAttach : convocatoriaEqListNew) {
                convocatoriaEqListNewConvocatoriaEqToAttach = em.getReference(convocatoriaEqListNewConvocatoriaEqToAttach.getClass(), convocatoriaEqListNewConvocatoriaEqToAttach.getId());
                attachedConvocatoriaEqListNew.add(convocatoriaEqListNewConvocatoriaEqToAttach);
            }
            convocatoriaEqListNew = attachedConvocatoriaEqListNew;
            convocado.setConvocatoriaEqList(convocatoriaEqListNew);
            convocado = em.merge(convocado);
            if (staffIdOld != null && !staffIdOld.equals(staffIdNew)) {
                staffIdOld.getConvocadoList().remove(convocado);
                staffIdOld = em.merge(staffIdOld);
            }
            if (staffIdNew != null && !staffIdNew.equals(staffIdOld)) {
                staffIdNew.getConvocadoList().add(convocado);
                staffIdNew = em.merge(staffIdNew);
            }
            if (jugadorIdOld != null && !jugadorIdOld.equals(jugadorIdNew)) {
                jugadorIdOld.getConvocadoList().remove(convocado);
                jugadorIdOld = em.merge(jugadorIdOld);
            }
            if (jugadorIdNew != null && !jugadorIdNew.equals(jugadorIdOld)) {
                jugadorIdNew.getConvocadoList().add(convocado);
                jugadorIdNew = em.merge(jugadorIdNew);
            }
            for (Evento eventoListNewEvento : eventoListNew) {
                if (!eventoListOld.contains(eventoListNewEvento)) {
                    Convocado oldConvocadoIdOfEventoListNewEvento = eventoListNewEvento.getConvocadoId();
                    eventoListNewEvento.setConvocadoId(convocado);
                    eventoListNewEvento = em.merge(eventoListNewEvento);
                    if (oldConvocadoIdOfEventoListNewEvento != null && !oldConvocadoIdOfEventoListNewEvento.equals(convocado)) {
                        oldConvocadoIdOfEventoListNewEvento.getEventoList().remove(eventoListNewEvento);
                        oldConvocadoIdOfEventoListNewEvento = em.merge(oldConvocadoIdOfEventoListNewEvento);
                    }
                }
            }
            for (ConvocatoriaEq convocatoriaEqListNewConvocatoriaEq : convocatoriaEqListNew) {
                if (!convocatoriaEqListOld.contains(convocatoriaEqListNewConvocatoriaEq)) {
                    Convocado oldConvocadosIdOfConvocatoriaEqListNewConvocatoriaEq = convocatoriaEqListNewConvocatoriaEq.getConvocadosId();
                    convocatoriaEqListNewConvocatoriaEq.setConvocadosId(convocado);
                    convocatoriaEqListNewConvocatoriaEq = em.merge(convocatoriaEqListNewConvocatoriaEq);
                    if (oldConvocadosIdOfConvocatoriaEqListNewConvocatoriaEq != null && !oldConvocadosIdOfConvocatoriaEqListNewConvocatoriaEq.equals(convocado)) {
                        oldConvocadosIdOfConvocatoriaEqListNewConvocatoriaEq.getConvocatoriaEqList().remove(convocatoriaEqListNewConvocatoriaEq);
                        oldConvocadosIdOfConvocatoriaEqListNewConvocatoriaEq = em.merge(oldConvocadosIdOfConvocatoriaEqListNewConvocatoriaEq);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = convocado.getId();
                if (findConvocado(id) == null) {
                    throw new NonexistentEntityException("The convocado with id " + id + " no longer exists.");
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
            Convocado convocado;
            try {
                convocado = em.getReference(Convocado.class, id);
                convocado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The convocado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Evento> eventoListOrphanCheck = convocado.getEventoList();
            for (Evento eventoListOrphanCheckEvento : eventoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Convocado (" + convocado + ") cannot be destroyed since the Evento " + eventoListOrphanCheckEvento + " in its eventoList field has a non-nullable convocadoId field.");
            }
            List<ConvocatoriaEq> convocatoriaEqListOrphanCheck = convocado.getConvocatoriaEqList();
            for (ConvocatoriaEq convocatoriaEqListOrphanCheckConvocatoriaEq : convocatoriaEqListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Convocado (" + convocado + ") cannot be destroyed since the ConvocatoriaEq " + convocatoriaEqListOrphanCheckConvocatoriaEq + " in its convocatoriaEqList field has a non-nullable convocadosId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Staff staffId = convocado.getStaffId();
            if (staffId != null) {
                staffId.getConvocadoList().remove(convocado);
                staffId = em.merge(staffId);
            }
            Jugador jugadorId = convocado.getJugadorId();
            if (jugadorId != null) {
                jugadorId.getConvocadoList().remove(convocado);
                jugadorId = em.merge(jugadorId);
            }
            em.remove(convocado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Convocado> findConvocadoEntities() {
        return findConvocadoEntities(true, -1, -1);
    }

    public List<Convocado> findConvocadoEntities(int maxResults, int firstResult) {
        return findConvocadoEntities(false, maxResults, firstResult);
    }

    private List<Convocado> findConvocadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Convocado.class));
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

    public Convocado findConvocado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Convocado.class, id);
        } finally {
            em.close();
        }
    }

    public int getConvocadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Convocado> rt = cq.from(Convocado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
