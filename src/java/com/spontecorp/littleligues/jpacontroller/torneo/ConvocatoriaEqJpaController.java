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
import com.spontecorp.littleligues.model.torneo.Partido;
import com.spontecorp.littleligues.model.liga.Equipo;
import com.spontecorp.littleligues.model.torneo.Convocado;
import com.spontecorp.littleligues.model.torneo.ConvocatoriaEq;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class ConvocatoriaEqJpaController implements Serializable {

    public ConvocatoriaEqJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ConvocatoriaEq convocatoriaEq) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Partido partidoId = convocatoriaEq.getPartidoId();
            if (partidoId != null) {
                partidoId = em.getReference(partidoId.getClass(), partidoId.getId());
                convocatoriaEq.setPartidoId(partidoId);
            }
            Equipo equipoId = convocatoriaEq.getEquipoId();
            if (equipoId != null) {
                equipoId = em.getReference(equipoId.getClass(), equipoId.getId());
                convocatoriaEq.setEquipoId(equipoId);
            }
            Convocado convocadosId = convocatoriaEq.getConvocadosId();
            if (convocadosId != null) {
                convocadosId = em.getReference(convocadosId.getClass(), convocadosId.getId());
                convocatoriaEq.setConvocadosId(convocadosId);
            }
            em.persist(convocatoriaEq);
            if (partidoId != null) {
                partidoId.getConvocatoriaEqList().add(convocatoriaEq);
                partidoId = em.merge(partidoId);
            }
            if (equipoId != null) {
                equipoId.getConvocatoriaEqList().add(convocatoriaEq);
                equipoId = em.merge(equipoId);
            }
            if (convocadosId != null) {
                convocadosId.getConvocatoriaEqList().add(convocatoriaEq);
                convocadosId = em.merge(convocadosId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ConvocatoriaEq convocatoriaEq) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ConvocatoriaEq persistentConvocatoriaEq = em.find(ConvocatoriaEq.class, convocatoriaEq.getId());
            Partido partidoIdOld = persistentConvocatoriaEq.getPartidoId();
            Partido partidoIdNew = convocatoriaEq.getPartidoId();
            Equipo equipoIdOld = persistentConvocatoriaEq.getEquipoId();
            Equipo equipoIdNew = convocatoriaEq.getEquipoId();
            Convocado convocadosIdOld = persistentConvocatoriaEq.getConvocadosId();
            Convocado convocadosIdNew = convocatoriaEq.getConvocadosId();
            if (partidoIdNew != null) {
                partidoIdNew = em.getReference(partidoIdNew.getClass(), partidoIdNew.getId());
                convocatoriaEq.setPartidoId(partidoIdNew);
            }
            if (equipoIdNew != null) {
                equipoIdNew = em.getReference(equipoIdNew.getClass(), equipoIdNew.getId());
                convocatoriaEq.setEquipoId(equipoIdNew);
            }
            if (convocadosIdNew != null) {
                convocadosIdNew = em.getReference(convocadosIdNew.getClass(), convocadosIdNew.getId());
                convocatoriaEq.setConvocadosId(convocadosIdNew);
            }
            convocatoriaEq = em.merge(convocatoriaEq);
            if (partidoIdOld != null && !partidoIdOld.equals(partidoIdNew)) {
                partidoIdOld.getConvocatoriaEqList().remove(convocatoriaEq);
                partidoIdOld = em.merge(partidoIdOld);
            }
            if (partidoIdNew != null && !partidoIdNew.equals(partidoIdOld)) {
                partidoIdNew.getConvocatoriaEqList().add(convocatoriaEq);
                partidoIdNew = em.merge(partidoIdNew);
            }
            if (equipoIdOld != null && !equipoIdOld.equals(equipoIdNew)) {
                equipoIdOld.getConvocatoriaEqList().remove(convocatoriaEq);
                equipoIdOld = em.merge(equipoIdOld);
            }
            if (equipoIdNew != null && !equipoIdNew.equals(equipoIdOld)) {
                equipoIdNew.getConvocatoriaEqList().add(convocatoriaEq);
                equipoIdNew = em.merge(equipoIdNew);
            }
            if (convocadosIdOld != null && !convocadosIdOld.equals(convocadosIdNew)) {
                convocadosIdOld.getConvocatoriaEqList().remove(convocatoriaEq);
                convocadosIdOld = em.merge(convocadosIdOld);
            }
            if (convocadosIdNew != null && !convocadosIdNew.equals(convocadosIdOld)) {
                convocadosIdNew.getConvocatoriaEqList().add(convocatoriaEq);
                convocadosIdNew = em.merge(convocadosIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = convocatoriaEq.getId();
                if (findConvocatoriaEq(id) == null) {
                    throw new NonexistentEntityException("The convocatoriaEq with id " + id + " no longer exists.");
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
            ConvocatoriaEq convocatoriaEq;
            try {
                convocatoriaEq = em.getReference(ConvocatoriaEq.class, id);
                convocatoriaEq.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The convocatoriaEq with id " + id + " no longer exists.", enfe);
            }
            Partido partidoId = convocatoriaEq.getPartidoId();
            if (partidoId != null) {
                partidoId.getConvocatoriaEqList().remove(convocatoriaEq);
                partidoId = em.merge(partidoId);
            }
            Equipo equipoId = convocatoriaEq.getEquipoId();
            if (equipoId != null) {
                equipoId.getConvocatoriaEqList().remove(convocatoriaEq);
                equipoId = em.merge(equipoId);
            }
            Convocado convocadosId = convocatoriaEq.getConvocadosId();
            if (convocadosId != null) {
                convocadosId.getConvocatoriaEqList().remove(convocatoriaEq);
                convocadosId = em.merge(convocadosId);
            }
            em.remove(convocatoriaEq);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ConvocatoriaEq> findConvocatoriaEqEntities() {
        return findConvocatoriaEqEntities(true, -1, -1);
    }

    public List<ConvocatoriaEq> findConvocatoriaEqEntities(int maxResults, int firstResult) {
        return findConvocatoriaEqEntities(false, maxResults, firstResult);
    }

    private List<ConvocatoriaEq> findConvocatoriaEqEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ConvocatoriaEq.class));
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

    public ConvocatoriaEq findConvocatoriaEq(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ConvocatoriaEq.class, id);
        } finally {
            em.close();
        }
    }

    public int getConvocatoriaEqCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ConvocatoriaEq> rt = cq.from(ConvocatoriaEq.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
