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
import com.spontecorp.littleligues.model.liga.Equipo;
import com.spontecorp.littleligues.model.torneo.Convocado;
import com.spontecorp.littleligues.model.torneo.Jugador;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class JugadorJpaController implements Serializable {

    public JugadorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Jugador jugador) {
        if (jugador.getConvocadoList() == null) {
            jugador.setConvocadoList(new ArrayList<Convocado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipo equipoId = jugador.getEquipoId();
            if (equipoId != null) {
                equipoId = em.getReference(equipoId.getClass(), equipoId.getId());
                jugador.setEquipoId(equipoId);
            }
            List<Convocado> attachedConvocadoList = new ArrayList<Convocado>();
            for (Convocado convocadoListConvocadoToAttach : jugador.getConvocadoList()) {
                convocadoListConvocadoToAttach = em.getReference(convocadoListConvocadoToAttach.getClass(), convocadoListConvocadoToAttach.getId());
                attachedConvocadoList.add(convocadoListConvocadoToAttach);
            }
            jugador.setConvocadoList(attachedConvocadoList);
            em.persist(jugador);
            if (equipoId != null) {
                equipoId.getJugadorList().add(jugador);
                equipoId = em.merge(equipoId);
            }
            for (Convocado convocadoListConvocado : jugador.getConvocadoList()) {
                Jugador oldJugadorIdOfConvocadoListConvocado = convocadoListConvocado.getJugadorId();
                convocadoListConvocado.setJugadorId(jugador);
                convocadoListConvocado = em.merge(convocadoListConvocado);
                if (oldJugadorIdOfConvocadoListConvocado != null) {
                    oldJugadorIdOfConvocadoListConvocado.getConvocadoList().remove(convocadoListConvocado);
                    oldJugadorIdOfConvocadoListConvocado = em.merge(oldJugadorIdOfConvocadoListConvocado);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Jugador jugador) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugador persistentJugador = em.find(Jugador.class, jugador.getId());
            Equipo equipoIdOld = persistentJugador.getEquipoId();
            Equipo equipoIdNew = jugador.getEquipoId();
            List<Convocado> convocadoListOld = persistentJugador.getConvocadoList();
            List<Convocado> convocadoListNew = jugador.getConvocadoList();
            if (equipoIdNew != null) {
                equipoIdNew = em.getReference(equipoIdNew.getClass(), equipoIdNew.getId());
                jugador.setEquipoId(equipoIdNew);
            }
            List<Convocado> attachedConvocadoListNew = new ArrayList<Convocado>();
            for (Convocado convocadoListNewConvocadoToAttach : convocadoListNew) {
                convocadoListNewConvocadoToAttach = em.getReference(convocadoListNewConvocadoToAttach.getClass(), convocadoListNewConvocadoToAttach.getId());
                attachedConvocadoListNew.add(convocadoListNewConvocadoToAttach);
            }
            convocadoListNew = attachedConvocadoListNew;
            jugador.setConvocadoList(convocadoListNew);
            jugador = em.merge(jugador);
            if (equipoIdOld != null && !equipoIdOld.equals(equipoIdNew)) {
                equipoIdOld.getJugadorList().remove(jugador);
                equipoIdOld = em.merge(equipoIdOld);
            }
            if (equipoIdNew != null && !equipoIdNew.equals(equipoIdOld)) {
                equipoIdNew.getJugadorList().add(jugador);
                equipoIdNew = em.merge(equipoIdNew);
            }
            for (Convocado convocadoListOldConvocado : convocadoListOld) {
                if (!convocadoListNew.contains(convocadoListOldConvocado)) {
                    convocadoListOldConvocado.setJugadorId(null);
                    convocadoListOldConvocado = em.merge(convocadoListOldConvocado);
                }
            }
            for (Convocado convocadoListNewConvocado : convocadoListNew) {
                if (!convocadoListOld.contains(convocadoListNewConvocado)) {
                    Jugador oldJugadorIdOfConvocadoListNewConvocado = convocadoListNewConvocado.getJugadorId();
                    convocadoListNewConvocado.setJugadorId(jugador);
                    convocadoListNewConvocado = em.merge(convocadoListNewConvocado);
                    if (oldJugadorIdOfConvocadoListNewConvocado != null && !oldJugadorIdOfConvocadoListNewConvocado.equals(jugador)) {
                        oldJugadorIdOfConvocadoListNewConvocado.getConvocadoList().remove(convocadoListNewConvocado);
                        oldJugadorIdOfConvocadoListNewConvocado = em.merge(oldJugadorIdOfConvocadoListNewConvocado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = jugador.getId();
                if (findJugador(id) == null) {
                    throw new NonexistentEntityException("The jugador with id " + id + " no longer exists.");
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
            Jugador jugador;
            try {
                jugador = em.getReference(Jugador.class, id);
                jugador.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jugador with id " + id + " no longer exists.", enfe);
            }
            Equipo equipoId = jugador.getEquipoId();
            if (equipoId != null) {
                equipoId.getJugadorList().remove(jugador);
                equipoId = em.merge(equipoId);
            }
            List<Convocado> convocadoList = jugador.getConvocadoList();
            for (Convocado convocadoListConvocado : convocadoList) {
                convocadoListConvocado.setJugadorId(null);
                convocadoListConvocado = em.merge(convocadoListConvocado);
            }
            em.remove(jugador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Jugador> findJugadorEntities() {
        return findJugadorEntities(true, -1, -1);
    }

    public List<Jugador> findJugadorEntities(int maxResults, int firstResult) {
        return findJugadorEntities(false, maxResults, firstResult);
    }

    private List<Jugador> findJugadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Jugador.class));
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

    public Jugador findJugador(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Jugador.class, id);
        } finally {
            em.close();
        }
    }

    public int getJugadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Jugador> rt = cq.from(Jugador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
