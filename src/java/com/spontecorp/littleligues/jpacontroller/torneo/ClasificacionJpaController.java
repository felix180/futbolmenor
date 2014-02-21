/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller.torneo;

import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.NonexistentEntityException;
import com.spontecorp.littleligues.model.liga.Equipo;
import com.spontecorp.littleligues.model.torneo.Clasificacion;
import com.spontecorp.littleligues.model.torneo.Jornada;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author jgcastillo
 */
public class ClasificacionJpaController implements Serializable {

    public ClasificacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clasificacion clasificacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jornada jornadaId = clasificacion.getJornadaId();
            if (jornadaId != null) {
                jornadaId = em.getReference(jornadaId.getClass(), jornadaId.getId());
                clasificacion.setJornadaId(jornadaId);
            }
            Equipo equipoId = clasificacion.getEquipoId();
            if (equipoId != null) {
                equipoId = em.getReference(equipoId.getClass(), equipoId.getId());
                clasificacion.setEquipoId(equipoId);
            }
            em.persist(clasificacion);
            if (jornadaId != null) {
                jornadaId.getClasificacionList().add(clasificacion);
                jornadaId = em.merge(jornadaId);
            }
            if (equipoId != null) {
                equipoId.getClasificacionList().add(clasificacion);
                equipoId = em.merge(equipoId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clasificacion clasificacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clasificacion persistentClasificacion = em.find(Clasificacion.class, clasificacion.getId());
            Jornada jornadaIdOld = persistentClasificacion.getJornadaId();
            Jornada jornadaIdNew = clasificacion.getJornadaId();
            Equipo equipoIdOld = persistentClasificacion.getEquipoId();
            Equipo equipoIdNew = clasificacion.getEquipoId();
            if (jornadaIdNew != null) {
                jornadaIdNew = em.getReference(jornadaIdNew.getClass(), jornadaIdNew.getId());
                clasificacion.setJornadaId(jornadaIdNew);
            }
            if (equipoIdNew != null) {
                equipoIdNew = em.getReference(equipoIdNew.getClass(), equipoIdNew.getId());
                clasificacion.setEquipoId(equipoIdNew);
            }
            clasificacion = em.merge(clasificacion);
            if (jornadaIdOld != null && !jornadaIdOld.equals(jornadaIdNew)) {
                jornadaIdOld.getClasificacionList().remove(clasificacion);
                jornadaIdOld = em.merge(jornadaIdOld);
            }
            if (jornadaIdNew != null && !jornadaIdNew.equals(jornadaIdOld)) {
                jornadaIdNew.getClasificacionList().add(clasificacion);
                jornadaIdNew = em.merge(jornadaIdNew);
            }
            if (equipoIdOld != null && !equipoIdOld.equals(equipoIdNew)) {
                equipoIdOld.getClasificacionList().remove(clasificacion);
                equipoIdOld = em.merge(equipoIdOld);
            }
            if (equipoIdNew != null && !equipoIdNew.equals(equipoIdOld)) {
                equipoIdNew.getClasificacionList().add(clasificacion);
                equipoIdNew = em.merge(equipoIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clasificacion.getId();
                if (findClasificacion(id) == null) {
                    throw new NonexistentEntityException("The clasificacion with id " + id + " no longer exists.");
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
            Clasificacion clasificacion;
            try {
                clasificacion = em.getReference(Clasificacion.class, id);
                clasificacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clasificacion with id " + id + " no longer exists.", enfe);
            }
            Jornada jornadaId = clasificacion.getJornadaId();
            if (jornadaId != null) {
                jornadaId.getClasificacionList().remove(clasificacion);
                jornadaId = em.merge(jornadaId);
            }
            Equipo equipoId = clasificacion.getEquipoId();
            if (equipoId != null) {
                equipoId.getClasificacionList().remove(clasificacion);
                equipoId = em.merge(equipoId);
            }
            em.remove(clasificacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Clasificacion> findClasificacionEntities() {
        return findClasificacionEntities(true, -1, -1);
    }

    public List<Clasificacion> findClasificacionEntities(int maxResults, int firstResult) {
        return findClasificacionEntities(false, maxResults, firstResult);
    }

    private List<Clasificacion> findClasificacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clasificacion.class));
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

    public Clasificacion findClasificacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clasificacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getClasificacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clasificacion> rt = cq.from(Clasificacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
